package com.jumbox.game.android.ui.widget

import com.jumbox.game.android.Engine
import com.jumbox.game.android.Rectangle
import com.jumbox.game.android.graphics.Render
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.util.Extension.dpToPxInt

/**
 * Created by Jumadi Janjaya date on 31/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
open class Row(
    key: String? = null,
    size: Size = Size.wrap,
    var space: Int = 0,
    childes: Array<Widget>? = null,
    style: Style = Style()
) : WidgetGroup(key, size, childes, style) {

    constructor(
        size: Size = Size.wrap,
        spaceDp: Float = 0f,
        childes: Array<Widget>? = null,
        style: Style = Style()
    ) : this(null, size, spaceDp.dpToPxInt(), childes, style)

    override fun applyDraw(render: Render) {
        val cx = this.x
        var cy = this.y
        for (widget in childes) {
            if (widget.isVisible) {
                widget.invalidate()
                widget.setPosition(cx + space, (cy + space))
                widget.draw(render)
                cy += widget.rect.height + space
            }
        }
    }

    override fun invalidate() {
        val wrapSize = Size().apply {
            height = space
            width = 0
            childes.forEach {
                height += it.rect.height + space
                if (width < it.rect.width) width = it.rect.width
            }
            width += space * 2
        }

        val fitSize = when {
            parent != null -> parent!!.sizeChild(this)
            layer != null -> Size(layer!!.screenWidth, layer!!.screenHeight)
            else -> Size(Engine.game.width, Engine.game.height)
        }

        val w: Int = when {
            size.isWrapWidth -> wrapSize.width
            size.isFitWidth -> fitSize.width - (space * 2)
            else -> size.width
        }

        val h: Int = when {
            size.isWrapHeight -> wrapSize.height
            size.isFitHeight -> fitSize.height - (space * 2)
            else -> size.height
        }

        setSize(w, h)
    }

    override fun rectChild(child: Widget): Rectangle {
        return Rectangle(x + space, y + space, width - (space * 2), child.rect.height)
    }

    override fun sizeChild(child: Widget): Size {
        return Size(width - (space * 2), child.rect.height)
    }
}