package com.jumbox.game.android.graphics

/**
 * Created by Jumadi Janjaya date on 18/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Colors {

    var colors = Array<Color?>(2) { null }
    private var percentages = Array<Color?>(2) { null }

    fun parse(content: String) {
        val split = content.split(",")
        colors = Array(split.size) {null}

    }

    fun shader() {

    }

}