package com.jumbox.blockpuzzle.game.effect

import com.jumbox.blockpuzzle.game.block.Block
import com.jumbox.game.android.Engine
import com.jumbox.game.android.Vector
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.SpriteRender
import kotlin.math.pow


/**
 * Created by Jumadi Janjaya date on 20/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class DefaultEffect {
    private var block: Block? = null

    private var vanishColor: Color? = null
    private var vanishSize = 0
    private var vanishElapsed = 0f
    private var vanishLifetime = 0f

    private val minimumSize = 0.3f
    private val speed = 0.1f

    init {
        vanishElapsed = Float.POSITIVE_INFINITY
    }

    fun setInfo(deadCell: Block, culprit: Vector) : DefaultEffect {
        block = deadCell
        vanishSize = block!!.size
        vanishColor = block!!.colorCopy()
        vanishLifetime = 1f

        val center =
            Vector(block!!.x + block!!.size * 0.5f, block!!.y + 0.5f)
        val vanishDist: Float = culprit.copy().distanceSqr(Vector(center.x, center.y)
        ) / (block!!.size.toDouble().pow(4.0).toFloat() * 0.2f)

        vanishElapsed = vanishLifetime * 0.4f - vanishDist
        return this
    }

    fun draw(batch: SpriteRender) {
        vanishElapsed += speed * Engine.game.deltaTime
        if (block == null) return

        val progress = 1f.coerceAtMost(vanishElapsed.coerceAtLeast(0f) / vanishLifetime)

        vanishSize = Interpolation.linear.apply(vanishSize.toFloat(), Interpolation.elastic.out.apply(block!!.size.toFloat(), 0f, progress), 0.2f).toInt()
        val centerOffset = (block!!.size * 0.5f - vanishSize * 0.5f).toInt()

        Block.draw(batch, vanishColor!!, block!!.x + centerOffset, block!!.y + centerOffset, vanishSize)
    }

    fun isDone(): Boolean {
        return vanishSize < minimumSize
    }
}