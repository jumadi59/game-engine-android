package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.Engine
import com.jumbox.game.android.graphics.*
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Scale.Companion.withTexture
import com.jumbox.game.android.ui.utils.Size

/**
 * Created by Jumadi Janjaya date on 28/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Image(
    private val texture: BaseShape,
    size: Size,
    style: Style = default) : Widget(null, size,
    style
) {

    companion object {
        val default = Style()
    }

    val requestStyle: Style
        get() = style as Style

    constructor(texture: BaseShape, style: Style) : this(texture, Size(texture.width, texture.height), style)
    constructor(texture: BaseShape, size: Size) : this(texture, size, default)

    override fun applyDraw(render: Render) {

        val rectImg = requestStyle.scaleType.withTexture(rect, Size(texture.width, texture.height))

        texture.color = requestStyle.colorFilter
        if (texture is Texture) texture.colorFilterMode = requestStyle.colorFilterMode
        render.draw(texture, rectImg.x, rectImg.y, rectImg.width, rectImg.height)
    }

    override fun invalidate() {
        val wrapSize = Size(texture.width, texture.height)
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

    class Style(
        background: Background? = null,
        backgroundColor: Color = Color.CLEAR,
        var scaleType: Scale.Type = Scale.Type.center,
        var colorFilter: Color = Color.CLEAR,
        var colorFilterMode: Int? = null
    ) :Widget.Style(background, backgroundColor){

        override fun copy(background: Background?, backgroundColor: Color?): Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                scaleType
            )
        }

        fun copy(background: Background? = null, backgroundColor: Color? = null, scaleType: Scale.Type? = null,
                 colorFilter: Color = Color.CLEAR,
                 colorFilterMode: Int? = null) : Style {
            return Style(
                background ?: this.background,
                backgroundColor ?: this.backgroundColor,
                scaleType?:this.scaleType,
                colorFilter?:this.colorFilter, colorFilterMode?:this.colorFilterMode
            )
        }
    }
}