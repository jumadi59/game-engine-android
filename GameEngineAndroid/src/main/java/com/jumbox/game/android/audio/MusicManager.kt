package com.jumbox.game.android.audio

/**
 * Created by Jumadi Janjaya date on 19/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class MusicManager {

    private var musics = ArrayList<Music>()

    fun add(music: Music) : MusicManager {
        musics.add(music)
            return this
    }

    fun remove(music: Music) {
        musics.remove(music)
    }
}