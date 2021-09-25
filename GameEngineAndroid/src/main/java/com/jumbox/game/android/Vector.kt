package com.jumbox.game.android

import com.jumbox.game.android.animation.Interpolation
import kotlin.math.*

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Vector(open var x: Float, open var y: Float) {

    constructor() : this(0f, 0f)
    constructor(vector: Vector) : this(vector.x, vector.y)

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(vector: Vector) {
        set(vector.x, vector.y)
    }

    fun lerp(vector: Vector, speed: Float) : Vector {
        set(Interpolation.linear.apply(x, vector.x, speed), Interpolation.linear.apply(y, vector.y, speed))
        return this
    }

    fun add(vector: Vector) : Vector {
        set(x + vector.x, y + vector.y)
        return this
    }

    fun subtract(vector: Vector) : Vector {
        set(x - vector.x, y - vector.y)
        return this
    }

    fun scale(scalar: Float) : Vector {
        set(x * scalar, y * scalar)
        return this
    }

    fun dot(vector: Vector) : Vector {
        set(x * vector.x, y * vector.y)
        return this
    }

    fun moveToward(vector: Vector, t: Float) : Vector {
        val tt = t.coerceAtMost(1f)
        return add(vector.subtract(this).scale(tt))
    }

    fun magnitudeSqr() : Float {
        return x * x + y * y
    }

    fun magnitude() : Float {
        return sqrt(magnitudeSqr().toDouble()).toFloat()
    }

    fun distanceSqr(vector: Vector) : Float {
        val deltaX= x - vector.x
        val deltaY = y - vector.y
        return deltaX * deltaX + deltaY * deltaY
    }

    fun distance(vector: Vector) : Float {
        return sqrt(distanceSqr(vector).toDouble()).toFloat()
    }

    fun normalize() : Vector {
        val mag = magnitude()
        if(abs(mag) < 1e-9) {
            x = 0f
            y = 0f
        } else {
            x /= mag
            y /= mag
        }
        return this
    }

    fun rotate(alpha: Float) : Vector {
        val cos = cos(alpha.toDouble())
        val sin = sin(alpha.toDouble())
        x = (x * cos - y * sin).toFloat()
        y = (x * sin + y * cos).toFloat()
        return this
    }

    fun angle() : Float {
        return atan2(y.toDouble(), x.toDouble()).toFloat()
    }

    open fun  copy() = Vector(x, y)

    override fun toString(): String {
        return "x = $x " +
                "y = $y"
    }

    fun isSame(other: Vector): Boolean {
        return x == other.x && y == other.y
    }
}