package com.jumbox.game.android.ui

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.graphics.ShapeRender
import com.jumbox.game.android.ui.utils.Scale.Companion.withRect
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 24/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class WidgetGroup(
    key: String? = null, size: Size = Size.wrap, childes: Array<Widget>? = null, style: Style = Style()) : Widget(key, size, style) {

    private val widgets = ArrayList<Widget>()
    val childes: List<Widget>
        get() = widgets

    init {
        childes?.let { it ->
             it.forEach {
                 addWidget(it)
             }
         }
    }

    open fun addWidget(widget: Widget) {
        widget.layer = layer
        widget.parent = this

        widgets.add(widget)
        widgets.sortBy { it.indexZ }

        widget.invalidate()
        invalidate()
    }

    open fun removeWidget(widget: Widget) {
        widgets.remove(widget)
        widgets.sortBy { it.indexZ }
        invalidate()
    }

    override fun update(delta: Float) {
        for (widget in childes) {
            if (widget.isVisible) widget.update(delta)
        }
    }

    override fun draw(render: Render) {
        if (parent == null) invalidate()

        val saveRect = rect
        val rect = scale.withRect(saveRect)
        setRect(rect)
        var saveCount: Int? = null
        if (render is ShapeRender) {
            saveCount = render.save()
            render.clip(
                rect.x,
                rect.y,
                rect.width.toFloat(),
                rect.height.toFloat()
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

    override fun applyDraw(render: Render) {
        for (widget in childes) {
            widget.invalidate()
            widget.setPosition(x, y)
            if (widget.isVisible) {
                widget.draw(render)
            }
        }
    }

    override fun invalidate() {
        val wrapSize = Size().apply {
            childes.forEach {
                if (width < it.rect.width) width = it.rect.width
                if (height < it.rect.height)  height =  it.rect.height
            }
        }

        val fitSize = when {
            parent !=null -> parent!!.sizeChild(this)
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

    open fun rectChild(child: Widget) : Rectangle {
        return Rectangle(x, y, width, height)
    }

    open fun sizeChild(child: Widget) : Size {
        return Size(width, height)
    }

    override fun dispose() {
        childes.forEach { it.dispose() }
        super.dispose()
    }

    @Suppress("UNCHECKED_CAST")
    open fun <T : Widget> find(key: String) : T? {
        var widget: T? = childes.find { it.key == key && it.key != null } as T?
        if (widget == null) {
            val widgetGroups = childes.filterIsInstance<WidgetGroup>()
            for (widgetGroup in widgetGroups) {
                val w = widgetGroup.find<T>(key)
                if (w != null) {
                    widget = w
                    break
                }
            }
        }
        return widget
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        for (widget in childes) {
            if (!widget.isVisible) continue
            else if (widget.touchDown(screenX, screenY)) return true
        }
        return super.touchDown(screenX, screenY)
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        for (widget in childes) {
            if (!widget.isVisible) continue
            else if (widget.touchUp(screenX, screenY)) return true
        }
        return super.touchUp(screenX, screenY)
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        for (widget in childes) {
            if (!widget.isVisible) continue
            else if (widget.touchMoved(dx, dy)) return true
        }
        return super.touchMoved(dx, dy)
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        for (widget in childes) {
            if (!widget.isVisible) continue
            else if (widget.touchScroll(dx, dy)) return true
        }
        return super.touchScroll(dx, dy)
    }
}