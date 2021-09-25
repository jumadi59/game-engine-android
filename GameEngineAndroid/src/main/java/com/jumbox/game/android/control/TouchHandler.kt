package com.jumbox.game.android.control

import com.jumbox.game.android.Engine

/**
 * Created by Jumadi Janjaya date on 24/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class TouchHandler {

    companion object {

        private var touchHandler: TouchHandler? = null

        @JvmStatic
        fun getInstance() : TouchHandler {
            if (touchHandler == null) touchHandler = TouchHandler()
            return touchHandler!!
        }
    }

    var touchController: InputTouchController? = null
    var x = 0
    var y = 0
    var dx = 0f
    var dy = 0f

    fun touch(x: Int, y: Int) {
        val oldX = this.x
        val oldY = this.y
        this.x = x
        this.y = Engine.game.height - y

        dx = this.x.toFloat() - oldX
        dy = this.y.toFloat() - oldY
    }

    fun down() : Boolean {
        return touchController?.touchDown(x, y)?: false
    }

    fun up() : Boolean {
        return touchController?.touchUp(x, y)?: false
    }

    fun moved() : Boolean {
        return touchController?.touchMoved(dx, dy)?: false
    }

    fun scroll(distanceX: Float, distanceY: Float) : Boolean {
        return touchController?.touchScroll(distanceX, distanceY)?:false
    }

    fun fling(velocityX: Float, velocityY: Float) : Boolean {
        return false
    }

    fun cancel() : Boolean {
        return false
    }
}