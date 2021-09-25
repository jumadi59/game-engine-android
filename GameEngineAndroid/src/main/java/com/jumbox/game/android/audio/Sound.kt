package com.jumbox.game.android.audio

import android.media.SoundPool
import com.jumbox.game.android.Engine
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.FileHandle

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Sound : Audio() {

    companion object {
        const val MAX_STREAMS = 100
    }

    var soundManager: SoundManager = Engine.game.soundManager
    private var soundPool: SoundPool = soundManager.add(this)
    var soundId = -1
    private var soundPoolLoaded = false

    var leftVolume = 1f
    var rightVolume = 1f
    var loop = 0
    val isSoundPoolLoaded
    get() = soundPoolLoaded
    val id
    get() = soundId

    override fun loadComplete() {
        soundPoolLoaded = true
    }

    override fun play() {
        if (isSoundPoolLoaded && soundId != -1)
            soundPool.play(soundId, leftVolume, rightVolume, 1, loop, 1f)
    }

    fun play(volume: Float, loop: Int) {
        if (isSoundPoolLoaded && soundId != -1)
            soundPool.play(soundId, volume, volume, 1, loop, 1f)
    }

    override fun pause() {
        if (isSoundPoolLoaded && soundId == -1)
            soundPool.pause(soundId)
    }

    override fun resume() {
        if (isSoundPoolLoaded && soundId == -1)
            soundPool.resume(soundId)
    }

    override fun dispose() {
        super.dispose()
        soundManager.remove(this)
    }


    override fun load(fileHandle: FileHandle): BaseFile {
        super.load(fileHandle)
        try {
            soundId = if (fileHandle.type == FileHandle.INTERNAL) {
                val descriptor = fileHandle.assetFileDescriptor()
                soundPool.load(descriptor, 1)
            } else {
                soundPool.load(fileHandle.file.absolutePath, 1)
            }
        } catch (ex: Exception) {
            throw IllegalArgumentException("Error reading file: $fileHandle", ex)
        }
        return this
    }

}