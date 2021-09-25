package com.jumbox.game.android.graphics

import android.graphics.*
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.graphics.Color.Companion.toColorInt
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.Extension.dpToPx
import com.jumbox.game.android.util.Extension.toColorArray
import com.jumbox.game.android.util.Extension.toFloatArray
import com.jumbox.game.android.util.FileHandle


/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Shape : BaseShape() {

    companion object {

        const val SHAPE_RECTANGLE = "rectangle"
        const val SHAPE_OVAL = "oval"
        const val SHAPE_ARCH = "arch"
        const val SHAPE_ROUND = "round"

        const val MATCH = -2

    }

    private val keyShape = "shape"
    private val keyWidth = "width"
    private val keyHeight = "height"
    private val keyRound = "round"
    private val keyColor = "color"
    private val keyPadding = "padding"
    private val keyColors = "colors"
    private val keyRotate = "rotate"
    private val keyShadow = "shadow"

    private val keyStroke = "stroke"
    private val keyColorStroke = "colorStroke"

    private var strings = ArrayList<String>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val bounds = RectF()
    private val rect = RectF()
    private val radiusArr = FloatArray(8)
    private val path = Path()
    private val dashPath = Path()
    var shadow = Shadow()

    var id = ""

    var typeShape = "rectangle"
    var widthStroke = 0f
    var colorStroke = Color()

    private fun parse() {
        for (it in strings) {
            val split = it.split(":")
            if (split.size != 2) continue

            val key = split[0]
            val value = split[1].replace(" ", "")
            when(key) {
                "id" -> id = value
                keyShape -> typeShape = value
                keyWidth -> width = if (value== "match") MATCH else value.toInt()
                keyHeight -> height = if (value == "match") MATCH else value.toInt()
                keyColor -> color = value.toColor()
                keyPadding -> padding = value.toFloatArray().run {
                    if (this.isEmpty()) floatArrayOf(0f, 0f, 0f, 0f) else this
                }
                keyRound -> round = value.toFloatArray().run {
                    if (this.isEmpty()) floatArrayOf(0f, 0f, 0f, 0f) else this
                }
                keyColors -> colors = value.toColorArray()
                keyRotate -> rotate = value.toFloat()

                keyStroke -> widthStroke = value.toFloat().dpToPx()
                keyColorStroke -> colorStroke = value.toColor()
                keyShadow -> shadow.parse(if (split[1].substring(0, 1) == " ") split[1].substring(1) else split[1])
            }
        }
    }

    override fun setBounds(rect: RectF) {
        bounds.set(rect)
        val percentageW: Float = if (width != MATCH)
            bounds.width() / width * 1f
        else 1f
        val percentageH = if (height != MATCH)
            bounds.height() / height * 1f
        else 1f

        val percentage = percentageW.coerceAtMost(percentageH)

        for(i in round.indices) {
            radiusArr[i * 2] = (round[i] * percentage).dpToPx()
            radiusArr[(i * 2) + 1] = (round[i] * percentage).dpToPx()
        }

        if (width == MATCH) width = rect.width().toInt()
        if (height == MATCH) height = rect.height().toInt()
    }

    override fun setSize(width: Int, height: Int) {
        bounds.set(0.0f, 0.0f, width.toFloat(), height.toFloat())
        this.width = width
        this.height = height

        for(i in round.indices) {
            radiusArr[i * 2] = round[i].dpToPx()
            radiusArr[(i * 2) + 1] = round[i].dpToPx()
        }
    }

    override fun setPosition(x: Int, y: Int) {
        rect.set(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    }

    override fun draw(canvas: Canvas) {
        if (width <= 0 || height <= 0) return
        path.reset()
        dashPath.reset()

        paint.isAntiAlias = true

        rect.set(
            (bounds.left - padding[0].dpToPx() + widthStroke + shadow.radius + shadow.dx),
            (bounds.top - padding[1].dpToPx() + widthStroke + shadow.radius + shadow.dy),
            (bounds.right + padding[2].dpToPx() - widthStroke - shadow.radius - shadow.dx),
            bounds.bottom + padding[3].dpToPx() - widthStroke - shadow.radius - shadow.dy
        )

        if (widthStroke > 0f) {
            paint.style = Paint.Style.STROKE
            color = colorStroke
            paint.strokeWidth = widthStroke
            if (!dashPath.isEmpty) canvas.drawPath(dashPath, paint)
        } else
            paint.style = Paint.Style.FILL

        if (colors !=null) paint.shader = LinearGradient(0f, 0f, rect.width(), 0f, colors!![0], colors!![1], Shader.TileMode.CLAMP)
        else paint.color = color.toColorInt()

        if (!shadow.isEmpty) paint.setShadowLayer(shadow.radius.dpToPx(), shadow.dx.dpToPx(), shadow.dy.dpToPx(), shadow.color.toColorInt())

        var saveCount = 0
        if (rotate != 0f) {
            saveCount = canvas.save()
            canvas.rotate(rotate, rect.centerX(), rect.centerY())
        }

        when(typeShape) {
            SHAPE_RECTANGLE -> drawRectangle(canvas)
            SHAPE_ROUND -> drawRound(canvas)
            SHAPE_OVAL -> drawOval(canvas)
            SHAPE_ARCH -> {}
        }

        if (rotate != 0f)
            canvas.restoreToCount(saveCount)
    }

    override fun clear() {
        parse()
    }

    override fun load(fileHandle: FileHandle): BaseFile {
        try {
            val content = String(fileHandle.reed().readBytes())
            load(content)
        } catch (ex: Exception) {
            throw IllegalArgumentException("Error reading file: $fileHandle", ex)
        }
        return this
    }

    fun load(content: String): Shape {
        strings = ArrayList(content.split("\n"))
        parse()
        return this

    }

    private fun drawRectangle(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

    private fun drawOval(canvas: Canvas) {
        canvas.drawOval(rect, paint)
    }

    private fun drawRound(canvas: Canvas) {
        path.addRoundRect(bounds, radiusArr, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    override fun toString(): String {
        return strings.toString()
    }

}