package com.jumbox.blockpuzzle.game.layer

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.formatString
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.game.android.Engine
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.control.InputTouchController
import com.jumbox.game.android.graphics.Background
import com.jumbox.game.android.ui.Layer
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.ui.widget.*

/**
 * Created by Jumadi Janjaya date on 10/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class GameOverLayer : Layer() {

    private var oldInputTouchController: InputTouchController? = null
    private var textScore: Text? = null
    private var score = 0f
    private var currentScore = 0

    private val btnTextStyleTea =
        BlockGame.theme.btnTextStyle.copy(background = Background(AssetManager.manager.shapeBtnRoundTea))

    private val imgBtnTextStyleYellow =
        BlockGame.theme.btnImageStyle.copy(Background(AssetManager.manager.shapeBtnRoundYellow))
    private val imgBtnTextStyleBlue =
        BlockGame.theme.btnImageStyle.copy(Background(AssetManager.manager.shapeBtnRoundBlue))

    private val menu = Column(
        size = Size.wrap, spaceDp = 8f, childes = arrayOf(
            Row(
                spaceDp = 8f, childes = arrayOf(
                    ImageButton(
                        texture = AssetManager.manager.iconGame,
                        size = Size(50f, 50f),
                        style = BlockGame.theme.btnImageStyle
                    ).apply { align = Align.center }, // ImageButton
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
                    ).apply { align = Align.center }, // ImageButton
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
                    ).apply { align = Align.center }, // ImageButton
                    Text(
                        size = Size.wrap,
                        style = BlockGame.theme.textLightStyle,
                        text = "Rate Game"
                    ) // Text
                )
            ), //Row
        )
    ) //Column

    override fun build(): Widget {
        return WidgetGroup(size = Size.fit,
            style = Widget.Style(backgroundColor = BlockGame.theme.backgroundColorA7),
            childes = arrayOf(
                Row(size = Size.wrap, space = 0, childes = arrayOf(
                    Space(bottomDp = 40f, child = Row(
                        size = Size.wrap, spaceDp = 8f, childes = arrayOf(
                            Text(
                                size = Size.wrap,
                                style = BlockGame.theme.textLightStyle.copy(fontSize = 42f),
                                text = "0,000"
                            ).apply {
                                align = Align.center
                                textScore = this
                            }, //Text
                            Text(
                                size = Size.wrap,
                                style = BlockGame.theme.textLightStyle.copy(fontSize = 26f),
                                text = "OUT OF MOVIES!"
                            ).apply { align = Align.center }, //Text
                        )
                    ).apply { align = Align.center } //Row
                    ), // Space
                    Space(
                        bottomDp = 10f,
                        child = Button(
                            size = Size(200f, 50f),
                            style = BlockGame.theme.btnTextStyle,
                            text = "Retry"
                        ).apply {
                            align = Align.bottomCenter
                            key = "btnRetry"
                        } // Button
                    ), // Space
                    Space(
                        bottomDp = 25f,
                        child = Button(
                            size = Size(200f, 50f),
                            style = btnTextStyleTea,
                            text = "Quit"
                        ).apply {
                            align = Align.bottomCenter
                            key = "btnQuit"
                        } // Button
                    ), // Space
                    menu // menu
                )).apply {
                    align = Align.center
                }, //Row
            )) // WidgetGroup
    }

    override fun show() {
        super.show()
        oldInputTouchController = Engine.game.input.touchController
        Engine.game.input.touchController = this
        root.alpha = 0f
    }

    fun show(score: Int) {
        currentScore = score
        show()
    }

    override fun pause() {
        super.pause()
        Engine.game.input.touchController = oldInputTouchController
    }

    override fun update() {
        super.update()
        if (textScore?.isVisible == true) {
            score = Interpolation.linear.apply(score, currentScore.toFloat(), 0.1f)
            textScore!!.text = score.toInt().formatString()
        }
        root.alpha = Interpolation.linear.apply(root.alpha, 1f, 0.3f)
    }
}