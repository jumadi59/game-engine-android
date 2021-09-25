package com.jumbox.blockpuzzle.game.block

import com.jumbox.blockpuzzle.Config
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector
import com.jumbox.game.android.graphics.SpriteRender
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Piece : Rectangle {

    companion object {

        fun random(): Piece {
            return fromIndex(Random().nextInt(Config.COLOR_COUNT), Random().nextInt(4))
        }

        private fun fromIndex(colorIndex: Int, rotateCount: Int): Piece {
            when (colorIndex) {
                0 -> return Piece(1, 1, 0, colorIndex)
                1 -> return Piece(2, 2, 0, colorIndex)
                2 -> return Piece(3, 3, 0, colorIndex)
                3 -> return Piece(1, 2, rotateCount, colorIndex)
                4 -> return Piece(1, 3, rotateCount, colorIndex)
                5 -> return Piece(1, 4, rotateCount, colorIndex)
                6 -> return Piece(1, 5, rotateCount, colorIndex)
                7 -> return Piece(2, rotateCount, colorIndex)
                8 -> return Piece(3, rotateCount, colorIndex)
            }
            throw RuntimeException("Random function is broken.")
        }

        fun read(`in`: DataInputStream): Piece {
            return fromIndex(`in`.readInt(), `in`.readInt())
        }
    }

    var colorIndex = -1
    private var rotation = 0
    var cellCols = 0
    var cellRows = 0
    var shape: Array<BooleanArray>
    var cellSize = 10f
    set(value) {
        field = value
        width = (value * cellCols).toInt()
        height = (value * cellRows).toInt()
    }
    var space = 0f

    constructor(lSize: Int, rotateCount: Int, colorIndex: Int) {
        this.colorIndex = colorIndex

        cellCols = lSize.also { cellRows = it }
        shape = Array(lSize) { BooleanArray(lSize) {false} }

        rotation = rotateCount % 4
        when (rotation) {
            0 -> {
                for (j in 0 until lSize) shape[0][j] = true
                for (i in 0 until lSize) shape[i][0] = true
            }
            1 -> {
                for (j in 0 until lSize) shape[0][j] = true
                for (i in 0 until lSize) shape[i][lSize - 1] = true
            }
            2 -> {
                for (j in 0 until lSize) shape[lSize - 1][j] = true
                for (i in 0 until lSize) shape[i][lSize - 1] = true
            }
            3 -> {
                for (j in 0 until lSize) shape[lSize - 1][j] = true
                for (i in 0 until lSize) shape[i][0] = true
            }
        }
    }

    constructor(cols: Int, rows: Int, rotateSizeBy: Int, colorIndex: Int) {
        this.colorIndex = colorIndex

        rotation = rotateSizeBy % 2
        cellCols = if (rotation == 1) rows else cols
        cellRows = if (rotation == 1) cols else rows
        shape = Array(cellRows) { BooleanArray(cellCols) }
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                shape[i][j] = true
            }
        }
    }

    fun draw(batch: SpriteRender) {
        val color = BlockGame.theme.blockColors[colorIndex]
        for ((i, row) in shape.withIndex()) {
            for ((j, column) in row.withIndex())
                if (column) Block.draw(
                    batch,
                    color,
                    (x + j * cellSize) - space,
                    (y + i * cellSize) - space,
                    (cellSize - space).toInt()
                )
        }
    }

    fun filled(i: Int, j: Int): Boolean {
        return shape[i][j]
    }

    fun calculateArea(): Int {
        var area = 0
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                if (shape[i][j]) {
                    area++
                }
            }
        }
        return area
    }
    fun calculateGravityCenter(): Vector {
        var filledCount = 0
        val result = Vector()
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                if (shape[i][j]) {
                    filledCount++
                    result.add(
                        Vector(
                            x + j * cellSize - cellSize * 0.5f,
                            y + i * cellSize - cellSize * 0.5f
                        )
                    )
                }
            }
        }
        return result.scale(1f / filledCount)
    }

    fun write(out: DataOutputStream) {
        out.writeInt(colorIndex)
        out.writeInt(rotation)
    }

}