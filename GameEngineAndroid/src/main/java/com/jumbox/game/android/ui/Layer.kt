package com.jumbox.game.android.ui

import com.jumbox.game.android.Engine
import com.jumbox.game.android.control.InputTouchController
import com.jumbox.game.android.graphics.ShapeRender
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class Layer : InputTouchController {

    private val shapeRender = ShapeRender()
    val screenWidth = Engine.game.width
    val screenHeight = Engine.game.height
    private var rootWidget: Widget = WidgetGroup("root", Size.fit).apply { layer = this@Layer }

    val root: WidgetGroup
    get() = rootWidget as WidgetGroup

    private var show = false
    val isShow: Boolean
    get() = show

    open fun create() {
        build()?.let {
            it.layer = this
            rootWidget = it
            rootWidget.invalidate()
        }
    }

    open fun show() {
        show = true
        rootWidget.isVisible = true
    }


    open fun pause() {
        show = false
        rootWidget.isVisible = false
    }

    open fun update() {
        if (!isShow) return

        rootWidget.update(Engine.game.deltaTime)
    }

    fun <T : Widget> find(key: String) : T? {
        return root.find(key)
    }

    open fun draw() {
        if (!isShow) return

        shapeRender.begin()
        rootWidget.draw(shapeRender)
        shapeRender.end()
    }

    open fun build() : Widget? = null

    open fun dispose() {
        rootWidget.dispose()
    }

    override fun touchDown(screenX: Int, screenY: Int): Boolean {
        val y = screenHeight - screenY
        return if (rootWidget.isVisible) rootWidget.touchDown(screenX, y) else false
    }

    override fun touchUp(screenX: Int, screenY: Int): Boolean {
        val y = screenHeight - screenY
        return if (rootWidget.isVisible) rootWidget.touchUp(screenX, y) else false
    }

    override fun touchMoved(dx: Float, dy: Float): Boolean {
        return if (rootWidget.isVisible) rootWidget.touchMoved(dx, dy) else false
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return if (rootWidget.isVisible) rootWidget.touchScroll(dx, dy) else false
    }

}