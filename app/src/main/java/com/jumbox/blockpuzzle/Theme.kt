package com.jumbox.blockpuzzle

import com.jumbox.game.android.graphics.Background
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.graphics.Texture
import com.jumbox.game.android.ui.utils.Align
import com.jumbox.game.android.ui.utils.Scale
import com.jumbox.game.android.ui.widget.Image
import com.jumbox.game.android.ui.widget.Text

/**
 * Created by Jumadi Janjaya date on 18/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
data class Theme(
    val backgroundColor: Color,
    val backgroundColorA7: Color,
    val colorLight: Color,
    val blockColors: Array<Color>,
    val blockColorEmpty: Color,
    val textLightStyle: Text.Style,
    val textDarkStyle: Text.Style,
    val btnTextStyle: Text.Style,
    val btnImageStyle: Image.Style,
    val btnOvalImageStyle: Image.Style
) {

    companion object {
        val themes = ArrayList<Theme>()

        fun initialize() {
            val themeDark = Theme(
                backgroundColor = "#272729".toColor(),
                backgroundColorA7 = "#272729".toColor().apply { a = 0.7f },
                colorLight = "#a2acb8".toColor(),
                blockColors = arrayOf(
                    "#be545e".toColor(), "#486acb".toColor(), "#6dbfb0".toColor(), "#67a0fa".toColor(),
                    "#d27623".toColor(), "#dfb03e".toColor(), "#aa6fc9".toColor(), "#76be14".toColor()
                ),
                blockColorEmpty = "#444d56".toColor(),
                textLightStyle = Text.Style(
                    font = AssetManager.manager.fontMacan,
                    fontSize = 13f,
                    color = "#feffff".toColor()
                ),
                textDarkStyle = Text.Style(
                    font = AssetManager.manager.fontMacan,
                    fontSize = 18f,
                    color = "#666d75".toColor()
                ),
                btnTextStyle = Text.Style(
                    font = AssetManager.manager.fontMacan,
                    fontSize = 18f,
                    color = "#feffff".toColor(),
                    background = Background(AssetManager.manager.shapeBtnRoundRed),
                    align = Align.center
                ),
                btnImageStyle = Image.Style(
                    background = Background(AssetManager.manager.shapeBtnRoundRed),
                    scaleType = Scale.Type.centerInside
                ),
                btnOvalImageStyle = Image.Style(
                    background = Background(AssetManager.manager.shapeBtnOvalDark),
                    scaleType = Scale.Type.centerInside,
                    colorFilterMode = Texture.MULTIPLY,
                    colorFilter = "#feffff".toColor()
                )
            )

            val themeLight = themeDark.copy(
                backgroundColor = "#e5e9f2".toColor(),
                backgroundColorA7 = "#e5e9f2".toColor().apply { a = 0.7f },
                blockColorEmpty = "#c7d5e2".toColor(),
                colorLight = "#666d75".toColor(),
                textLightStyle = Text.Style(
                    font = AssetManager.manager.fontMacan,
                    fontSize = 13f,
                    color = "#2e3233".toColor()
                ),
                textDarkStyle = Text.Style(
                    font = AssetManager.manager.fontMacan,
                    fontSize = 18f,
                    color = "#a2acb8".toColor()
                ),
                btnOvalImageStyle = themeDark.btnOvalImageStyle.copy(
                    background = Background(AssetManager.manager.shapeBtnOvalLight),
                    colorFilter = "#666d75".toColor()
                )
            )
            themes.add(themeDark)
            themes.add(themeLight)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Theme

        if (backgroundColor != other.backgroundColor) return false
        if (backgroundColorA7 != other.backgroundColorA7) return false
        if (!blockColors.contentEquals(other.blockColors)) return false
        if (textLightStyle != other.textLightStyle) return false
        if (textDarkStyle != other.textDarkStyle) return false
        if (btnTextStyle != other.btnTextStyle) return false
        if (btnImageStyle != other.btnImageStyle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + backgroundColorA7.hashCode()
        result = 31 * result + blockColors.contentHashCode()
        result = 31 * result + textLightStyle.hashCode()
        result = 31 * result + textDarkStyle.hashCode()
        result = 31 * result + btnTextStyle.hashCode()
        result = 31 * result + btnImageStyle.hashCode()
        return result
    }

}