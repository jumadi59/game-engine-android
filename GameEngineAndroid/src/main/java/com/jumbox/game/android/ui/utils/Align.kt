package com.jumbox.game.android.ui.utils

import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector

/**
 * Created by Jumadi Janjaya date on 24/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Align(var x: Float, var y: Float) {

    companion object {

        val topLeft = Align(-1f, -1f)
        val topCenter = Align(0f, -1f)
        val topRight = Align(1f, -1f)

        val centerLeft = Align(-1f, 0f)
        val center = Align(0f, 0f,)
        val centerRight = Align(1f, 0f)

        val bottomLeft = Align(-1f, 1f)
        val bottomCenter = Align(0f, 1f)
        val bottomRight = Align(1f, 1f)

        fun Align.withinRect(rect: Rectangle, size: Size) : Rectangle {
            val r = rect.copy()
            val offsetWidth = (rect.width - size.width) / 2
            val offsetHeight = (rect.height - size.height) / 2
            r.add(Vector(offsetWidth + x * offsetWidth, offsetHeight + y * offsetHeight))
            return r
        }

        fun Align.withinOffset(rect: Rectangle, size: Size) : Vector {
            val offsetWidth = (rect.width - size.width) / 2
            val offsetHeight = (rect.height - size.height) / 2
            return Vector(offsetWidth + x * offsetWidth, offsetHeight + y * offsetHeight)
        }

        fun Align.withinVec(rect: Rectangle, size: Size) : Vector {
            return Vector(withinRect(rect, size))
        }

    }

}