package com.jumbox.game.android.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Color.Companion.toColorInt
import com.jumbox.game.android.util.Extension.dpToPx

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class Render {

    var color = Color.CLEAR
    var rotate: Float = 0f
    set(value) {
        val old = field
        field = value
        if (value != old) isRotate = true
    }
    private var isRotate = false
    private val rect = Rectangle()
    private var isDrawing = false
    protected var canvas: Canvas? = null
    protected val rectShape = Shape().load("shape: rectangle\ncolor: #000000")

    protected open fun draw(baseShape: BaseShape) {
        if (!isDrawing) return

        canvas?.let {
            val rect = RectF(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height)
            if (color != Color.CLEAR) baseShape.color = color
            if (isRotate) {
                save()
                it.rotate(rotate, rect.centerX(), rect.centerY())
            }

            baseShape.setBounds(rect)
            baseShape.draw(it)
            if (isRotate) {
                restore()
                isRotate = false
            }
        }

        rotate = 0f
        color = Color.CLEAR
        rect.clear()
        rectShape.clear()
        baseShape.clear()
    }

    fun clip(x: Float, y: Float, width: Float, height: Float) {
        canvas?.clipRect(x, y, x + width, y + height)
    }

    open fun setRect(x: Float, y: Float, width: Int, height: Int) {
        rect.set(x, y, width, height)
    }

    fun restore(index: Int? = null) {
        if (!isDrawing) return

        if (index != null) canvas?.restoreToCount(index)
        else canvas?.restore()
    }

    fun save() : Int? {
        if (!isDrawing) return null

        return canvas?.save()
    }

    abstract fun draw(baseShape: BaseShape, x: Int, y: Int)
    abstract fun draw(baseShape: BaseShape, x: Int, y: Int, width: Int, height: Int)
    abstract fun draw(color: Color, x: Int, y: Int, width: Int, height: Int)
    abstract fun draw(baseShape: BaseShape, x: Float, y: Float)
    abstract fun draw(baseShape: BaseShape, x: Float, y: Float, width: Int, height: Int)
    abstract fun draw(baseShape: BaseShape, x: Float, y: Float, width: Float, height: Float)
    abstract fun draw(color: Color, x: Float, y: Float, width: Int, height: Int)
    abstract fun draw(color: Color, x: Float, y: Float, width: Float, height: Float)

    open fun begin() {
        canvas = Engine.game.getCanvas()
        isDrawing = true
    }

    open fun end() {
        isDrawing = false
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    open fun debugDraw(color: Color, x: Float, y: Float, width: Float, height: Float) {
        canvas?.let {
            paint.isAntiAlias = true
            paint.style = Paint.Style.STROKE
            paint.color = color.toColorInt()
            paint.strokeWidth = 2f.dpToPx()
            it.drawRect(x, y, x + width, y + height, paint)
        }
    }
}