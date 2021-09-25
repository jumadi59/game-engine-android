package com.jumbox.blockpuzzle.game.scene

import android.util.Log
import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.Config
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.game.Particle
import com.jumbox.blockpuzzle.game.block.Block
import com.jumbox.blockpuzzle.game.block.Piece
import com.jumbox.blockpuzzle.game.effect.DefaultEffect
import com.jumbox.blockpuzzle.game.layer.GameOverLayer
import com.jumbox.blockpuzzle.game.layer.MainLayer
import com.jumbox.blockpuzzle.game.layer.PauseLayer
import com.jumbox.blockpuzzle.serializer.BinSerializable
import com.jumbox.blockpuzzle.serializer.BinSerializer
import com.jumbox.game.android.Engine
import com.jumbox.game.android.Game
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.control.InputTouchController
import com.jumbox.game.android.control.MultiInputTouchListener
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.graphics.SpriteRender
import com.jumbox.game.android.screen.Scene
import com.jumbox.game.android.ui.widget.Button
import com.jumbox.game.android.ui.widget.ImageButton
import com.jumbox.game.android.ui.widget.Text
import com.jumbox.game.android.util.Extension.dpToPx
import com.jumbox.game.android.util.Files
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.roundToInt


/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class GameScene(game: Game) : Scene, InputTouchController, BinSerializable {

    private val touchListener = MultiInputTouchListener()
    private val batch = SpriteRender()
    private val cellCount = Config.CELL_COUNT
    private val space = 2.5f.dpToPx()
    private val screenWidth = Engine.game.width
    private val screenHeight = Engine.game.height
    private var marginWidth = 0f
    private var marginHeight = 0f
    private var availableWidth = 0f
    private var boardHeight = 0f
    private var pieceHolderHeight = 0f
    private val pieceCount = 3
    var cellWidth = 10f
    var currentPiece = -1

    lateinit var gridBlocks: Array<Array<Block?>>
    var pieces: Array<Piece?> = Array(3) { null }
    var originalPositions = Array<Rectangle?>(3) { null }
    private val effects = ArrayList<DefaultEffect>()
    var lastPutPiecePos = Vector()

    private val styleText = Text.Style().apply {
        font = AssetManager.manager.fontMacan
        fontSize = 32f
        color = "#666d75".toColor()
    }
    private val particles = ArrayList<Particle>()

    private val mainLayer = MainLayer()
    private val pauseLayer = PauseLayer()
    private val gameOverLayer = GameOverLayer()

    init {
        mainLayer.create()
        pauseLayer.create()
        gameOverLayer.create()

        marginWidth = screenWidth * 0.10f
        marginHeight = screenHeight * 0.10f
        availableWidth = screenWidth - marginWidth * 2f

        boardHeight = screenHeight * 0.50f
        pieceHolderHeight = screenHeight * 0.25f

        createGridBlock()
        createPieces()

        load()

        mainLayer.find<ImageButton>("btnPause")?.onPressed = {
            pauseLayer.show()
        }
        mainLayer.find<ImageButton>("btnReset")?.onPressed = {
            reset()
        }

        gameOverLayer.find<Button>("btnQuit")?.onPressed = {
            game.back()
            reset()
        }
        gameOverLayer.find<Button>("btnRetry")?.onPressed = {
            gameOverLayer.pause()
            reset()
        }
    }

    private fun createGridBlock() {
        gridBlocks = Array(cellCount) { Array(cellCount) { null } }
        cellWidth = availableWidth.coerceAtMost(boardHeight) / cellCount

        for (row in 0 until cellCount) {
            for (column in 0 until cellCount) {
                val x = (column * cellWidth) + space
                val y = (row * cellWidth) + space
                val size = cellWidth - space
                gridBlocks[row][column] = Block(
                    marginWidth + x,
                    marginHeight + pieceHolderHeight + y,
                    size.toInt()
                )
            }
        }
    }

    private fun createPieces() {
        pieces = Array(pieceCount) { Piece.random() }
        originalPositions = Array(pieceCount) { Rectangle() }

        AssetManager.manager.takeSound.play()
        updatePiece()

    }

    private fun addParticle(vector: Vector, addScore: Int) {
        particles.add(Particle(vector, addScore, styleText))
    }

    private fun updatePiece() {
        val width = availableWidth
        val height = pieceHolderHeight - marginHeight
        val perPieceWidth = width / pieceCount

        for ((i, piece) in pieces.withIndex()) {
            piece?.let {
                it.space = space
                val x = marginWidth + (i * perPieceWidth)
                it.cellSize = (perPieceWidth / piece.cellCols).coerceAtMost(height / it.cellRows).coerceAtMost(
                    cellWidth
                )
                it.set(
                    x + ((perPieceWidth - it.width) / 2),
                    marginHeight + ((pieceHolderHeight - it.height) / 2)
                )
                originalPositions[i] = Rectangle(it.x, it.y, it.cellSize, it.cellSize)

                it.cellSize = 0f
            }
        }
    }

    private fun handFinished(): Boolean {
        for (element in pieces)
            if (element != null) return false

        return true
    }

    private fun pickPiece(mouse: Vector): Boolean {
        val perPieceWidth = availableWidth / pieces.size
        for ((i, piece) in pieces.withIndex()) {
            piece?.let {
                val rectangle = Rectangle(
                    marginWidth + (i * perPieceWidth),
                    marginHeight,
                    perPieceWidth,
                    pieceHolderHeight
                )
                if (rectangle.contains(mouse)) {
                    currentPiece = i
                    return true
                }
            }
        }
        currentPiece = -1
        return false
    }

    private fun dropPiece(result: (count: Int, pos: Vector) -> Unit): Boolean {
        if (currentPiece >= 0) {
            val piece = pieces[currentPiece]!!
            val isPut = putScreenPiece(piece)
            if (isPut) {
                AssetManager.manager.dropSound.play()
                result.invoke(piece.calculateArea(), piece.calculateGravityCenter())
                pieces[currentPiece] = null
            } else {
                AssetManager.manager.invalidDrop.play()
            }
            currentPiece = -1
            if (handFinished()) createPieces()

            return isPut
        }
        return false
    }

    private fun canPutPiece(piece: Piece, x: Int, y: Int): Boolean {
        if (!inBounds(piece, x, y)) return false

        lastPutPiecePos.set(piece.calculateGravityCenter())
        for (i in 0 until piece.cellRows)
            for (j in 0 until piece.cellCols)
                if (gridBlocks[y + i][x + j]?.isEmptyColor != true && piece.filled(i, j)) return false

        return true
    }

    private fun putScreenPiece(piece: Piece): Boolean {
        val boardSize = availableWidth.coerceAtMost(boardHeight)
        val local = piece.copy().subtract(
            Vector(
                screenWidth * 0.5f - boardSize * 0.5f,
                marginHeight + pieceHolderHeight
            )
        )
        val x = (local.x / piece.cellSize).roundToInt()
        val y = (local.y / piece.cellSize).roundToInt()

        if (!canPutPiece(piece, x, y)) return false

        for (i in 0 until piece.cellRows)
            for (j in 0 until piece.cellCols)
                if (piece.filled(i, j)) gridBlocks[y + i][x + j]?.colorIndex = piece.colorIndex

        return true
    }

    override fun update(delta: Float) {
        if (currentPiece >= 0) {
            pieces[currentPiece]?.let {
                val mouse = Vector(Engine.game.input.x.toFloat(), Engine.game.input.y.toFloat())
                mouse.subtract(Vector(it.width * 0.5f, -it.cellSize))
                it.lerp(mouse, 0.5f)
                it.cellSize = Interpolation.linear.apply(it.cellSize, cellWidth, 0.5f)
            }
        }

        for ((i, piece) in pieces.withIndex()) {
            if (i == currentPiece)
                continue

            piece?.let {
                originalPositions[i]?.let { rectangle ->
                    it.lerp(rectangle, 0.3f)
                    it.cellSize = Interpolation.linear.apply(
                        it.cellSize,
                        rectangle.width.toFloat(),
                        0.3f
                    )
                }
            }
        }

        mainLayer.update()
        pauseLayer.update()
        gameOverLayer.update()
    }

    override fun show() {
        touchListener.addTouchListener(this)
        touchListener.addTouchListener(mainLayer)
        Engine.game.input.touchController = touchListener
        when {
            pauseLayer.isShow -> pauseLayer.show()
            gameOverLayer.isShow -> gameOverLayer.show(mainLayer.currentScore)
            else -> mainLayer.show()
        }
    }

    override fun pause() {
        Engine.game.input.touchController = null

        if (gameOverLayer.isShow) delete()
        else save()
    }

    override fun render() {
        Engine.game.backgroundColor = BlockGame.theme.backgroundColor

        batch.begin()

        for (row in gridBlocks) {
            for (column in row) column?.draw(batch)
        }
        for ((i, piece) in pieces.withIndex()) {
            if (currentPiece == i) continue
            piece?.draw(batch)
        }

        val iteratorEffect = effects.iterator()
        while (iteratorEffect.hasNext()) {
            val effect = iteratorEffect.next()
            effect.draw(batch)
            if (effect.isDone()) iteratorEffect.remove()
        }

        val iteratorParticle = particles.iterator()
        while (iteratorParticle.hasNext()) {
            val particle = iteratorParticle.next()
            particle.run(batch)
            if (particle.done()) iteratorParticle.remove()
        }

        if (currentPiece >= 0) pieces[currentPiece]?.draw(batch)

        batch.end()

        mainLayer.draw()
        pauseLayer.draw()
        gameOverLayer.draw()
    }

    override fun dispose() {
        mainLayer.dispose()
        pauseLayer.dispose()
        gameOverLayer.dispose()
    }

    private fun inBounds(piece: Piece, x: Int, y: Int): Boolean {
        return inBounds(x, y) && inBounds(x + piece.cellCols - 1, y + piece.cellRows - 1)
    }

    private fun inBounds(x: Int, y: Int): Boolean {
        return x in 0 until cellCount && y >= 0 && y < cellCount
    }

    private fun canPutPiece(piece: Piece): Boolean {
        for (i in 0 until cellCount)
            for (j in 0 until cellCount)
                if (canPutPiece(piece, j, i)) return true

        return false
    }

    private fun isGameOver(): Boolean {
        for (piece in getAvailablePieces())
            if (canPutPiece(piece)) return false
        return true
    }

    private fun getAvailablePieces(): ArrayList<Piece> {
        val result = ArrayList<Piece>(pieceCount)
        for (piece in pieces) piece?.let { result.add(it) }
        return result
    }

    private fun clearAll(clearFromX: Int, clearFromY: Int) {
        val culprit = gridBlocks[clearFromY][clearFromX]
        for (row in gridBlocks) {
            for (col in row) {
                if (!col!!.isEmptyColor) {
                    effects.add(DefaultEffect().setInfo(col, culprit!!))
                    col.colorIndex = -1
                }
            }
        }
    }

    private fun clearComplete(): Int {
        var clearCount = 0
        val clearedRows = BooleanArray(cellCount)
        val clearedCols = BooleanArray(cellCount)

        for (i in 0 until cellCount) {
            clearedRows[i] = true
            for (j in 0 until cellCount) {
                if (gridBlocks[i][j]!!.isEmptyColor) {
                    clearedRows[i] = false
                    break
                }
            }
            if (clearedRows[i]) clearCount++
        }

        for (j in 0 until cellCount) {
            clearedCols[j] = true
            for (i in 0 until cellCount) {
                if (gridBlocks[i][j]!!.isEmptyColor) {
                    clearedCols[j] = false
                    break
                }
            }
            if (clearedCols[j]) clearCount++
        }

        if (clearCount > 0) {
            for (i in 0 until cellCount) {
                if (clearedRows[i]) {
                    for (j in 0 until cellCount) {
                        effects.add(DefaultEffect().setInfo(gridBlocks[i][j]!!, lastPutPiecePos))
                        gridBlocks[i][j]?.colorIndex = -1
                    }
                }
            }
            for (j in 0 until cellCount) {
                if (clearedCols[j]) {
                    for (i in 0 until cellCount) {
                        effects.add(DefaultEffect().setInfo(gridBlocks[i][j]!!, lastPutPiecePos))
                        gridBlocks[i][j]?.colorIndex = -1
                    }
                }
            }
        }
        return clearCount
    }

    private fun reset() {
        delete()
        var clearFromX = -1
        var clearFromY = -1
        for ((i, row) in gridBlocks.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (col != null && !col.isEmptyColor) {
                    clearFromX = j
                    clearFromY = i
                    break
                }
            }
        }

        if (clearFromX >= 0 && clearFromY >= 0) clearAll(clearFromX, clearFromY)
        mainLayer.reset()
        createPieces()
    }

    private fun calculateClearScore(stripsCleared: Int, boardSize: Int): Int {
        if (stripsCleared < 1) return 0
        return if (stripsCleared == 1) boardSize else boardSize * stripsCleared + calculateClearScore(stripsCleared - 1, boardSize)
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        return pickPiece(Vector(screenX.toFloat(), screenY.toFloat()))
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        return dropPiece { count, pos ->
            mainLayer.addScore(count)
            val bonus = calculateClearScore(clearComplete(), cellCount)
            if (bonus > 0) {
                AssetManager.manager.effectVanishSound.play()
                mainLayer.addScore(bonus)
                addParticle(pos, bonus)
            }
        }.also {
            if (it) {
                if (isGameOver()) {
                    delete()
                    gameOverLayer.show(mainLayer.currentScore)
                }
            }
        }
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        return false
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return false
    }

    private fun save() {
        val handle = Files.local(".puzzle.dt")
        try {
            BinSerializer.serialize(this, handle.write(false))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun load() {
        val handle = Files.local(".puzzle.dt")
        if (handle.exists()) {
            try {
                BinSerializer.deserialize(this, handle.reed())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun delete() {
        val handle = Files.local(".puzzle.dt")
        if (handle.exists()) handle.delete()

        Log.d("GameScene", "delete")
    }

    override fun write(out: DataOutputStream) {
        out.writeInt(cellCount)
        for (row in gridBlocks)
            for (col in row) col?.write(out)


        out.writeInt(pieceCount)
        for (i in 0 until pieceCount) {
            if (pieces[i] == null) {
                out.writeBoolean(false)
            } else {
                out.writeBoolean(true)
                pieces[i]?.write(out)
            }
        }

        mainLayer.write(out)
    }

    override fun read(`in`: DataInputStream) {
         val savedCellCount = `in`.readInt()
        if (savedCellCount != cellCount)
            throw IOException("Invalid cellCount saved.")

        for (row in gridBlocks)
            for (col in row) col?.read(`in`)


        val savedPieceCount = `in`.readInt()
        if (savedPieceCount != pieceCount) throw IOException("Invalid piece count saved.")

        for (i in 0 until pieceCount) pieces[i] = if (`in`.readBoolean()) Piece.read(`in`) else null
        updatePiece()

        mainLayer.read(`in`)
    }


}