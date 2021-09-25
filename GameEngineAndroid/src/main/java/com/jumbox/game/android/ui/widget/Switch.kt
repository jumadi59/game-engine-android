package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.graphics.*
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Scale.Companion.withRect
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.util.Extension.dpToPxInt

/**
 * Created by Jumadi Janjaya date on 14/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Switch(
    style: Style = default,
    size: Size = Size.wrap
) : Widget(style = style, size = size) {

    companion object {
        val default = Style(
            thumb = Background(
                idle = Shape().load(
                    "shape: round\n" +
                            "round: [45, 45, 45, 45]\n" +
                            "width: 45\n" +
                            "height: 120\n" +
                            "color: #dadada"
                ), checked = Shape().load(
                    "shape: round\n" +
                            "round: [45, 45, 45, 45]\n" +
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
                ), checked = Shape().load(
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
    var onSwitch: ((Widget, Boolean) -> Unit)? = null
    var isChecked = false
        set(value) {
            val old = field
            field = value
            requestStyle.thumb.isChecked = value
            requestStyle.track.isChecked = value
            requestStyle.background?.isChecked = value
            if (old != value) onSwitch?.invoke(this, value)
        }

    private val shapeRipple = Shape().load(
        "shape: oval\n" +
                "width: 50\n" +
                "height: 50\n" +
                "color: #29a3f630"
    )
    private var scaleRipple = Scale(0f)
    private var thumbRect = Rectangle(0f, 0f, 45f.dpToPxInt(), 18f.dpToPxInt())
    private var trackRect = Rectangle(0f, 0f, 30f.dpToPxInt(), 30f.dpToPxInt())
    private var trackRippleRect = Rectangle(0f, 0f, 50f.dpToPxInt(), 50f.dpToPxInt())

    private var isMoved = false

    init {
        this.size.setMin(thumbRect.width + (trackRippleRect.width * 0.5f).toInt(), trackRippleRect.height)
        onPressed = {
            isChecked = !isChecked
            onSwitch?.invoke(this, isChecked)
        }
    }

    override fun applyDraw(render: Render) {
        trackRect.y = thumbRect.y + (thumbRect.height - trackRect.height) * 0.5f
        if (!isPressed && !isMoved) {
            if (isChecked)
                trackRect.x = Interpolation.linear.apply(trackRect.x, (thumbRect.x + thumbRect.width + 5f.dpToPxInt()) - trackRect.width, 0.5f)
            else
               trackRect.x = Interpolation.linear.apply(trackRect.x, thumbRect.x - 2.5f.dpToPxInt(), 0.5f)
        }

        if (isPressed) {
            scaleRipple.width = Interpolation.linear.apply(scaleRipple.width, 1f, 0.3f)
            scaleRipple.height = Interpolation.linear.apply(scaleRipple.height, 1f, 0.3f)
        } else {
            scaleRipple.width = Interpolation.linear.apply(scaleRipple.width, 0f, 0.3f)
            scaleRipple.height = Interpolation.linear.apply(scaleRipple.height, 0f, 0.3f)
        }

        requestStyle.thumb.set(thumbRect)
        requestStyle.thumb.draw(render, alpha)

        trackRippleRect.set(trackRect.x + ((trackRect.width - trackRippleRect.width) / 2), trackRect.y + ((trackRect.height - trackRippleRect.height) / 2))
        val rectRipple = scaleRipple.withRect(trackRippleRect)
        render.draw(shapeRipple, rectRipple.x, rectRipple.y, rectRipple.width, rectRipple.height)

        requestStyle.track.set(trackRect)
        requestStyle.track.draw(render, alpha)
    }

    override fun invalidate() {
        val wrapSize = Size(thumbRect.width, trackRippleRect.height)
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
        thumbRect.set(
            this.x.toFloat(),
            this.y.toFloat() + ((trackRect.height - thumbRect.height) / 2)
        )
    }

    private fun switch(dx: Float): Boolean {
        val x = trackRect.x + dx
        trackRect.x = when {
            x < thumbRect.x -> thumbRect.x - 2.5f.dpToPxInt()
            x + trackRect.width > thumbRect.x + thumbRect.width -> thumbRect.x + (thumbRect.width - trackRect.width) + 2.5f.dpToPxInt()
            else -> x
        }
        isMoved = true
        return true
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        return if (isPressed && x < Engine.game.input.x && Engine.game.input.y < x + width) {
            switch(dx)
        } else false
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        if (contains(screenX, screenY) && isPressed) {
            onPressed?.invoke(this)
            touchListener?.onPressed(this)
            isPressed = false
            return true
        } else if (isPressed) {
            if (isMoved) {
                isChecked = trackRect.x > (thumbRect.x + thumbRect.width) / 2
                switch(0f)
                onSwitch?.invoke(this, isChecked)
            }
        }
        isPressed = false
        return false
    }


    class Style(
        background: Background? = null,
        backgroundColor: Color = Color.CLEAR,
        var thumb: Background,
        var track: Background,
        var alignSwitch: Align = Align.centerLeft,
        var alignText: Align = Align.centerLeft,
        var rippleColor: Color = Color("#3e3e3e").also { it.a = 0.6f }
    ) : Widget.Style(background, backgroundColor) {

        constructor(
            background: Background? = null,
            backgroundColor: Color = Color.CLEAR,
            thumb: BaseShape,
            track: BaseShape,
            alignSwitch: Align = Align.centerLeft,
            alignText: Align = Align.centerLeft,
            rippleColor: Color = Color("#3e3e3e").also { it.a = 0.6f }
        ) : this(
            background,
            backgroundColor,
            Background(thumb),
            Background(track),
            alignSwitch,
            alignText,
            rippleColor
        )

        override fun copy(background: Background?, backgroundColor: Color?): Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                thumb,
                track,
                alignSwitch,
                alignText,
                rippleColor
            )
        }

        fun copy(
            background: Background? = null,
            backgroundColor: Color? = null,
            thumb: Background? = null,
            track: Background? = null,
            alignSwitch: Align? = null,
            alignText: Align? = null,
            rippleColor: Color? = null
        ): Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                thumb ?: this.thumb,
                track ?: this.track,
                alignSwitch ?: this.alignSwitch,
                alignText ?: this.alignText,
                rippleColor?:this.rippleColor
            )
        }
    }
}