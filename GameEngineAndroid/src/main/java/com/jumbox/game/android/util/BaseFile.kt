package com.jumbox.game.android.util

/**
 * Created by Jumadi Janjaya date on 19/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class BaseFile {

    fun reload() {
        FileHandle.get(this)?.let { load(it) }
    }

    open fun load(fileHandle: FileHandle): BaseFile {
        FileHandle.add(this, fileHandle)
        return this
    }
}