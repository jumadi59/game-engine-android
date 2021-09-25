package com.jumbox.blockpuzzle.game

import com.jumbox.blockpuzzle.AssetManager
import com.jumbox.blockpuzzle.Theme
import com.jumbox.blockpuzzle.game.scene.SplashScene
import com.jumbox.game.android.Game
import com.jumbox.game.android.util.Preference


/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class BlockGame : Game() {

    companion object {
        lateinit var theme: Theme
        lateinit var preference: Preference
    }

    override fun start() {
        AssetManager.manager.splashLoad()
        AssetManager.manager.load()
        preference = Preference("com.jumbox.blockpuzzle.game")
        Theme.initialize()

        theme = Theme.themes[preference.getInt("theme", 0)]
        setScene(SplashScene(this))
    }

    override fun pause() {
        preference.save()
        super.pause()
    }

    override fun dispose() {
        super.dispose()
        AssetManager.manager.effectVanishSound.dispose()
        AssetManager.manager.invalidDrop.dispose()
        AssetManager.manager.dropSound.dispose()
    }

}