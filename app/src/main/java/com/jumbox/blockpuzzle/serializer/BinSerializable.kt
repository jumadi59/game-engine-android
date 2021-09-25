package com.jumbox.blockpuzzle.serializer

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Created by Jumadi Janjaya date on 29/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
interface BinSerializable {
    @Throws(IOException::class)
    fun write(out: DataOutputStream)

    @Throws(IOException::class)
    fun read(`in`: DataInputStream)
}