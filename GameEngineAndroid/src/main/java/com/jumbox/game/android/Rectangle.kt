package com.jumbox.game.android

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

open class Rectangle(x: Float, y: Float, open var width: Int, open var height: Int) : Vector(x, y) {

    val centerX: Float
    get() = x + (width / 2)
    val centerY: Float
    get() = y + (height / 2)
    val isEmpty: Boolean
    get() = (x == 0f && y == 0f && width == 0 && height == 0)

    constructor() :this(0f,0f, 0, 0)
    constructor(x: Float, y: Float, width: Float, height: Float) :this(x, y, width.toInt(), height.toInt())
    constructor(x: Int, y: Int, width: Int, height: Int) :this(x.toFloat(), y.toFloat(), width, height)

    fun set(x: Float, y: Float, width: Int, height: Int) : Rectangle {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        return this
    }

    open fun contains(vector: Vector): Boolean {
        return x < vector.x && vector.x < x + width && y < vector.y && vector.y < y + height
    }

    open fun setSize(width: Int, height: Int) : Rectangle{
        this.width = width
        this.height = height
        return this
    }

    fun set(rect: Rectangle) : Rectangle {
        set(rect.x, rect.y, rect.width, rect.height)
        return this
    }

    fun clear() {
        set(0f, 0f, 0, 0)
    }

    override fun copy() = Rectangle(x, y, width, height)

    override fun toString(): String {
        return "x = $x " +
                "y = $y " +
                "width = $width " +
                "height = $height"
    }
}