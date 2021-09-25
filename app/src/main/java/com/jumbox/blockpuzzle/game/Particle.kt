package com.jumbox.blockpuzzle.game

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Vector
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.widget.Text

/**
 * Created by Jumadi Janjaya date on 31/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Particle(pos: Vector, score: Int, style: Text.Style) {
    private var text = Text(style, "+$score").apply {
        setPosition(pos.x.toInt(), pos.y.toInt())
    }
    private var lifetime = 0f

    private val velocity = 0.1f

    fun run(render: Render) {
        lifetime += velocity * Engine.game.deltaTime
        if (lifetime > 1f) lifetime = 1f

        text.scale = Scale(Interpolation.elastic.out.apply(0f, 1f, lifetime))
        val opacity = Interpolation.linear.apply(1f, 0f, lifetime)
        text.requestStyle.color.a = opacity
        text.draw(render)
    }

    fun done(): Boolean {
        return lifetime >= 1f
    }
}