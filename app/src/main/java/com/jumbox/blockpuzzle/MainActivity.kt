package com.jumbox.blockpuzzle

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jumbox.blockpuzzle.game.BlockGame
import com.jumbox.game.android.Game
import com.jumbox.game.android.GameCallback
import com.jumbox.game.android.GameView

class MainActivity : AppCompatActivity(), GameCallback {

    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_main)
        gameView = findViewById(R.id.game_view)
        gameView.engine.gameCallback = this
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onBackPressed() {
        if (gameView.engine.back()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        gameView.engine.destroy()
        super.onDestroy()
    }

    override fun exit() { finish() }

    override fun startGame(): Game = BlockGame()

}