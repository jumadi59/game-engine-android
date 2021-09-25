package com.jumbox.blockpuzzle.game.scene

import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.game.block.Cube
import com.jumbox.blockpuzzle.game.layer.LapBoardLayer
import com.jumbox.blockpuzzle.game.layer.MenuLayer
import com.jumbox.blockpuzzle.serializer.BinSerializable
import com.jumbox.blockpuzzle.serializer.BinSerializer
import com.jumbox.game.android.Engine
import com.jumbox.game.android.Game
import com.jumbox.game.android.Vector
import com.jumbox.game.android.graphics.Camera
import com.jumbox.game.android.graphics.SpriteRender
import com.jumbox.game.android.screen.Scene
import com.jumbox.game.android.util.Files
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Jumadi Janjaya date on 01/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class MenuScene(val game: Game) : Scene, BinSerializable {

    private val spriteRender = SpriteRender()
    private val camera = Camera()
    private val bottomCubes = ArrayList<Cube>()
    private val topCubes = ArrayList<Cube>()

    private val menuLayer = MenuLayer(this)
    val lapBoardLayer = LapBoardLayer(this)

    private val screenWidth = Engine.game.width
    private val screenHeight = Engine.game.height

    private val row = 3
    private val col = 8

    init {
        menuLayer.create()
        lapBoardLayer.create()

        var startY = screenHeight * 0.1f
        var startX = -50f
        var pos = Vector(startX, startY)
        for (i in 0 until row) {
            for (j in 0 until col) {
                bottomCubes.add(Cube(Random().nextInt(4), pos.x, pos.y, 100f, 116f))
                pos.x += 50f
                pos.y -= 32f + Random().nextInt(10)
            }
            pos.y = startY - (32f * (i+1))
            pos.x = startX +(-50f * (i+1))
        }

        startY = screenHeight + ((startY - pos.y) / 2)
        startX = (screenWidth / 2) + 50f
        pos = Vector(startX, startY)
        for (i in 0 until row) {
            for (j in 0 until col) {
                topCubes.add(Cube(Random().nextInt(4), pos.x, pos.y, 100f, 116f))
                pos.x += 50f
                pos.y -= 32f + Random().nextInt(10)
            }
            pos.y = startY - (32f * (i+1))
            pos.x = startX +(-50f * (i+1))
        }

        if (!load()) {
            for (i in 0 until 10) {
                val rand = Random().nextInt(col * row)
                bottomCubes[rand].apply {
                    maxPosY = Random().nextInt(70).toFloat()
                    isAnimate = true
                }
            }

            for (i in 0 until 10) {
                val rand = Random().nextInt(col * row)
                topCubes[rand].apply {
                    maxPosY = Random().nextInt(70).toFloat()
                    isAnimate = true
                }
            }
            save()
        }
    }

    fun showLapBoard() {
        menuLayer.pause()
        lapBoardLayer.show()
    }

    fun showMenu() {
        lapBoardLayer.pause()
        menuLayer.show()
    }

    override fun show() {
        camera.setOrtho(true, screenWidth, screenHeight)
        if (lapBoardLayer.isShow) lapBoardLayer.show()
        else menuLayer.show()
    }

    override fun pause() {
        if (menuLayer.isShow) menuLayer.pause()
        else if (lapBoardLayer.isShow) lapBoardLayer.pause()
    }

    override fun update(delta: Float) {
        camera.update()
        topCubes.forEach {
            it.update(delta)
        }
        bottomCubes.forEach {
            it.update(delta)
        }

        menuLayer.update()
        lapBoardLayer.update()
    }

    override fun render() {
        Engine.game.backgroundColor = BlockGame.theme.backgroundColor
        spriteRender.projectionMatrix = camera

        spriteRender.begin()
        if (!lapBoardLayer.isShow) {
            topCubes.forEach {
                it.draw(spriteRender)
            }
            bottomCubes.forEach {
                it.draw(spriteRender)
            }
        }

        spriteRender.end()

        menuLayer.draw()
        lapBoardLayer.draw()
    }

    override fun dispose() {
        menuLayer.dispose()
        lapBoardLayer.dispose()
    }

    private fun save() {
        val handle = Files.local(".UIMenu.dt")
        try {
            BinSerializer.serialize(this, handle.write(false))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun delete() {
        val handle = Files.local(".UIMenu.dt")
        if (handle.exists()) handle.delete()
    }

    private fun load() : Boolean {
        val handle = Files.local(".UIMenu.dt")
        return if (handle.exists()) {
            try {
                BinSerializer.deserialize(this, handle.reed())
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        } else false
    }

    override fun write(out: DataOutputStream) {
        out.writeInt(row)
        out.writeInt(col)
        topCubes.forEach {
            it.write(out)
        }
        bottomCubes.forEach {
            it.write(out)
        }
    }

    override fun read(`in`: DataInputStream) {
        val row = `in`.readInt()
        val col = `in`.readInt()
        if (row != this.row || col != this.col)
            throw IOException("Invalid row or column saved.")

        topCubes.forEach {
            it.read(`in`)
        }
        bottomCubes.forEach {
            it.read(`in`)
        }
    }

}