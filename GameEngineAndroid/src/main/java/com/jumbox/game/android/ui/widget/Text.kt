package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Background
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.Font
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Align.Companion.withinVec
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 22/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Text(style: Style, var text: String, size: Size = Size.wrap) :
    Widget(null, size, style) {

    private val fontRect = Rectangle()
    val requestStyle: Style
        get() = style as Style

    constructor(mFont: Font, text: String) : this(
        style = Style().apply { font = mFont },
        text = text
    )

    override fun applyDraw(render: Render) {
        applyStyle()

        val fontVec = requestStyle.align.withinVec(rect, Size(fontRect.width, fontRect.height))

        val color = requestStyle.color.copy()
        color.a *= alpha
        render.color = color
        render.draw(requestStyle.font, fontVec.x, fontVec.y, fontRect.width, fontRect.height)
    }

    private fun applyStyle() {
        val textSize = scale.scale * requestStyle.fontSize
        requestStyle.font.text = text
        requestStyle.font.textSize = textSize
        requestStyle.font.setStyle(requestStyle.fontStyle)
        if (!requestStyle.outlineColor.isClear()) requestStyle.font.outlineColor =
            requestStyle.outlineColor
        if (requestStyle.outlineWidth > 0) requestStyle.font.outlineWidth =
            requestStyle.outlineWidth.toInt()
        val rect = requestStyle.font.getSizeLabel()
        fontRect.setSize(rect.width, rect.height)
    }

    override fun invalidate() {
        applyStyle()

        val wrapSize = Size((fontRect.width + (fontRect.width * 0.1f)).toInt(), (fontRect.height + (fontRect.height * 0.1f)).toInt())
        val fitSize = when {
            parent != null -> parent!!.sizeChild(this)
            layer != null -> Size(layer!!.screenWidth, layer!!.screenHeight)
            else -> Size(Engine.game.width, Engine.game.height)
        }


        val w: Int = when {
            size.isWrapWidth -> wrapSize.width
            size.isFitWidth -> fitSize.width
            else -> size.width
        }

        val h: Int = when {
            size.isWrapHeight -> wrapSize.height
            size.isFitHeight -> fitSize.height
            else -> size.height
        }

        setSize(w, h)
    }

    override fun setPosition(x: Int, y: Int) {
        super.setPosition(x, y)
        fontRect.set(this.x.toFloat(), this.y.toFloat())
    }

    open class Style(
        var font: Font = Font(),
        var fontSize: Float = 14f,
        var fontStyle: Int = NORMAL,
        var color: Color = Color.CLEAR,
        var outlineColor: Color = Color.CLEAR,
        var outlineWidth: Float = 0f,
        var align: Align = Align.topLeft,
        background: Background? = null,
        backgroundColor: Color = Color.CLEAR,
    ) : Widget.Style(background, backgroundColor) {

        override fun copy(background: Background?, backgroundColor: Color?): Style {
            return Style(
                font,
                fontSize,
                fontStyle,
                color,
                outlineColor,
                outlineWidth,
                align,
                background?:this.background,
                backgroundColor?:this.backgroundColor
            )
        }

        open fun copy(
            font: Font? = null,
            fontSize: Float? = null,
            fontStyle: Int? = null,
            color: Color? = null,
            outlineColor: Color? = null,
            outlineWidth: Float? = null,
            align: Align? = null,
            background: Background? = null,
            backgroundColor: Color? = null,
        ): Style {
            return Style(
                font ?: this.font,
                fontSize ?: this.fontSize,
                fontStyle ?: this.fontStyle,
                color ?: this.color,
                outlineColor ?: this.outlineColor,
                outlineWidth?:this.outlineWidth,
                align?:this.align,
                background?:this.background,
                backgroundColor?:this.backgroundColor
            )
        }

        companion object {
            const val NORMAL = 0
            const val BOLD = 1
            const val ITALIC = 2
        }
    }
}