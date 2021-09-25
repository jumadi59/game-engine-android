package com.jumbox.blockpuzzle.game

import com.jumbox.game.android.screen.Scene

/**
 * Created by Jumadi Janjaya date on 28/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
interface BlockGameListener {

    fun gameOver(score: Int, game: BlockGame)

    fun switchFragment(scene: Scene, game: BlockGame)

}