package com.jumbox.game.android.audio

import android.media.MediaPlayer
import android.util.Log
import com.jumbox.game.android.Engine
import com.jumbox.game.android.util.FileHandle

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Music : Audio() {

    private val musicManager = Engine.game.musicManager

    private var mediaPlayer = MediaPlayer()
    private var volumeLeft: Float = 1.0f
    private var volumeRight: Float = 1.0f

    var loop: Boolean = false
        set(value) {
            field = value
            mediaPlayer.isLooping = value
        }
    var isRunning = false
    var isPause = false

    init {

    }

    override fun loadComplete() {

    }

    override fun play() {
        mediaPlayer.start().also {
            isRunning = true
            isPause = false
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPause = true
            isRunning = false
        }
    }

    override fun resume() {
        if (isPause) {
            play()
        }
    }

    fun stop() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
            isPause = false
            isRunning = false
        }
    }

    override fun dispose() {
        super.dispose()
        musicManager.remove(this)
    }

    override fun load(fileHandle: FileHandle) : Music {
        super.load(fileHandle)
        try {
            if (fileHandle.type == FileHandle.INTERNAL) {
                val descriptor = fileHandle.assetFileDescriptor()
                mediaPlayer.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            } else {
                mediaPlayer.setDataSource(fileHandle.file.absolutePath)
            }
        } catch (e: java.lang.Exception) {
            Log.e("Music", e.message, e)
        }
        return this
    }

}