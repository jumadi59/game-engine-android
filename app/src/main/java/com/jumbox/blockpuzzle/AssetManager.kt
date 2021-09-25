package com.jumbox.blockpuzzle

import com.jumbox.game.android.audio.Sound
import com.jumbox.game.android.graphics.Font
import com.jumbox.game.android.graphics.Shape
import com.jumbox.game.android.graphics.ShapeList
import com.jumbox.game.android.graphics.Texture
import com.jumbox.game.android.util.BaseFile
import com.jumbox.game.android.util.Files

/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class AssetManager {

    companion object {
        private var assetManager: AssetManager? = null
        val manager: AssetManager
        get() = if (assetManager != null) assetManager!! else newInstance()

        private fun newInstance() : AssetManager {
            if (assetManager == null)
                assetManager = AssetManager()

            return assetManager!!
        }
    }

    lateinit var shapeBlock: Shape
    lateinit var shapeBlock8: ShapeList

    lateinit var shapeBtnOvalDark: Shape
    lateinit var shapeBtnOvalLight: Shape

    lateinit var shapeBtnRoundRed: Shape
    lateinit var shapeBtnRoundTea: Shape
    lateinit var shapeBtnRoundYellow: Shape
    lateinit var shapeBtnRoundBlue: Shape


    lateinit var dropSound: Sound
    lateinit var takeSound: Sound
    lateinit var invalidDrop: Sound
    lateinit var effectVanishSound: Sound
    lateinit var fontMacan: Font
    lateinit var fontStangkorv: Font
    lateinit var mahkotaTexture: Texture

    lateinit var iconGame: Texture
    lateinit var iconShare: Texture
    lateinit var iconLapboard: Texture
    lateinit var iconReset: Texture
    lateinit var iconPaused: Texture
    lateinit var iconRate: Texture
    lateinit var iconBack: Texture

    val cubeTextures = ArrayList<Texture>()
    var totalLoad = 0

    fun splashLoad() {
        fontMacan = load("macan_regular.ttf", Font::class.java)
        fontStangkorv = load("Stangkorv.ttf", Font::class.java)
    }

    fun load() {
        shapeBlock = load("block_round.shape", Shape::class.java)
        shapeBlock8 = load("block_8.shape", ShapeList::class.java)

        shapeBtnOvalDark = load("oval_dark.shape", Shape::class.java)
        shapeBtnOvalLight = load("oval_light.shape", Shape::class.java)

        shapeBtnRoundRed = load("round_gradient_red.shape", Shape::class.java)
        shapeBtnRoundTea = load("round_gradient_tea.shape", Shape::class.java)
        shapeBtnRoundYellow = load("round_gradient_yellow.shape", Shape::class.java)
        shapeBtnRoundBlue = load("round_gradient_blue.shape", Shape::class.java)

        dropSound = load("piece_drop.mp3", Sound::class.java)
        takeSound = load("take_pieces.mp3", Sound::class.java)
        invalidDrop = load("invalid_drop.mp3", Sound::class.java)
        effectVanishSound = load("effect_vanish.mp3", Sound::class.java)

        mahkotaTexture = load("mahkota.png", Texture::class.java)
        iconGame = load("icon_game.png", Texture::class.java)
        iconShare = load("icon_share.png", Texture::class.java)
        iconLapboard = load("icon_lapboard.png", Texture::class.java)
        iconReset = load("icon_reset.png", Texture::class.java)
        iconPaused = load("icon_pause.png", Texture::class.java)
        iconRate = load("icon_rate.png", Texture::class.java)
        iconBack = load("icon_back.png", Texture::class.java)

        cubeTextures.add(load("cube-tea.png", Texture::class.java))
        cubeTextures.add(load("cube-red.png", Texture::class.java))
        cubeTextures.add(load("cube-yellow.png", Texture::class.java))
        cubeTextures.add(load("cube-blue.png", Texture::class.java))
    }

    private fun <T : BaseFile> load(fileName: String, klass: Class<T>) : T {
        return try {
            val instance = klass.newInstance()
            synchronized(instance) {
                instance.load(Files.internal(fileName))
            }
            totalLoad++
            instance
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}