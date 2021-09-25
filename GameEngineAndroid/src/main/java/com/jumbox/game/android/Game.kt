package com.jumbox.game.android

import com.jumbox.game.android.screen.Scene

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class Game {

    private var scene: Scene? = null
    private var isScenePause = false
    private var historyScene = ArrayList<Scene>()

    fun getScene() = scene

    abstract fun start()

    open fun show() {
        scene?.show()
        isScenePause = false
    }

    open fun pause() {
        scene?.pause()
        isScenePause = true
    }

    open fun update(delta: Float) {
        if (!isScenePause) scene?.update(delta)
    }

    open fun render() {
        if (!isScenePause) scene?.render()
    }

    open fun setScene(scene: Scene, isRecord: Boolean = true) {
        isScenePause = true
        this.scene?.pause()
        if (isRecord) historyScene.add(scene)

        this.scene = scene
        this.scene?.show()
        isScenePause = false
    }

    open fun back() : Boolean {
        return if (historyScene.size > 1) {
            isScenePause = true
            this.scene?.pause()
            finish(scene!!)
            setScene(historyScene[historyScene.size-1], false)
            false
        } else true
    }

    fun finish(scene: Scene) {
        scene.dispose()
        historyScene.remove(scene)
    }

    open fun dispose() {
        this.scene?.dispose()
        historyScene.clear()
    }

}