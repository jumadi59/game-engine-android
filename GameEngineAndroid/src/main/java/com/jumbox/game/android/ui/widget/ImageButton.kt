package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.BaseShape
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 07/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class ImageButton(
    texture: BaseShape,
    size: Size,
    style: Style = default
)  : Image(texture, size, style) {

    constructor(texture: BaseShape, style: Style) : this(texture, Size(texture.width, texture.height), style)

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
}