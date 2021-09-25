package com.jumbox.game.android.graphics

import android.graphics.*
import com.jumbox.game.android.graphics.Color.Companion.toColorInt
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.FileHandle

/**
 * Created by Jumadi Janjaya date on 19/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Texture(private var bitmap: Bitmap? = null) : BaseShape() {

    companion object {
        const val SCREEN = 1
        const val ADD = 2
        const val MULTIPLY = 3
        const val OVERLAY = 4
        const val CLEAR = 5

        private fun porterDuffMode(type: Int): PorterDuff.Mode {
            return when(type) {
                SCREEN -> PorterDuff.Mode.SCREEN
                ADD -> PorterDuff.Mode.ADD
                MULTIPLY -> PorterDuff.Mode.MULTIPLY
                OVERLAY -> PorterDuff.Mode.OVERLAY
                CLEAR -> PorterDuff.Mode.CLEAR
                else -> throw IllegalArgumentException("Cannot type filter color")
            }
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val rect = RectF()
    private val src = RectF()
    private var colorFilter: PorterDuff.Mode? = null

    var colorFilterMode: Int? = 0
    set(value) {
        field = value
        if (value in 1..5) {
            colorFilter = porterDuffMode(value!!)
        }
    }

    override fun setBounds(rect: RectF) {
        this.rect.set(rect)
        setSize(rect.width().toInt(), rect.height().toInt())
    }

    override fun setSize(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        this.width = width
        this.height = height
    }

    override fun setPosition(x: Int, y: Int) {
        rect.set(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    }

    override fun draw(canvas: Canvas) {
        if (width <= 0 || height <= 0) return

        bitmap?.let {

            if (rotate != 0f) {
                canvas.save()
                canvas.rotate(rotate, rect.centerX(), rect.centerY())
            }


            colorFilter?.let { it2 -> paint.colorFilter = PorterDuffColorFilter(color.toColorInt(), it2) }
            canvas.drawBitmap(it, null, rect, paint)

            if (rotate != 0f)
                canvas.restore()
        }
    }

    override fun clear() {
        rect.set(src)
        this.width = src.width().toInt()
        this.height = src.height().toInt()
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        height = bitmap.height
        width = bitmap.width
        src.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    override fun load(fileHandle: FileHandle): BaseFile {
        val bm = BitmapFactory.decodeStream(fileHandle.reed())
        setBitmap(bm)
        return this
    }

}