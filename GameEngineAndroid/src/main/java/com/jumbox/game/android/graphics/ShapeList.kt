package com.jumbox.game.android.graphics

import android.graphics.Canvas
import android.graphics.RectF
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.Extension.dpToPx
import com.jumbox.game.android.util.FileHandle
import java.util.*

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class ShapeList : BaseShape() {

    private var layerList = ArrayList<String>()
    private val bounds = RectF()
    private var shapes = ArrayList<Shape>()

    override var color = Color()
        get() = super.color
        set(value) {
        if (color != value) shapes.forEach {
                it.color = value
            }
        field = value
    }

    override var rotate: Float = 0f
        get() = super.rotate
        set(value) {
            if (rotate != value) shapes.forEach {
                it.rotate = value
            }
            field = value

        }
    override var round: FloatArray = floatArrayOf(0f, 0f, 0f, 0f)
        get() = super.round
        set(value) {
            if (!round.contentEquals(value)) shapes.forEach {
                it.round = value
            }
            field = value
        }

    override var colors: IntArray? = null
        get() = super.colors
        set(value) {
            if (!colors.contentEquals(value)) shapes.forEach {
                it.colors = value
            }
            field = value
        }

    fun findId(id: String) : BaseShape? {
        return shapes.find { it.id == id }
    }

    private fun parse() {
        shapes.clear()
        layerList.forEach {
            shapes.add(Shape().load(it))
        }
    }

    override fun setBounds(rect: RectF) {
        bounds.set(rect)

        if (width == Shape.MATCH) width = rect.width().toInt()
        if (height == Shape.MATCH) height = rect.height().toInt()
    }

    override fun setSize(width: Int, height: Int) {
        bounds.set(0F, 0F, width.toFloat(), height.toFloat())
    }

    override fun setPosition(x: Int, y: Int) {

    }

    override fun draw(canvas: Canvas) {
        for ((i, it) in shapes.withIndex()) {
            if (i > 0) {
                val s = shapes[i-1]
                bounds.set(
                    bounds.left+s.padding[0].dpToPx().toInt(),
                    bounds.top+s.padding[1].dpToPx().toInt(),
                    bounds.right-s.padding[2].dpToPx().toInt(),
                    bounds.bottom-s.padding[3].dpToPx().toInt()
                )
            }
            it.setBounds(bounds)
            it.draw(canvas)
        }
    }

    override fun clear() {
        parse()
    }

    override fun load(fileHandle: FileHandle): BaseFile {
        try {
            val content = String(fileHandle.reed().readBytes())
            val split = content.split("\n\n")
            layerList.addAll(split)
            parse()
        } catch (ex: Exception) {
            throw IllegalArgumentException("Error reading file: $fileHandle", ex)
        }
        return this
    }

}