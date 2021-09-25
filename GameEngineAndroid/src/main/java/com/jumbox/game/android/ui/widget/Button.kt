package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.*
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 25/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Button(style: Style, text: String, size: Size = Size.wrap) : Text(style, text, size) {

    companion object {
        val default = Background(idle = Shape().also {
            it.typeShape = Shape.SHAPE_ROUND
            it.round = floatArrayOf(5f, 5f, 5f, 5f)
            it.color = "#0072a1".toColor()
            it.shadow = Shadow(3f, 0f, 0f, "#3e3e3e".toColor())
        },
            pressed = Shape().also {
                it.typeShape = Shape.SHAPE_ROUND
                it.round = floatArrayOf(5f, 5f, 5f, 5f)
                it.color = "#0694cf".toColor()
                it.shadow = Shadow(5f, 2f, 2f, "#3e3e3e".toColor())
            })
    }

    override fun draw(render: Render) {
        if (isPressed) {
            scale.width = Interpolation.linear.apply(scale.width, 0.8f, 0.5f)
            scale.height = Interpolation.linear.apply(scale.height, 0.8f, 0.5f)
        } else {
            scale.width = Interpolation.linear.apply(scale.width, 1f, 0.5f)
            scale.height = Interpolation.linear.apply(scale.height, 1f, 0.5f)
        }
        super.draw(render)
    }

    constructor(font: Font, text: String) : this(Style(font = font, background = default), text)

}