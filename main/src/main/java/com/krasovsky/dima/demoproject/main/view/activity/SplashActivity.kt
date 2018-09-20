package com.krasovsky.dima.demoproject.main.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.krasovsky.dima.demoproject.main.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startHandler()
    }

    private fun startHandler() {
        launch(UI) {
            val result = async { delay(3000) }.await()

            startActivity<MenuActivity>()
            SplashActivity@finish()
        }

    }
}
