package com.jumbox.game.android.graphics

import com.jumbox.game.android.graphics.Color.Companion.toColor

/**
 * Created by Jumadi Janjaya date on 09/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
data class Shadow(var radius: Float = 0f, var dx: Float = 0f, var dy: Float = 0f, var color: Color = Color.CLEAR) {

    val isEmpty: Boolean
    get() = radius == 0f || color.isClear()

    fun parse(string: String) {
        val spilt = string.split(" ")
        if (spilt.size == 4) {
            radius = spilt[0].toFloat()
            dx = spilt[1].toFloat()
            dy = spilt[2].toFloat()
            color = spilt[3].toColor()
        } else if (spilt.size == 2) {
            radius = spilt[0].toFloat()
            color = spilt[1].toColor()
        }
    }
}