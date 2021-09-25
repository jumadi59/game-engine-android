package com.jumbox.game.android.graphics

import com.jumbox.game.android.Rectangle

/**
 * Created by Jumadi Janjaya date on 20/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Sprite(var texture: BaseShape, x: Float, y: Float, width: Int, height: Int) : Rectangle(x, y, width, height) {

    constructor(texture: BaseShape, x: Float, y: Float, width: Float, height: Float) : this(texture, x, y, width.toInt(), height.toInt())
    constructor(texture: BaseShape) : this(texture, 0f, 0f, texture.width, texture.height)
    constructor(texture: BaseShape, width: Int, height: Int) : this(texture, 0f, 0f, width, height)
    constructor(texture: BaseShape, x: Float, y: Float) : this(texture, x, y, texture.width, texture.height)

    var color = Color.CLEAR
    var scale = 1f
        set(value) {
            field = value
            scaleX = value
            scaleY = value
        }
    var scaleX = 1f
    var scaleY = 1f
    var rotate = 0f

    fun draw(batch: SpriteRender) {
        batch.color = color
        val w = scaleX * width
        val h = scaleY * height
        val px = (width - w) * 0.5f
        val py = (height - h) * 0.5f
        batch.draw(texture, x + px, y + py, w, h)
    }

    override fun copy(): Sprite {
        return Sprite(texture, x, y, width, height)
    }

}