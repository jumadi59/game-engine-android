package com.jumbox.blockpuzzle

import java.text.NumberFormat

/**
 * Created by Jumadi Janjaya date on 01/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
fun Int.formatString() : String {
    return NumberFormat.getInstance().format(this).replace(".", ",")
}