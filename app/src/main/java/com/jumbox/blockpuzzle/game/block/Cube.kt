package com.jumbox.blockpuzzle.game.block

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.serializer.BinSerializable
import com.jumbox.game.android.graphics.Sprite
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Created by Jumadi Janjaya date on 02/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Cube(index: Int, x: Float, y: Float, width: Float, height: Float) :
    Sprite(AssetManager.manager.cubeTextures[index], x, y, width, height), BinSerializable {

    private var originPosY = y
    private var isTop = true
    private var textureIndex = index
    set(value) {
        if (field !=value) {
            field = value
            texture = AssetManager.manager.cubeTextures[value]
        }
    }

    var maxPosY = 0f
    var isAnimate = false

    fun update(delta: Float) {
        if (!isAnimate) return

        val translate = (maxPosY / 100) * delta
        if (y > originPosY + maxPosY) isTop = false
         else if (y < originPosY) isTop = true

        if (isTop)
            y -= translate
        else
            y += translate
    }

    override fun write(out: DataOutputStream) {
        out.writeFloat(originPosY)
        out.writeFloat(maxPosY)
        out.writeBoolean(isAnimate)
        out.writeInt(textureIndex)
    }

    override fun read(`in`: DataInputStream) {
        originPosY = `in`.readFloat()
        maxPosY = `in`.readFloat()
        isAnimate = `in`.readBoolean()
        textureIndex = `in`.readInt()
    }
}