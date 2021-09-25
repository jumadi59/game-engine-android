package com.jumbox.game.android.ui

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.animation.Animation
import com.jumbox.game.android.control.InputController
import com.jumbox.game.android.graphics.Background
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.graphics.ShapeRender
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Align.Companion.withinVec
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Scale.Companion.withRect
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Widget(
    var key: String? = null,
    size: Size = Size.wrap,
    var style: Style = Style()
) : InputController() {

    var indexZ = 0
        set(value) {
            val old = field
            field = value
            if (old != value) parent?.invalidate()
        }
    open var scale = Scale(1f, 1f)
    var touchListener: OnTouchListener? = null
    var isVisible = true

    var parent: WidgetGroup? = null
    var layer: Layer? = null
    var size: Size = size.copy()
    open var align = Align.topLeft
    var isDebug = false
    var onPressed: ((widget: Widget) -> Unit)? = null
    var alpha = 1f
        get() {
            return if (parent != null) parent!!.alpha else field
        }

    protected var x = 0
    protected var y = 0
    protected var width = 0
    protected var height = 0
    protected var isPressed = false

    val rect: Rectangle
        get() = Rectangle(x.toFloat(), y.toFloat(), width, height)

    open fun update(delta: Float) {}

    open fun draw(render: Render) {
        val saveRect = rect
        val rect = scale.withRect(saveRect)
        setRect(rect)
        var saveCount: Int? = null
        if (render is ShapeRender) {
            val marginWidthClip = width * 0.1f
            val marginHeightClip = height * 0.1f
            saveCount = render.save()
            render.clip(
                rect.x - marginWidthClip,
                rect.y - marginHeightClip,
                rect.width.toFloat() + marginWidthClip,
                rect.height.toFloat() + marginHeightClip
            )
        }

        if (!style.backgroundColor.isClear()) {
            val color = style.backgroundColor.copy()
            color.a *= alpha
            render.draw(color, rect.x, rect.y, rect.width, rect.height)
        } else if (style.background != null) {
            style.background?.set(rect)
            style.background?.draw(render, alpha)
        }

        applyDraw(render)
        if (isDebug) debugDraw(render, Color.RED)

        setRect(saveRect)
        if (render is ShapeRender) render.restore(saveCount)
    }

    open fun applyDraw(render: Render) {}

    open fun invalidate() {}

    protected fun setRect(rect: Rectangle) {
        width = rect.width
        height = rect.height
        x = rect.x.toInt()
        y = rect.y.toInt()
    }

    open fun setSize(width: Int, height: Int) {
        this.width = if (width < size.minWidth) size.minWidth else width
        this.height = if (height < size.minHeight) size.minHeight else height

    }

    fun debugDraw(render: Render, color: Color) {
        val rect = rect
        render.debugDraw(color, rect.x, rect.y, rect.width.toFloat(), rect.height.toFloat())
    }

    open fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y

        if (parent == null) return

        val size = parent!!.rectChild(this)
        val vec =
            align.withinVec(Rectangle(rect.x, rect.y, size.width, size.height), Size(width, height))
        this.x = vec.x.toInt()
        this.y = vec.y.toInt()
    }

    open fun dispose() {

    }

    fun contains(x: Int, y: Int): Boolean {
        return this.x < x && x < this.x + width && this.y < y && y < this.y + height
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        if (contains(screenX, screenY)) isPressed = true
        return isPressed
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        if (contains(screenX, screenY) && isPressed) {
            onPressed?.invoke(this)
            touchListener?.onPressed(this)
            isPressed = false
            return true
        }
        isPressed = false
        return false
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        if (isPressed && !contains(
                Engine.game.input.x,
                Engine.game.height - Engine.game.input.y
            )
        ) isPressed = false
        return false
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return false
    }

    abstract class OnTouchListener(
        private val clickAnimate: Animation? = null,
        private val scrollAnimate: Animation? = null
    ) {

        open fun onPressed(widget: Widget) {}
        open fun onScroll(widget: Widget) {}

    }

    open class Style(
        var background: Background? = null,
        var backgroundColor: Color = Color.CLEAR,
    ) {

        open fun copy(background: Background? = null, backgroundColor: Color? = null): Style {
            return Style(background ?: this.background, backgroundColor ?: this.backgroundColor)
        }
    }

}