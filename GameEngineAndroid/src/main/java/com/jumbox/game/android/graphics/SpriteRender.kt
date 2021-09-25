package com.jumbox.game.android.graphics

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Vector
import com.jumbox.game.android.graphics.Color.Companion.toColorInt

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class SpriteRender : Render() {

    var projectionMatrix: Camera? = null

    override fun draw(baseShape: BaseShape, x: Float, y: Float) {
        setRect(x, y, baseShape.width, baseShape.height)
        draw(baseShape)
    }

    override fun draw(baseShape: BaseShape, x: Int, y: Int) {
        setRect(x.toFloat(), y.toFloat(), baseShape.width, baseShape.height)
        draw(baseShape)
    }

    override fun draw(baseShape: BaseShape, x: Int, y: Int, width: Int, height: Int) {
        setRect(x.toFloat(), y.toFloat(), width, height)
        draw(baseShape)
    }

    override fun draw(color: Color, x: Int, y: Int, width: Int, height: Int) {
        rectShape.color = color
        setRect(x.toFloat(), y.toFloat(), width, height)
        draw(rectShape)
    }

    override fun draw(baseShape: BaseShape, x: Float, y: Float, width: Int, height: Int) {
        draw(baseShape, x.toInt(), y.toInt(), width, height)
    }

    override fun draw(baseShape: BaseShape, x: Float, y: Float, width: Float, height: Float) {
        draw(baseShape, x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    override fun draw(color: Color, x: Float, y: Float, width: Int, height: Int) {
        draw(color, x.toInt(), y.toInt(), width, height)
    }

    override fun draw(color: Color, x: Float, y: Float, width: Float, height: Float) {
        draw(color, x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    override fun debugDraw(color: Color, x: Float, y: Float, width: Float, height: Float) {
        val screen = projectionMatrix?.worldToScreen(x, y.gravityY() - height)?: Vector(x, y.gravityY() - height)
        super.debugDraw(color, screen.x, screen.y, width, height)
    }

    override fun setRect(x: Float, y: Float, width: Int, height: Int) {
        val screen = projectionMatrix?.worldToScreen(x, y.gravityY() - height)?: Vector(x, y.gravityY() - height)
        super.setRect(screen.x, screen.y, width, height)
    }

    override fun begin() {
        super.begin()
        canvas?.drawColor(Engine.game.backgroundColor.toColorInt())
    }

    override fun end() {
        super.end()
        canvas?.drawColor(Color.CLEAR.toColorInt())
    }

    private fun Float.gravityY() = Engine.game.height - this
}