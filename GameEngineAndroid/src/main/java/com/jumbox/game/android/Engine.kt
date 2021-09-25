package com.jumbox.game.android

import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.view.SurfaceHolder
import com.jumbox.game.android.audio.MusicManager
import com.jumbox.game.android.audio.SoundManager
import com.jumbox.game.android.control.TouchHandler
import com.jumbox.game.android.graphics.Color
import com.jumbox.game.android.util.Files

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Engine {

    companion object {

        const val FPS = 30f
        const val MAX_FPS = 60f
        const val MAX_FRAME_SKIPS = 5
        const val FRAME_PERIOD = 1000 / MAX_FPS
        private var engine: Engine? = null

        val game: Engine
        get() = if (engine != null) engine!! else getInstance()

        val input = game.input
        //val music = game.musicManager
        //val sound = game.soundManager

        fun getInstance() : Engine {
            if (engine == null)
                engine = Engine()

            return engine!!
        }

    }

    lateinit var soundManager: SoundManager
    lateinit var musicManager: MusicManager
    private lateinit var gameView: GameView

    private var canvas: Canvas? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var game: Game? = null

    var width = 0
    var height = 0
    val input = TouchHandler()
    var backgroundColor = Color.CLEAR
    var gameCallback: GameCallback? = null
    var deltaTime = 0.0f

    fun initialize(gameView: GameView) {
        this.gameView = gameView
        Files.assets = gameView.context.assets
        Files.localDir = gameView.context.filesDir
        soundManager = SoundManager()
        musicManager = MusicManager()
    }

    fun createSurfaceView(holder: SurfaceHolder) {
        if (surfaceHolder != null) return

        surfaceHolder = holder
        val game = gameCallback?.startGame()
        if (game != null) {
            game.start()
            synchronized(game) {
                this.game = game
            }
        }
        resume()
    }

    fun update(delta: Float) {
        this.deltaTime = delta
        game?.update(this.deltaTime)
    }

    fun render(canvas: Canvas?) {
        this.canvas = canvas
        game?.render()
    }

    fun setCanvas(canvas: Canvas) {
        this.canvas = canvas
    }

    fun getCanvas() = canvas

    fun pause() {
        game?.pause()
    }

    fun resume() {
        game?.show()
    }

    fun back() : Boolean {
        return game?.back()?:true
    }

    fun exit() {
        gameView.pause()
        game?.dispose()
        gameCallback?.exit()
    }

    fun openLink(link: String) {
        gameView.context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        })
    }

    fun destroy() {
        gameView.pause()
        game?.dispose()
    }

}