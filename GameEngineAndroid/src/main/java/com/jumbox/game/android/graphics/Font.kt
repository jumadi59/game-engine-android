package com.jumbox.game.android.graphics

import android.graphics.*
import android.text.TextPaint
import com.jumbox.game.android.util.Extension.density
import com.jumbox.game.android.util.Extension.dpToPx
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Color.Companion.toColorInt
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.FileHandle

/**
 * Created by Jumadi Janjaya date on 21/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Font : BaseShape() {

    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG and Paint.SUBPIXEL_TEXT_FLAG)
    private val bounds = RectF()
    var textSize = 10f
    var outlineColor = Color.CLEAR
    var outlineWidth = 0
    var text = ""

    override fun setBounds(rect: RectF) {
        val textBounds = getSizeLabel()
        bounds.set(
            rect.left, rect.bottom,
                textBounds.width.toFloat(),
                textBounds.height.toFloat()
        )}

    fun getSizeLabel() : Rectangle {
        val textBounds = Rect()
        applyPaint(Paint.Style.FILL, color)
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        return Rectangle(0f, 0f, textBounds.width().toFloat(), textBounds.height().toFloat())
    }

    override fun setSize(width: Int, height: Int) {}
    override fun setPosition(x: Int, y: Int) {
        bounds.set(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    }

    fun setStyle(style: Int) {
        if (style > 0) {
            val typeface = textPaint.typeface
            val typefaceStyle = typeface?.style ?: 0
            val need = style and typefaceStyle.inv()
            textPaint.isFakeBoldText = need and Typeface.BOLD != 0
            textPaint.textSkewX = if (need and Typeface.ITALIC != 0) -0.25f else 0f
        } else {
            textPaint.isFakeBoldText = false
            textPaint.textSkewX = 0f
        }
    }

    private fun applyPaint(style: Paint.Style, color: Color) {
        textPaint.density = density()
        textPaint.isAntiAlias = true
        textPaint.isSubpixelText = true
        textPaint.textSize = textSize.dpToPx()
        textPaint.color = color.toColorInt()

        if (outlineWidth > 0) textPaint.strokeWidth = (2 * outlineWidth + 1).toFloat()
        textPaint.style = style
    }

    override fun draw(canvas: Canvas) {
        if (outlineWidth > 0) {
            applyPaint(Paint.Style.STROKE, outlineColor)
            canvas.drawText(text, bounds.left, bounds.top, textPaint)
        }
        applyPaint(Paint.Style.FILL, color)
        canvas.drawText(text, bounds.left, bounds.top, textPaint)
    }

    override fun clear() {}

    override fun load(fileHandle: FileHandle): BaseFile {
        if (fileHandle.type == FileHandle.INTERNAL)
            textPaint.typeface = Typeface.createFromAsset(fileHandle.asset(), fileHandle.file.path)
        else
            textPaint.typeface = Typeface.createFromFile(fileHandle.file)
        return this
    }

}