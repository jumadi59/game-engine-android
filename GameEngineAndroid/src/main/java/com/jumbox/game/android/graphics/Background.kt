package com.jumbox.game.android.graphics

import com.jumbox.game.android.Rectangle


/**
 * Created by Jumadi Janjaya date on 25/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Background(
    var idle: BaseShape? = null,
    var pressed: BaseShape? = null,
    var selected: BaseShape? = null,
    var checked: BaseShape? = null
) : Rectangle(0f, 0f, 0, 0) {

    var isSelect = false
    var isPressed = false
    var isChecked = false

    constructor(baseShape: BaseShape) : this(idle = baseShape)

    fun draw(render: Render, parentAlpha: Float) {
        when {
            isPressed -> pressed?.let {
                it.color.a *= parentAlpha
                render.draw(it, x, y, width, height)
            }
            isSelect -> selected?.let {
                it.color.a *= parentAlpha
                render.draw(it, x, y, width, height)
            }
            isChecked -> checked?.let {
                it.color.a *= parentAlpha
                render.draw(it, x, y, width, height)
            }
            else -> idle?.let {
                it.color.a *= parentAlpha
                render.draw(it, x, y, width, height)
            }
        }
    }


}