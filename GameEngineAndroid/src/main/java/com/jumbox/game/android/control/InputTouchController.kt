package com.jumbox.game.android.control

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
interface InputTouchController {

    fun touchDown(screenX: Int, screenY: Int) : Boolean
    fun touchUp(screenX: Int, screenY: Int) : Boolean
    fun touchMoved(dx: Float, dy: Float) : Boolean
    fun touchScroll(dx: Float, dy: Float) : Boolean
}