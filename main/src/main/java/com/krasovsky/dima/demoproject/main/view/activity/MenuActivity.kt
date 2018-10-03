package com.krasovsky.dima.demoproject.main.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.repository.manager.AppStorageManager
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initView()
    }

    private fun initView() {
       setupWithNavController(bottom_navigation, findNavController(my_nav_host_fragment))
    }
}
