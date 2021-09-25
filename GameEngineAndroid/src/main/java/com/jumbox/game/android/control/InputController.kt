package com.jumbox.game.android.control

/**
 * Created by Jumadi Janjaya date on 06/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class InputController : InputTouchController {
    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        return false
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return false
    }

}