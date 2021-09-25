package com.jumbox.game.android.ui.utils

import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector
import kotlin.math.roundToInt

/**
 * Created by Jumadi Janjaya date on 07/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Scale(var width: Float, var height: Float) {

    constructor(scale: Float) : this(scale, scale)

    val isScale: Boolean
    get() = width != 1f || height != 1f

    val scale: Float
    get() = if(width == height) width else width.coerceAtMost(height)

    companion object {

        fun Scale.withRect(rect: Rectangle) : Rectangle {
            val r = rect.copy()
            val width = width * rect.width
            val height = height * rect.height
            val offsetX = (rect.width - width) * 0.5f
            val offsetY = (rect.height - height) * 0.5f
            r.set(rect.x + offsetX, rect.y + offsetY, width.toInt(), height.toInt())
            return r
        }

        fun Type.withTexture(rect: Rectangle, size: Size) : Rectangle {
            var offsetX = (rect.width - size.width)
            var offsetY = (rect.height - size.height)
            val r = rect.copy()
            return when(this) {
                Type.center -> {
                    val sz = Size(rect.width, rect.height)
                    if (size.width > size.height) {
                        val aspectRatio = size.width.toFloat() / size.height.toFloat()
                        sz.height = (rect.width / aspectRatio).roundToInt()
                    } else if (size.height > size.width) {
                        val aspectRatio = size.height.toFloat() / size.width.toFloat()
                        sz.width = (rect.height / aspectRatio).roundToInt()
                    }

                    offsetX = (rect.width - sz.width)
                    offsetY = (rect.height - sz.height)

                    r.add(Vector(offsetX * 0.5f, offsetY * 0.5f))
                    r.setSize(sz.width, sz.height)
                }
                Type.fitCenter -> r
                Type.centerInside -> {
                    val maxSize = Size((rect.width - (rect.width * 0.3f)).toInt(), (rect.height - (rect.height * 0.3f)).toInt())
                    val sz = Size(maxSize.width, maxSize.height)
                    if (size.width > size.height) {
                        val aspectRatio = size.width.toFloat() / size.height.toFloat()
                        sz.height = (maxSize.width / aspectRatio).roundToInt()
                    } else if (size.height > size.width) {
                        val aspectRatio = size.height.toFloat() / size.width.toFloat()
                        sz.width = (maxSize.height / aspectRatio).roundToInt()
                    }

                    offsetX = (rect.width - sz.width)
                    offsetY = (rect.height - sz.height)

                    r.add(Vector(offsetX * 0.5f, offsetY * 0.5f))
                    r.setSize(sz.width, sz.height)
                }
                Type.centerCrop -> {
                    r.add(Vector(offsetX * 0.5f, offsetY * 0.5f))
                    r.setSize(size.width, size.height)
                }
                Type.fitLeft -> r.setSize(size.width, size.height)

                Type.fitRight -> {
                    r.add(Vector(-offsetX.toFloat(), 0f))
                    r.setSize(size.width, size.height)
                }
            }
        }
    }

    enum class Type {
        center,
        fitCenter,
        centerInside,
        centerCrop,
        fitLeft,
        fitRight
    }
}