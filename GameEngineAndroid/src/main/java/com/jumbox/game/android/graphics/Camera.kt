package com.jumbox.game.android.graphics

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector

/**
 * Created by Jumadi Janjaya date on 03/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Camera(var viewPortWidth: Int, var viewPortHeight: Int) {

    var zoom = 1f
    var viewPortCenterX = (viewPortWidth / 2).toFloat()
    var viewPortCenterY = (viewPortHeight / 2).toFloat()

    private var wordWidth = 0
    private var wordHeight = 0
    private var isOffset = false

    var position = Vector()
    private var speed = 0.8f
    private var moved = Vector()

    constructor() : this(Engine.game.width, Engine.game.height)

    init {
        update(viewPortCenterX, viewPortCenterY)
    }

    fun setOrtho(isOffset: Boolean, wordWidth: Int, wordHeight: Int) {
        this.isOffset = isOffset
        this.wordWidth = wordWidth
        this.wordHeight = wordHeight
    }

    fun update(x: Float, y: Float) {
        if (isOffset) position.set(positionOffset(Vector(x, y)))
        else position.set(x, y)

        moved.set(position)
    }

    fun update(vector: Vector) {
        update(vector.x, vector.y)
    }

    fun translate(dx: Float, dy: Float) {
        update(position.x - dx, position.y - dy)
    }

    fun moved(vector: Vector, speed: Float) {
        moved.set(vector)
        this.speed = speed
    }

    fun worldToScreen(
        objectX: Float,
        objectY: Float,
    ): Vector {
        val x = viewPortCenterX - (position.x - objectX)
        val y = viewPortCenterY - ((Engine.game.height - position.y) - objectY)
        return Vector(x, y)
    }

    fun update() {
        if (!moved.isSame(position)) {
            if (isOffset)
                position.lerp(positionOffset(moved), speed)
            else
                position.lerp(moved, speed)
        }
    }

    fun rectCamera() : Rectangle {
        return Rectangle(
            position.x - viewPortCenterX,
            position.y - viewPortCenterY,
            viewPortWidth,
            viewPortHeight
        )
    }

    fun rectWOrd() : Rectangle {
        return Rectangle(0f, 0f, wordWidth, wordHeight)
    }

    private fun positionOffset(to: Vector) : Vector {
        val vec = Vector()
        vec.x = when {
            to.x + viewPortCenterX > wordWidth -> wordWidth - viewPortCenterX
            to.x - viewPortCenterX < 0 -> viewPortCenterX
            else -> to.x
        }
        vec.y = when {
            to.y + viewPortCenterY > wordHeight -> wordHeight - viewPortCenterY
            to.y - viewPortCenterY < 0 -> viewPortCenterY
            else -> to.y
        }
        return vec
    }

    fun unProject(vector: Vector) : Vector {
        val rect = rectCamera()
        val x = rect.x + vector.x
        val y = rect.y + vector.y
        return Vector(x, y)
    }
}