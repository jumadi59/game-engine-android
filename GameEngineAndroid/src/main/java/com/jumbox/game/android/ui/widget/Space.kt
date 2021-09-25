package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Align.Companion.withinVec
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.util.Extension.dpToPxInt

/**
 * Created by Jumadi Janjaya date on 08/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Space<T : Widget>(
    private var left: Int = 0,
    private var right: Int = 0,
    private var top: Int = 0,
    private var bottom: Int = 0,
    private var child: T
) : WidgetGroup(null, Size.wrap) {

    constructor(space: Int, child: T) : this(space, space, space, space, child)
    constructor(
        leftDp: Float = 0f,
        rightDp: Float = 0f,
        topDp: Float = 0f,
        bottomDp: Float = 0f,
        child: T) : this(leftDp.dpToPxInt(), rightDp.dpToPxInt(), topDp.dpToPxInt(), bottomDp.dpToPxInt(), child)
    constructor(spaceDp: Float, child: T) : this(spaceDp, spaceDp, spaceDp, spaceDp, child)

    init {
        child.layer = layer
        child.parent = this
        child.invalidate()
        invalidate()
    }

    override var scale: Scale = Scale(1f, 1f)
        get() = super.scale
        set(value) {
            field = value
            throw IllegalArgumentException("Cannot set scale")
        }

    override var align: Align = Align.center
        get() = super.align
        set(value) {
            field = value
            throw IllegalArgumentException("Cannot set align")
        }

    override fun addWidget(widget: Widget) {
        throw IllegalArgumentException("Cannot add childes")
    }

    override fun removeWidget(widget: Widget) {
        throw IllegalArgumentException("Cannot remove childes")
    }

    override fun update(delta: Float) {
        if (child.isVisible && isVisible) child.update(delta)
    }

    override fun applyDraw(render: Render) {
        if (child.isVisible && isVisible) {
            child.invalidate()
            child.setPosition(this.x + left, this.y + top)
            child.draw(render)
        }
    }

    override fun draw(render: Render) {
        applyDraw(render)
    }

    override fun invalidate() {
        val size = child.size

        val wrapSize = Size(child.rect.width + left + right, child.rect.height + top + bottom)
        val fitSize = when {
            parent !=null -> parent!!.sizeChild(this)
            layer != null -> Size(layer!!.screenWidth, layer!!.screenHeight)
            else -> Size(Engine.game.width, Engine.game.height)
        }


        val w: Int = when {
            size.isWrapWidth -> wrapSize.width
            size.isFitWidth -> fitSize.width
            else -> child.size.width + left + right
        }

        val h: Int = when {
            size.isWrapHeight -> wrapSize.height
            size.isFitHeight -> fitSize.height
            else -> child.size.height + top + bottom
        }

        setSize(w, h)
    }

    override fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun setPosition(x: Int, y: Int) {
        if (parent == null) {
            this.x = x
            this.y = y
            return
        }

        val size = parent!!.rectChild(this)
        val vec = child.align.withinVec(Rectangle(x, y, size.width, size.height), Size(width, height))
        this.x = vec.x.toInt()
        this.y = vec.y.toInt()
    }

    override fun dispose() {
        child.dispose()
    }

    override fun rectChild(child: Widget): Rectangle {
        return Rectangle(x + left, y + top, width - left - right, height - top - bottom)
    }

    override fun sizeChild(child: Widget): Size {
        return Size(width - left - right, height - top - bottom)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Widget> find(key: String): T? {
        return if (child.key == key) child as T else null
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        return child.touchDown(screenX, screenY)
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        return child.touchUp(screenX, screenY)
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        return child.touchMoved(dx, dy)
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return child.touchScroll(dx, dy)
    }
}