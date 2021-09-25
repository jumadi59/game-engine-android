package com.jumbox.game.android.animation

import com.jumbox.game.android.graphics.Color


/**
 * Created by Jumadi Janjaya date on 10/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Animation {

    var duration = 200f
    var loop = 1

    private var animated = false
    private var animationStart = 0L
    private var currentLoop = 1

    val isAnimated: Boolean
    get() = animated

    fun start() {
        animated = true
        animationStart = System.currentTimeMillis()
    }

    fun end() {
        animated = false
    }

    fun update() {
        if (!isAnimated) return

        val elapsed = (System.currentTimeMillis() - animationStart).toFloat()
        val delta = elapsed / duration
        val time = elapsed / 1000f

        if (elapsed >= duration) {
            if (loop > 1 && currentLoop < loop) {
                currentLoop++
                start()
            } else end()
        }
    }
}