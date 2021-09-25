package com.jumbox.game.android.screen

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
interface Scene {

    fun show()
    fun pause()
    fun update(delta: Float)
    fun render()
    fun dispose()
}