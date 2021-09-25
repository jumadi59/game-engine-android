package com.jumbox.game.android.util

import android.content.res.Resources
import com.jumbox.game.android.graphics.Color.Companion.toColor
import com.jumbox.game.android.graphics.Color.Companion.toColorInt
import java.util.*

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
object Extension {

    fun Float.dpToPx() : Float {
        return this * density() + 0.5f
    }

    fun Float.dpToPxInt() : Int {
        return (this * density() + 0.5f).toInt()
    }

    fun density() : Float {
        val metrics = Resources.getSystem().displayMetrics
        return metrics.density
    }
    fun String.toBoolean() : Boolean {
        return when {
            toLowerCase(Locale.ROOT) == "true" -> true
            toLowerCase(Locale.ROOT) == "false" -> false
            else -> throw IllegalArgumentException("Unknown boolean")
        }
    }

    fun String.toIntArray() : IntArray {
        return if (isArray()) {
            val s = substring(1, length-1)
            val split = s.split(",")
            val a = IntArray(split.size)
            for ((i , it) in split.withIndex()) {
                a[i] = it.toInt()
            }
            a
        } else intArrayOf()
    }

    fun String.toFloatArray() : FloatArray {
        return if (isArray()) {
            val s = substring(1, length-1)
            val split = s.split(",")
            val a = FloatArray(split.size)
            for ((i , it) in split.withIndex()) {
                a[i] = it.toFloat()
            }
            a
        } else floatArrayOf()
    }

    fun String.toColorArray() : IntArray {
        return if (isArray()) {
            val s = substring(1, length-1)
            val split = s.split(",")
            val a = IntArray(split.size)
            for ((i , it) in split.withIndex()) {
                a[i] = it.toColor().toColorInt()
            }
            a
        } else intArrayOf()
    }

    fun String.isArray() : Boolean {
        return startsWith("[", 0) && startsWith("]", length-1)
    }
}