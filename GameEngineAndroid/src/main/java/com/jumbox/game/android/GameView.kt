package com.jumbox.game.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.*


/**
 * Created by Jumadi Janjaya date on 17/05/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class GameView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, GestureDetector.OnGestureListener {

    private val gesture: GestureDetector = GestureDetector(context, this)
    private var gameThread: GameThread? = null
    var engine = Engine.getInstance()

    init {
        engine.initialize(this)
        holder.addCallback(this)
        isFocusable = true

    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Engine.game.width = width
        Engine.game.height = height
        engine.createSurfaceView(surfaceHolder)
        if (gameThread?.isRunning == false) resume()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    fun pause() {
        engine.pause()
        try {
            gameThread?.isRunning = false
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        gameThread = GameThread(holder, engine)
        gameThread!!.isRunning = true
        gameThread!!.start()
        engine.resume()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        pause()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        engine.input.touch(x, y)
        return if (gesture.onTouchEvent(event)) true
        else when (event.action  and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> engine.input.down()
            MotionEvent.ACTION_MOVE -> engine.input.moved()
            MotionEvent.ACTION_UP -> engine.input.up()
            MotionEvent.ACTION_CANCEL -> engine.input.cancel()
            else -> false
        }
    }

    override fun onDown(event: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(event: MotionEvent?) {
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(event: MotionEvent?, event1: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return engine.input.scroll(distanceX, distanceY)
    }

    override fun onLongPress(event: MotionEvent?) {}

    override fun onFling(event: MotionEvent?, event1: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return engine.input.fling(velocityX, velocityY)
    }

    class GameThread(private val holder: SurfaceHolder, private val engine: Engine) : Thread() {

        var isRunning = false
        var isDrawing = true

        override fun run() {
            var beginTime: Long
            var timeDiff: Long
            var framesSkipped: Int
            var sleepTime = 0.0f

            while (isRunning) {
                var canvas: Canvas? = null
                beginTime = System.currentTimeMillis()
                try {
                    canvas = holder.lockCanvas()
                    synchronized(holder) {
                        framesSkipped = 0
                        engine.update(sleepTime / Engine.FPS)
                        if (isDrawing) engine.render(canvas)

                        timeDiff = System.currentTimeMillis() - beginTime
                        sleepTime = Engine.FRAME_PERIOD - timeDiff
                        if (sleepTime > 0) {
                            try {
                                sleep(sleepTime.toLong())
                            } catch (e: InterruptedException) {}
                        }
                    }
                    while (sleepTime < 0 && framesSkipped < Engine.MAX_FRAME_SKIPS) {
                        engine.update(sleepTime / Engine.MAX_FPS)
                        sleepTime += Engine.MAX_FRAME_SKIPS
                        framesSkipped++
                    }
                    if (System.currentTimeMillis() - beginTime >= 1000) {
                        Log.d("GameView", "FPS > sleepTime: $sleepTime framesSkipped: $framesSkipped")
                    }
                } finally {
                    if (canvas != null) holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

}