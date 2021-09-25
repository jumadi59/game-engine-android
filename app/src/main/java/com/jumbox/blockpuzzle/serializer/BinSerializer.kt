package com.jumbox.blockpuzzle.serializer

import java.io.*

/**
 * Created by Jumadi Janjaya date on 29/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class BinSerializer {
    companion object {
        private val HEADER = "block".encodeToByteArray()
        private const val VERSION = 2

        @Throws(IOException::class)
        fun serialize(serializable: BinSerializable, output: OutputStream?) {
            val out = DataOutputStream(output)
            try {
                out.write(HEADER)
                out.writeInt(VERSION)
                serializable.write(out)
            } finally {
                try {
                    out.close()
                } catch (ignored: IOException) {
                }
            }
        }

        @Throws(IOException::class)
        fun deserialize(serializable: BinSerializable, input: InputStream?) {
            val `in` = DataInputStream(input)
            try {
                // Read the HEADER and the VERSION (checks)
                val savedBuffer = ByteArray(HEADER.size)
                `in`.readFully(savedBuffer)
                if (!savedBuffer.contentEquals(HEADER)
                ) throw IOException("Invalid saved header found.")
                val savedVersion: Int = `in`.readInt()
                if (savedVersion != VERSION) {
                    throw IOException("Invalid saved version found. Should be $VERSION, not $savedVersion")
                }
                serializable.read(`in`)
            } finally {
                try {
                    `in`.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }
}