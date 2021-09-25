package com.jumbox.blockpuzzle.game.block

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.serializer.BinSerializable
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.BaseShape
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.SpriteRender
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Block(x: Float, y: Float, var size: Int) : Rectangle(x, y, size, size), BinSerializable {

    companion object {

        fun draw(batch: SpriteRender, shape: BaseShape, x: Float, y: Float, size: Int) {
            batch.draw(shape, x, y, size, size)
        }

        fun draw(batch: SpriteRender, color: Color, x: Float, y: Float, size: Int) {
            batch.color = color
            batch.draw(AssetManager.manager.shapeBlock, x, y, size, size)
        }

    }

    var colorIndex = -1
    val isEmptyColor
    get() = colorIndex < 0

    fun draw(batch: SpriteRender) {
        val color = if (colorIndex >= 0) BlockGame.theme.blockColors[colorIndex] else BlockGame.theme.blockColorEmpty
        draw(batch, color, x, y, size)
    }

    fun colorCopy() = if (colorIndex >= 0)  BlockGame.theme.blockColors[colorIndex] else BlockGame.theme.blockColorEmpty

    override fun write(out: DataOutputStream) {
        out.writeInt(colorIndex)
    }

    override fun read(`in`: DataInputStream) {
        colorIndex = `in`.readInt()
    }

}