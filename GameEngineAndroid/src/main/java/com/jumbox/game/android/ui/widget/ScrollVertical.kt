package com.jumbox.game.android.ui.widget

import android.util.Log
import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.Vector
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.*
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.util.Extension.dpToPxInt

/**
 * Created by Jumadi Janjaya date on 11/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class ScrollVertical(
    key: String? = null,
    size: Size = Size.fit,
    space: Int = 0,
    childes: Array<Widget>? = null,
    style: Style = default) : Row(key, size, space, childes, style) {

    companion object {
        val default = Style(
            thumb = Background(
                idle = Shape().load(
                    "shape: rectangle\n" +
                            "width: 45\n" +
                            "height: 120\n" +
                            "color: #dadada30"
                ), pressed = Shape().load(
                    "shape: rectangle\n" +
                            "width: 45\n" +
                            "height: 120\n" +
                            "color: #b8d6ea"
                )
            ), track = Background(
                idle = Shape().load(
                    "shape: oval\n" +
                            "width: 45\n" +
                            "height: 45\n" +
                            "color: #b4b4b4\n" +
                            "shadow: 3 #3e3e3e30"
                ), pressed = Shape().load(
                    "shape: oval\n" +
                            "width: 45\n" +
                            "height: 45\n" +
                            "color: #29a3f6\n" +
                            "shadow: 3 #3e3e3e30"
                )
            )
        )
    }

    val requestStyle: Style
        get() = style as Style
    private var filingScrolling = 0f
    private var maxScrolling = 0f
    private var trackRect = Rectangle(0f, 0f, 20f.dpToPxInt(), 20f.dpToPxInt())
    private var thumbRect = Rectangle(0f, 0f, 20f.dpToPxInt(), 20)
    private var isPressedThumb= false
    private var isPressedTrack = false
        set(value) {
            field = value
            requestStyle.track.isPressed = value
        }

    override fun applyDraw(render: Render) {
        if (filingScrolling > 0 && !isPressed) filingScrolling = Interpolation.linear.apply(filingScrolling, 0f, 0.3f)
        else if (filingScrolling < maxScrolling && !isPressed) filingScrolling = Interpolation.linear.apply(filingScrolling, maxScrolling, 0.3f)

        val cx = this.x
        var cy = (this.y + filingScrolling).toInt()
        for (widget in childes) {
            if (widget.isVisible) {
                widget.invalidate()
                widget.setPosition(cx + space, (cy + space))
                widget.draw(render)
                cy += widget.rect.height + space
            }
        }

        thumbRect.set((x + width - thumbRect.width).toFloat(), y.toFloat(), thumbRect.width, rect.height)
        requestStyle.thumb.set(thumbRect)
        requestStyle.thumb.draw(render, alpha)

        trackRect.setSize(trackRect.width,
            trackRect.height.toFloat().coerceAtLeast(maxScrolling * height / height).toInt()
        )

        val scroll = if (filingScrolling > 0f) 0f else if (filingScrolling < maxScrolling) maxScrolling else filingScrolling
        val percentage = (+scroll * 100) / +maxScrolling
        val tractY = ((height - trackRect.height) * percentage) / 100
        trackRect.set((x + width - trackRect.width).toFloat(), y + tractY)
        requestStyle.track.set(trackRect)
        requestStyle.track.draw(render, alpha)
    }

    override fun invalidate() {
        super.invalidate()
        var heightChild = 0
        childes.forEach {
            heightChild += it.rect.height + space
        }
        heightChild += space * 2
        maxScrolling = -(heightChild.toFloat() - height)
    }

    private fun scrollTo(screenY: Int) : Boolean {
        val percentage = ((screenY - thumbRect.y) * 100) / height
        filingScrolling = (+maxScrolling * percentage) / 100
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        val input = Vector(screenX.toFloat(), screenY.toFloat())
        isPressedThumb = false
        isPressedTrack = false
        return when {
            thumbRect.contains(input) -> {
                isPressedThumb = true
                true
            }
            trackRect.contains(input) -> {
                isPressedTrack = true
                true
            }
            else -> super.touchDown(screenX, screenY)
        }
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        isPressedTrack = false
        return if (isPressedThumb && thumbRect.contains(Vector(screenX.toFloat(), screenY.toFloat()))) scrollTo(screenY)
        else super.touchUp(screenX, screenY)
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        Log.d("ScrollVertical", "$dx $dy")
        val screenX = Engine.game.input.x
        val screenY = Engine.game.height - Engine.game.input.y
        return if (isPressedTrack && trackRect.contains(Vector(screenX.toFloat(), screenY.toFloat()))) scrollTo(screenY)
        else if (contains(screenX, screenY)) {
            isPressed = false
            filingScrolling -= dy
            true
        } else false
    }

    class Style(
        background: Background? = null,
        backgroundColor: Color = Color.CLEAR,
        var thumb: Background,
        var track: Background,
        var rippleColor: Color = Color("#3e3e3e").also { it.a = 0.6f }
    ) : Widget.Style(background, backgroundColor) {

        constructor(
            background: Background? = null,
            backgroundColor: Color = Color.CLEAR,
            thumb: BaseShape,
            track: BaseShape,
            rippleColor: Color = Color("#3e3e3e").also { it.a = 0.6f }
        ) : this(
            background,
            backgroundColor,
            Background(thumb),
            Background(track),
            rippleColor
        )

        override fun copy(background: Background?, backgroundColor: Color?): Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                thumb,
                track,
                rippleColor
            )
        }

        fun copy(
            background: Background? = null,
            backgroundColor: Color? = null,
            thumb: Background? = null,
            track: Background? = null,
            rippleColor: Color? = null
        ): Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                thumb ?: this.thumb,
                track ?: this.track,
                rippleColor?:this.rippleColor
            )
        }
    }
}