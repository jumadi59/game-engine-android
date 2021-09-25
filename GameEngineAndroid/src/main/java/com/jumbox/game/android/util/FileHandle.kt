package com.jumbox.game.android.util

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


/**
 * Created by Jumadi Janjaya date on 22/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class FileHandle(val file: File, val type: Int) {

    companion object {
        const val INTERNAL = 0
        const val EXTERNAL = 1
        private val fileHandles = ArrayList<Map>()

        fun <T : BaseFile> add(baseFile: T, fileHandle: FileHandle) {
            val find = fileHandles.find { it.baseFile == baseFile }
            if (find != null) remove(baseFile)

            fileHandles.add(Map(baseFile, fileHandle))
        }

        fun <T : BaseFile> get(baseFile: T) : FileHandle? {
            return fileHandles.find { it.baseFile == baseFile }?.fileHandle
        }

        fun <T: BaseFile> remove(baseFile: T) {
            val find = fileHandles.find { it.baseFile == baseFile }
            fileHandles.remove(find!!)
        }
    }

    private var assetManager: AssetManager? = null

    constructor(fileName: String, type: Int) : this(File(fileName), type)
    constructor(assetManager: AssetManager, fileName: String, type: Int) : this(fileName, type) {
        this.assetManager = assetManager
    }

    fun write(append: Boolean) : FileOutputStream {
        if (type == INTERNAL) throw IllegalArgumentException("Cannot write to an internal file: $file")
        if (!file.exists()) file.parentFile?.mkdirs()
        if (!file.isFile) file.createNewFile()
        return try {
            FileOutputStream(file, append)
        } catch (ex: Exception) {
            if (file.isDirectory) throw IllegalArgumentException("Cannot open a stream to a directory: $file ($type)", ex)
            throw IllegalArgumentException("Error writing file: $file ($type)", ex)
        }
    }

    fun assetFileDescriptor(): AssetFileDescriptor {
        return assetManager!!.openFd(file.path)
    }

    fun asset() = assetManager!!

    fun reed() : InputStream {
        return if ( type == INTERNAL) {
            assetManager?.open(file.path)?: throw IllegalArgumentException("File not found: $file ($type)")
        } else try {
            FileInputStream(file)
        } catch (ex: Exception) {
            if (file.isDirectory) throw IllegalArgumentException("Cannot open a stream to a directory: $file ($type)", ex)
            throw IllegalArgumentException("Error reading file: $file ($type)", ex)
        }
    }

    fun delete() {
        if (type == INTERNAL) throw IllegalArgumentException("Cannot delete to an internal file: $file")

        file.delete()
    }

    fun exists() = file.exists()

    fun mkdirs() = file.mkdirs()

    data class Map(val baseFile: BaseFile, val fileHandle: FileHandle)
}