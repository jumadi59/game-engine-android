package com.jumbox.game.android.graphics

import android.graphics.Canvas
import android.graphics.RectF
import com.jumbox.game.android.util.BaseFile

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class BaseShape : BaseFile() {

    open var color = Color()
    open var padding = FloatArray(4)
    open var width = Shape.MATCH
    open var height = Shape.MATCH
    open var round = FloatArray(4)
    open var colors: IntArray? = null
    open var rotate: Float = 0f

    abstract fun setBounds(rect: RectF)
    abstract fun setSize(width: Int, height: Int)
    abstract fun setPosition(x: Int, y: Int)
    abstract fun draw(canvas: Canvas)
    abstract fun clear()
}