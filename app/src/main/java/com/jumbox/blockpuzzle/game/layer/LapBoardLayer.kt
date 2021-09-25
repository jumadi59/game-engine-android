package com.jumbox.blockpuzzle.game.layer

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.game.scene.MenuScene
import com.jumbox.game.android.Engine
import com.jumbox.game.android.ui.Layer
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.ui.widget.*

/**
 * Created by Jumadi Janjaya date on 22/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class LapBoardLayer(private val menuScene: MenuScene) : Layer() {

    override fun build(): Widget {
        return Space(
            spaceDp = 20f, child = WidgetGroup(
                size = Size.fit, childes = arrayOf(
                    ImageButton(
                        texture = AssetManager.manager.iconBack,
                        size = Size(50f, 50f),
                        style = BlockGame.theme.btnOvalImageStyle
                    ).apply {
                        align = Align.topLeft
                        onPressed = {
                            menuScene.showMenu()
                        }
                    }, //ImageButton
                    Text(
                        style = BlockGame.theme.textLightStyle.copy(fontSize = 20f),
                        text = "LapBoard"
                    ).apply { align = Align.topCenter }, //Text
                    Space(
                        topDp = 60f, child =
                        ScrollVertical(size = Size.fit, childes = Array(20) {
                            Column(
                                size = Size.fitWrap,
                                spaceDp = 20f,
                                childes = arrayOf(
                                    Text(
                                        size = Size.wrap,
                                        style = BlockGame.theme.textLightStyle.copy(align = Align.centerLeft),
                                        text = "$it."
                                    ), //Text
                                    Text(
                                        size = Size.wrap,
                                        style = BlockGame.theme.textLightStyle.copy(align = Align.centerLeft),
                                        text = "$it,000"
                                    ) //Text
                                ),
                                style = Widget.Style(backgroundColor = BlockGame.theme.blockColors[it % 8])
                            ).apply { isDebug = true } //Column
                        }) //ScrollVertical
                    ), //Space
                )
            ) //WidgetGroup
        ) //Space
    }

    override fun show() {
        super.show()
        Engine.input.touchController = this
    }
}