package com.jumbox.game.android.graphics

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class ShapeRender : Render() {

    fun typeShape(type: Int) {
    }

    override fun draw(baseShape: BaseShape, x: Int, y: Int) {
        setRect(x.toFloat(), y.toFloat(), baseShape.width, baseShape.height)
        save()
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

    override fun draw(baseShape: BaseShape, x: Float, y: Float) {
        draw(baseShape, x.toInt(), y.toInt())
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

}