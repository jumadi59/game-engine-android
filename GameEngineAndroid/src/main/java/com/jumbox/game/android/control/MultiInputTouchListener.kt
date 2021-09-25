package com.jumbox.game.android.control

/**
 * Created by Jumadi Janjaya date on 25/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class MultiInputTouchListener : InputTouchController {
    private val touchListeners = ArrayList<InputTouchController>()

    fun addTouchListener(inputTouchController: InputTouchController) {
        if (touchListeners.find { it == inputTouchController } == null)
            touchListeners.add(inputTouchController)
    }

    fun removeTouchListener(inputTouchController: InputTouchController) {
        touchListeners.remove(inputTouchController)
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        for (target in touchListeners) {
            val isTouch = target.touchDown(screenX, screenY)
            if (isTouch) return true
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        for (target in touchListeners) {
            val isTouch = target.touchUp(screenX, screenY)
            if (isTouch) return true
        }
        return false
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        for (target in touchListeners) {
            val isTouch = target.touchMoved(dx, dy)
            if (isTouch) return true
        }
        return false
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        for (target in touchListeners) {
            val isTouch = target.touchScroll(dx, dy)
            if (isTouch) return true
        }
        return false
    }

}