package com.jumbox.game.android.util

import android.content.res.AssetManager
import java.io.File

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/

class Files {

    companion object {
        var localDir: File? = null
        var assets: AssetManager? = null

        fun internal(fileName: String) : FileHandle {
            return FileHandle(assets!!, fileName, FileHandle.INTERNAL)
        }

        fun external(fileName: String) : FileHandle {
            return FileHandle(File(fileName), FileHandle.EXTERNAL)
        }

        fun local(fileName: String) : FileHandle {
            return FileHandle(File(localDir, fileName), FileHandle.EXTERNAL)
        }

        fun rootApp(fileName: String) : FileHandle {
            val path = localDir!!.absoluteFile.parentFile
            return FileHandle(File(path, fileName), FileHandle.EXTERNAL)
        }

        fun res() {

        }
    }
}