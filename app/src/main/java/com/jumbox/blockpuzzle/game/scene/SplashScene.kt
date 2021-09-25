package com.jumbox.blockpuzzle.game.scene

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.game.android.Engine
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.graphics.SpriteRender
import com.jumbox.game.android.screen.Scene
import com.jumbox.game.android.ui.widget.Text
import com.jumbox.game.android.util.Extension.dpToPx


/**
 * Created by Jumadi Janjaya date on 01/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class SplashScene(private val game: BlockGame) : Scene {

    private val spriteRender = SpriteRender()
    val screenWidth = Engine.game.width
    val screenHeight = Engine.game.height
    private val text = Text(Text.Style().apply {
        font = AssetManager.manager.fontMacan
         fontSize = 16f.dpToPx()
        color = Color.RED
    }, "Loading")

    override fun show() {
    }

    override fun pause() {}

    override fun update(delta: Float) {
        if (AssetManager.manager.totalLoad > 11) {
            game.setScene(MenuScene(game))
            game.finish(this)
        }
    }

    override fun render() {
        Engine.game.backgroundColor = "#272729".toColor()
        spriteRender.begin()
        text.text = "Loading ${AssetManager.manager.totalLoad}"
        text.draw(spriteRender)
        spriteRender.end()
    }

    override fun dispose() {

    }
}