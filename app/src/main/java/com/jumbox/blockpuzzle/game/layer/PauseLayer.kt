package com.jumbox.blockpuzzle.game.layer

import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.game.android.Engine
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.control.InputTouchController
import com.jumbox.game.android.ui.Layer
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.ui.widget.Row
import com.jumbox.game.android.ui.widget.Text

/**
 * Created by Jumadi Janjaya date on 02/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class PauseLayer : Layer() {

    private var oldInputTouchController: InputTouchController? = null
    var screenShot = false

    override fun build(): Widget {
        return WidgetGroup(size = Size.fit,
            style = Widget.Style(backgroundColor = BlockGame.theme.backgroundColorA7),
            childes = arrayOf(
                Row(
                    size = Size.wrap, 8f, arrayOf(
                        Text(
                            style = BlockGame.theme.textLightStyle.copy(fontSize = 25f), "Paused"
                        ).apply { align = Align.center }, //Text
                        Text(
                            style = BlockGame.theme.textLightStyle.copy(fontSize = 16f),
                            "Ketuk untuk melanjutkan"
                        ) // Text
                    )
                ).apply { align = Align.center } // Row
            )).apply {
            onPressed = {
                pause()
            }
        } // WidgetGroup
    }

    override fun show() {
        super.show()
        oldInputTouchController = Engine.game.input.touchController
        Engine.game.input.touchController = this
        root.alpha = 0f
    }

    override fun pause() {
        super.pause()
        Engine.game.input.touchController = oldInputTouchController
    }

    override fun update() {
        super.update()
        root.alpha = Interpolation.linear.apply(root.alpha, 1f, 0.3f)
    }
}