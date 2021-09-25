package com.jumbox.game.android.ui.utils

import com.jumbox.game.android.util.Extension.dpToPxInt

/**
 * Created by Jumadi Janjaya date on 07/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

data class Size(var width: Int, var height: Int, var minWidth: Int = 0, var minHeight: Int = 0) {

    val widthF: Float
        get() = width.toFloat()
    val heightF: Float
        get() = height.toFloat()

    val isWrap: Boolean
    get() = isWrapWidth && isWrapHeight
    val isFit: Boolean
        get() = isFitWidth && isFitHeight

    val isWrapWidth: Boolean
        get() = width == -1
    val isFitWidth: Boolean
        get() = width == -2

    val isWrapHeight: Boolean
        get() = height == -1
    val isFitHeight: Boolean
        get() = height == -2

    constructor(widthDp: Float, heightDp: Float) : this(widthDp.dpToPxInt(), heightDp.dpToPxInt())
    constructor(size: Size, minWidth: Int = 0, minHeight: Int = 0) : this(size.width, size.height, minWidth, minHeight)
    constructor(size: Size, minWidthDp: Float = 0f, minHeightDp: Float = 0f) : this(size.width, size.height, minWidthDp.dpToPxInt(), minHeightDp.dpToPxInt())
    constructor() : this(0, 0)

    fun setMin(width: Int, height: Int) {
        minWidth = width
        minHeight = height
    }

    fun copy() = Size(width, height, minWidth, minHeight)

    companion object {
        const val WRAP = -1
        const val FIT = -2
        val wrap = Size(width = -1, height = -1)
        val fit = Size(width = -2, height = -2)
        val wrapFit = Size(width = -1, height = -2)
        val fitWrap = Size(width = -2, height = -1)
    }
}