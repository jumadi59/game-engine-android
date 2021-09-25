package com.jumbox.blockpuzzle.game.layer

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.formatString
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.blockpuzzle.serializer.BinSerializable
import com.jumbox.game.android.animation.Interpolation
import com.jumbox.game.android.ui.Layer
import com.jumbox.game.android.ui.Widget
import com.jumbox.game.android.ui.WidgetGroup
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.utils.Size
import com.jumbox.game.android.ui.widget.*
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Created by Jumadi Janjaya date on 23/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class MainLayer : Layer(), BinSerializable {

    var bestScore = BlockGame.preference.getInt("bestScore", 0)
    var currentScore = 0
    var score = 0f
    private var textBestSore: Text? = null
    private var textScore: Text? = null

    override fun build(): Widget {
        return WidgetGroup(size = Size.fit, childes = arrayOf(
            Row(size = Size.wrap, spaceDp = 6f, childes = arrayOf(
                Space(
                    topDp = 12f, child = Column(
                        Size.wrap, spaceDp = 2f, childes = arrayOf(
                            Image(
                                size = Size(30f, 30f),
                                texture = AssetManager.manager.mahkotaTexture,
                                style = Image.Style(scaleType = Scale.Type.center),
                            ) // Image
                            ,
                            Text(
                                style = BlockGame.theme.textDarkStyle,
                                text = "Best ${bestScore.formatString()}"
                            ).apply {
                                align = Align.bottomCenter
                                textBestSore = this
                            }// Text
                        )
                    ) // Column
                ) // Space
                ,
                Text(
                    style = BlockGame.theme.textLightStyle.copy(fontSize = 27f),
                    text = "$currentScore"
                ).apply {
                    align = Align.center
                    textScore = this
                }
            )).apply {
                align = Align.topCenter
            } //Row
            , Space(spaceDp = 16f,
                child = ImageButton(
                    texture = AssetManager.manager.iconPaused,
                    size = Size(50f, 50f),
                    style = BlockGame.theme.btnOvalImageStyle
                ).apply { key = "btnPause" }
            ), Space(spaceDp = 16f,
                child = ImageButton(
                    texture = AssetManager.manager.iconReset,
                    size = Size(50f, 50f),
                    style = BlockGame.theme.btnOvalImageStyle
                ).apply {
                    align = Align.topRight
                    key = "btnReset"
                }
            )
        )
        ) //WidgetGroup
    }

    override fun touchScroll(dx: Float, dy: Float): Boolean {
        return false
    }

    fun addScore(score: Int) {
        currentScore += score
        if (currentScore > bestScore) {
            bestScore = currentScore
            textBestSore!!.text = "Best " + bestScore.formatString()
            BlockGame.preference.setInt("bestScore", currentScore)
            //Log.d("MainLayer", "$bestScore $currentScore")
        }
    }

    fun reset() {
        currentScore = 0
    }

    override fun update() {
        super.update()
        if (textScore?.isVisible == true) {
            val roundScore = score
            if (roundScore != currentScore.toFloat()) {
                score = Interpolation.linear.apply(roundScore, currentScore.toFloat(), 0.1f)
                textScore!!.text = score.toInt().formatString()
            }
        }
    }

    override fun write(out: DataOutputStream) {
        out.writeInt(currentScore)
        out.writeInt(bestScore)
    }

    override fun read(`in`: DataInputStream) {
        currentScore = `in`.readInt()
        bestScore = `in`.readInt()
    }

}