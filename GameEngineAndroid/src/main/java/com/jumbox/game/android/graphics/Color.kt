package com.jumbox.game.android.graphics

import kotlin.math.roundToInt

/**
 * Created by Jumadi Janjaya date on 20/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Color {
    companion object {
        val WHITE = Color(1f, 1f, 1f, 1f)
        val BLACK = Color(0f, 0f, 0f, 1f)

        val CLEAR = Color(0f, 0f, 0f, 0f)
        val TRANSPARENT = Color(1f, 1f, 1f, 0f)

        val BLUE = Color(0f, 0f, 1f, 1f)
        val RED = Color(1f, 0f, 0f, 1f)
        val GREEN = Color(0f, 1f, 0f, 1f)
        val YELLOW = Color(1f, 1f ,0f ,1f)
        val CYAN = Color(0f, 1f ,1f ,1f)

        fun String.toColor(): Color {
            return this.toColor(Color())
        }

        fun String.toColor(color: Color): Color {
            var h = this
            h = if (h[0] == '#') h.substring(1) else h
            color.r = h.substring(0, 2).toInt(16) / 255f
            color.g = h.substring(2, 4).toInt(16) / 255f
            color.b = h.substring(4, 6).toInt(16) / 255f
            color.a = if (h.length != 8) 1f else h.substring(6, 8).toInt(16) / 255f
            return color
        }

        fun alpha(alpha: Float): Int {
            return (alpha * 255.0f).toInt()
        }

        fun luminanceAlpha(luminance: Float, alpha: Float): Int {
            return (luminance * 255.0f).toInt() shl 8 or (alpha * 255).toInt()
        }

        fun Color.rgb(): Int {
            val red = (r * 255).roundToInt() shl 16 and 0x00FF0000
            val green = (g * 255).roundToInt() shl 8 and 0x0000FF00
            val blue = (b * 255).roundToInt() and 0x000000FF

            return -0x1000000 or red or green or blue
        }

        fun Color.argb(): Int {
            val alpha = (a * 255).roundToInt() shl 24 and -0x1000000
            val red = (r * 255).roundToInt() shl 16 and 0x00FF0000
            val green = (g * 255).roundToInt() shl 8 and 0x0000FF00
            val blue = (b * 255).roundToInt() and 0x000000FF

            return alpha or red or green or blue
        }

        fun Color.toColorInt() : Int {
           return if (a == 1.0f) this.rgb()
            else this.argb()
        }

        fun Color.rgbToColor(value: Int) {
            r = (value and 0x00FF0000 ushr 16) / 255f
            g = (value and 0x0000FF00 ushr 8) / 255f
            b = (value and 0x000000FF) / 255f
        }

        fun Color.argbToColor(value: Int) {
            this.a = (value and -0x1000000 ushr 24) / 255f
            this.r = (value and 0x00FF0000 ushr 16) / 255f
            this.g = (value and 0x0000FF00 ushr 8) / 255f
            this.b = (value and 0x000000FF) / 255f
        }
    }

    var r = 0f
    var g = 0f
    var b = 0f
    var a = 0f

    constructor()
    constructor(color: Color) {
        this.r = color.r
        this.g = color.g
        this.b = color.b
        this.a = color.a
    }

    constructor(argb: Int) {
        argbToColor(argb)
    }

    constructor(hexColor: String) {
        hexColor.toColor(this)
    }

    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
        clamp()
    }

    fun mul(color: Color): Color {
        this.r *= color.r
        this.g *= color.g
        this.b *= color.b
        this.a *= color.a
        return clamp()
    }

    fun mul(value: Float): Color {
        this.r *= value
        this.g *= value
        this.b *= value
        this.a *= value
        return clamp()
    }

    fun add(color: Color): Color {
        this.r += color.r
        this.g += color.g
        this.b += color.b
        this.a += color.a
        return clamp()
    }

    fun sub(color: Color): Color {
        this.r -= color.r
        this.g -= color.g
        this.b -= color.b
        this.a -= color.a
        return clamp()
    }

    private fun clamp(): Color {
        if (r < 0) r = 0f else if (r > 1) r = 1f
        if (g < 0) g = 0f else if (g > 1) g = 1f
        if (b < 0) b = 0f else if (b > 1) b = 1f
        if (a < 0) a = 0f else if (a > 1) a = 1f
        return this
    }

    operator fun set(r: Float, g: Float, b: Float, a: Float): Color {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
        return clamp()
    }

    fun set(argb: Int): Color {
        argbToColor(argb)
        return this
    }

    fun add(r: Float, g: Float, b: Float, a: Float): Color {
        this.r += r
        this.g += g
        this.b += b
        this.a += a
        return clamp()
    }

    fun sub(r: Float, g: Float, b: Float, a: Float): Color {
        this.r -= r
        this.g -= g
        this.b -= b
        this.a -= a
        return clamp()
    }

    fun mul(r: Float, g: Float, b: Float, a: Float): Color {
        this.r *= r
        this.g *= g
        this.b *= b
        this.a *= a
        return clamp()
    }

    fun lerp(target: Color, t: Float): Color {
        r += t * (target.r - r)
        this.g += t * (target.g - this.g)
        this.b += t * (target.b - this.b)
        this.a += t * (target.a - this.a)
        return clamp()
    }

    fun lerp(r: Float, g: Float, b: Float, a: Float, t: Float): Color {
        this.r += t * (r - this.r)
        this.g += t * (g - g)
        this.b += t * (b - b)
        this.a += t * (a - a)
        return clamp()
    }

    fun premultiplyAlpha(): Color {
        r *= a
        g *= a
        b *= a
        return this
    }

    fun copy() : Color {
        return Color(this)
    }

    fun isClear() : Boolean {
        return a == 0f && r == 0f && g == 0f && b == 0f
    }

    override fun toString(): String {
        var value = Integer.toHexString((255 * r).toInt() shl 24 or ((255 * g).toInt() shl 16) or ((255 * b).toInt() shl 8) or (255 * a).toInt())
        while (value.length < 8) value = "0$value"
        return value
    }

}