package com.jumbox.blockpuzzle.game.layer

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.Theme
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.game.scene.GameScene
import com.jumbox.blockpuzzle.game.scene.MenuScene
import com.jumbox.game.android.Engine
import com.jumbox.game.android.graphics.Background
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.ui.Layer
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.ui.widget.*

/**
 * Created by Jumadi Janjaya date on 07/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class MenuLayer(private val menuScene: MenuScene) : Layer() {

    private val btnTextStyleTea =
        BlockGame.theme.btnTextStyle.copy(background = Background(AssetManager.manager.shapeBtnRoundTea))

    private val imgBtnTextStyleYellow =
        BlockGame.theme.btnImageStyle.copy(Background(AssetManager.manager.shapeBtnRoundYellow))
    private val imgBtnTextStyleBlue =
        BlockGame.theme.btnImageStyle.copy(Background(AssetManager.manager.shapeBtnRoundBlue))

    override fun build(): Widget {
        return WidgetGroup(size = Size.fit, childes = arrayOf(
            Row(
                size = Size.wrap, spaceDp = 12f, childes = arrayOf(
                    Column(
                        size = Size.wrap, spaceDp = 3f, childes = arrayOf(
                            Image(
                                size = Size(widthDp = 55f, heightDp = 55f),
                                texture = AssetManager.manager.cubeTextures[0],
                                style = Image.Style(scaleType = Scale.Type.center)
                            ), //Image
                            Text(
                                size = Size.wrap,
                                style = Text.Style(
                                    font = AssetManager.manager.fontStangkorv,
                                    fontSize = 32f,
                                    fontStyle = Text.Style.BOLD,
                                    color = "#ff5d72".toColor()
                                ),
                                text = "BLOCK"
                            ).apply { align = Align.center }, // text
                            Text(
                                size = Size.wrap,
                                style = Text.Style(
                                    font = AssetManager.manager.fontStangkorv,
                                    fontSize = 32f,
                                    fontStyle = Text.Style.BOLD,
                                    color = BlockGame.theme.colorLight
                                ),
                                text = "JAM"
                            ).apply { align = Align.center }, // text
                        )
                    ).apply {
                        align = Align.center
                    }, //Column
                    Button(
                        size = Size(200f, 50f),
                        style = BlockGame.theme.btnTextStyle,
                        text = "Play"
                    ).apply {
                        align = Align.center
                        onPressed = {
                            menuScene.game.setScene(GameScene(menuScene.game))
                        }
                    }, //Button
                    Button(
                        size = Size(200f, 50f),
                        style = btnTextStyleTea,
                        text = "Play Time Mode"
                    ).apply {
                        align = Align.center
                        onPressed = {
                            menuScene.game.setScene(GameScene(menuScene.game))
                        }
                    }, //Button
                    Column(
                        size = Size.wrap, spaceDp = 8f, childes = arrayOf(
                            Row(
                                spaceDp = 8f, childes = arrayOf(
                                    ImageButton(
                                        texture = AssetManager.manager.iconGame,
                                        size = Size(50f, 50f),
                                        style = BlockGame.theme.btnImageStyle
                                    ).apply {
                                        align = Align.center
                                        onPressed = {
                                            Engine.game.openLink("https://play.google.com/store/apps/dev?id=8642106023087130877")
                                        }
                                    }, // ImageButton
                                    Text(
                                        size = Size.wrap,
                                        style = BlockGame.theme.textLightStyle,
                                        text = "More Game"
                                    ) // Text
                                )
                            ), //Row
                            Row(
                                spaceDp = 8f, childes = arrayOf(
                                    ImageButton(
                                        texture = AssetManager.manager.iconLapboard,
                                        size = Size(50f, 50f),
                                        style = imgBtnTextStyleYellow
                                    ).apply {
                                        align = Align.center
                                        onPressed = {
                                            menuScene.showLapBoard()
                                        }
                                    }, // ImageButton
                                    Text(
                                        size = Size.wrap,
                                        style = BlockGame.theme.textLightStyle,
                                        text = "Lapboard"
                                    ) // Text
                                )
                            ), //Row
                            Row(
                                spaceDp = 8f, childes = arrayOf(
                                    ImageButton(
                                        texture = AssetManager.manager.iconRate,
                                        size = Size(50f, 50f),
                                        style = imgBtnTextStyleBlue
                                    ).apply {
                                        align = Align.center
                                        onPressed = {
                                            Engine.game.openLink("https://play.google.com/store/apps/dev?id=")
                                        }
                                    }, // ImageButton
                                    Text(
                                        size = Size.wrap,
                                        style = BlockGame.theme.textLightStyle,
                                        text = "Rate Game"
                                    ) // Text
                                )
                            ), //Row
                        )
                    ) //Column
                )
            ).apply {
                align = Align.center
            }, //Row
            Space(spaceDp = 16f, child = Switch().apply {
                align = Align.topLeft
                isChecked = BlockGame.preference.getInt("theme", 0) == 0
                onSwitch = { _, b ->
                    val darkLight = if (b) 0 else 1
                    BlockGame.theme = Theme.themes[darkLight]
                    BlockGame.preference.setInt("theme", darkLight)
                    this@MenuLayer.create()
                    menuScene.lapBoardLayer.create()
                }
            } // Switch
            ) //Space
        ))
    }

    override fun show() {
        super.show()
        Engine.game.input.touchController = this
    }
}