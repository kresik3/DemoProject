package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.view.model.SplashViewModel
import com.krasovsky.dima.demoproject.repository.manager.LocalManager
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val model: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observeFields()
    }

    private fun observeFields() {
        observeInitializing()
    }

    private fun observeInitializing() {
        model.initializingData.observe(this, Observer {
            startActivity<MenuActivity>()
            SplashActivity@finish()
        })
    }
}
