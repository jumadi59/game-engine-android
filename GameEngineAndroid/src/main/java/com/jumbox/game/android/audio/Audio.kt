package com.jumbox.game.android.audio

import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.FileHandle

/**
 * Created by Jumadi Janjaya date on 19/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class Audio : BaseFile() {

    abstract fun loadComplete()
    abstract fun play()
    abstract fun pause()
    abstract fun resume()
    open fun dispose() {
        FileHandle.remove(this)
    }

}