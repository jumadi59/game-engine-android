package com.jumbox.game.android.audio

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.util.Log

/**
 * Created by Jumadi Janjaya date on 19/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class SoundManager {

    var soundPool: SoundPool = if (Build.VERSION.SDK_INT >= 21) {
        val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(Sound.MAX_STREAMS).build()
    } else {
        SoundPool(Sound.MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
    }
    private var sounds = ArrayList<Sound>()

    init {
        soundPool.setOnLoadCompleteListener { _, i, _ ->
            sounds.find { it.id == i }?.loadComplete()
            Log.d("SoundManager", "loadSuccess $i")
        }
    }

    fun add(sound: Sound) : SoundPool {
        sounds.add(sound)
        return soundPool
    }

    fun release() {
        soundPool.release()
    }

    fun remove(sound: Sound) {
        sounds.remove(sound)
    }

}