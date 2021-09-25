package com.jumbox.game.android.animation

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Created by Jumadi Janjaya date on 20/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
abstract class Interpolation {

    companion object {
        val linear = object : Interpolation() {
            override fun apply(speed: Float): Float  = speed
        }
        
        val quadratic = Quadratic()
        val cubic = Cubic()
        val quartic = Quartic()
        val quintic = Quintic()
        val sinusoidal = Sinusoidal()
        val exponential = Exponential()
        val circular = Circular()
        val elastic = Elastic()
        val back = Back()
        val bounce = Bounce()
        
    }

    abstract fun apply(speed: Float) : Float

    open fun apply(from: Float, to: Float, speed: Float) : Float {
        return from + ((to - from) * apply(speed))
    }
    
    class Quadratic {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float  = speed * speed
        }
        
        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return speed * (2f - speed)
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return 0.5f * speed * speed
                return -0.5f * ((speed - 1f) * (speed - 2f) - 1f)
            }
        }

        val bezier = object : Interpolation() {
            override fun apply(speed: Float): Float = 0.5f * 2 * speed * (1 - speed) + speed * speed
        }
    }
    
    class Cubic {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return speed * speed * speed
            }

        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f + ((speed - 1f) * speed * speed)
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return 0.5f * speed * speed * speed
                return 0.5f * ((speed - 2f) * speed * speed + 2f)
            }
        }
    }
    
    class Quartic {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return speed * speed * speed * speed
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f - ((speed - 1f) * speed * speed * speed)
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return 0.5f * speed * speed * speed * speed
                return -0.5f * ((speed - 2f) * speed * speed * speed - 2f)
            }
        }
    }

    class Quintic {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return speed * speed * speed * speed * speed
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f + ((speed - 1f) * speed * speed * speed * speed)
            }

        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return 0.5f * speed * speed * speed * speed * speed
                return 0.5f * ((speed - 2f) * speed * speed * speed * speed + 2f)
            }
        }
    }

    class Sinusoidal {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f - cos(speed * Math.PI / 2f).toFloat()
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return sin(speed * Math.PI / 2f).toFloat()
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 0.5f * (1f - cos(Math.PI * speed).toFloat())
            }
        }
    }

    class Exponential {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return if(speed == 0f) 0f else 1024.0.pow(speed - 1.0).toFloat()
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return if (speed == 1f) 1f else 1f - 2.0.pow(-10.0 * speed).toFloat()
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if (speed == 0f) return 0f
                if (speed == 1f) return 1f
                if ((speed * 2f) < 1f) return 0.5f * 1024.0.pow(speed - 1.0).toFloat()
                return 0.5f * ((-2.0).pow(-10f * (speed - 1.0)) + 2f).toFloat()
            }
        }
    }

    class Circular {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f - sqrt(1.0 - speed * speed).toFloat()
            }
        }

        val out = object :Interpolation() {
            override fun apply(speed: Float): Float {
                return sqrt(1.0 - ((speed - 1f) * speed)).toFloat()
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return -0.5f * (sqrt(1.0 - speed * speed) - 1).toFloat()
                return 0.5f * (sqrt(1.0 - (speed - 2f) * speed) + 1f).toFloat()
            }
        }
    }

    class Elastic {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if (speed == 0f) return 0f
                if (speed == 1f) return 1f
                return ((-2.0).pow(10f * (speed - 1.0)) * sin((speed - 0.1f) * (2f * Math.PI) / 0.4f)).toFloat()
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if (speed == 0f) return 0f
                if (speed == 1f) return 1f
                return (2.0.pow(-10.0 * speed) * sin((speed - 0.1f) * (2f * Math.PI) / 0.4f) + 1f).toFloat()
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return (-0.5f * 2.0.pow(10f * (speed - 1.0)) * sin((speed - 0.1f) * (2f * Math.PI) / 0.4f)).toFloat()
                return (2.0.pow(-10f * (speed - 1.0)) * sin((speed - 0.1f) * (2f * Math.PI) / 0.4f) * 0.5f + 1f).toFloat()
            }
        }
    }

    class Back {
        private val s = 1.70158f
        private val s2 = 2.5949095f

        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return speed * speed * ((s + 1f) * speed - s)
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return (speed - 1f) * speed * ((s + 1f) * speed + s) + 1f
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if ((speed * 2f) < 1f) return 0.5f * (speed * speed * ((s2 + 1f) * speed - s2))
                return 0.5f * ((speed - 2f) * speed * ((s2 + 1f) * speed + s2) + 2f)
            }
        }
    }

    class Bounce {
        val iin = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return 1f - out.apply(1f - speed)
            }
        }

        val out = object : Interpolation() {
            override fun apply(speed: Float): Float {
                return when {
                    speed < (1f / 2.75f) -> 7.5625f * speed * speed
                    speed < (2f / 2.75f) -> 7.5625f * (speed - (1.5f / 2.75f)) * speed + 0.75f
                    speed < (2.5f / 2.75f) -> 7.5625f * (speed - (2.25f / 2.75f)) * speed + 0.9375f
                    else -> 7.5625f * (speed - (2.625f / 2.75f)) * speed + 0.984375f
                }
            }
        }

        val inOut = object : Interpolation() {
            override fun apply(speed: Float): Float {
                if (speed < 0.5f) return iin.apply(speed * 2f) * 0.5f
                return out.apply(speed * 2f - 1f) * 0.5f + 0.5f
            }
        }
    }
}