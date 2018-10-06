package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.view.activity.controller.NavigationMenuController
import com.krasovsky.dima.demoproject.main.view.model.NavigationViewModel
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val model: NavigationViewModel by lazy {
        ViewModelProviders.of(this).get(NavigationViewModel::class.java)
    }

    private val controller: NavigationMenuController by lazy {
        model.controller
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initView()
    }

    private fun initView() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        if (!controller.isEmpty()) return

        bottom_navigation.selectedItemId = bottom_navigation.selectedItemId
    }

    override fun onNavigationItemSelected(@NonNull menuItem: MenuItem): Boolean {
        val itemId = menuItem.itemId
        val menu = bottom_navigation.menu
        if (itemId == menu.getItem(0).itemId) {
            openFragment(controller.getDeliveryFragment())
        } else if (itemId == menu.getItem(1).itemId) {
            openFragment(controller.getMenuFragment())
        } else if (itemId == menu.getItem(2).itemId) {
            openFragment(controller.getDiscountFragment())
        } else if (itemId == menu.getItem(3).itemId) {
            openFragment(controller.getAboutFragment())
        } else
            return false
        return true
    }

    private fun openFragment(model: NavigationMenuController.NavigationFragmentModel?) {
        if (model == null) return

        val transition = supportFragmentManager.beginTransaction()
        if (model.oldFragment != null && supportFragmentManager.findFragmentByTag(model.oldFragment!!.javaClass.name) != null) {
            model.oldFragment!!.onHideFragment()
            transition.hide(model.oldFragment!!)
        }
        if (supportFragmentManager.findFragmentByTag(model.newFragment.javaClass.name) != null) {
            transition.show(model.newFragment)
            model.oldFragment!!.onShowFragment()
        } else {
            transition.add(R.id.navigation_container, model.newFragment, model.newFragment.javaClass.name)
        }
        transition.commit()
    }

    override fun onBackPressed() {
        if (!onBackPressedControler()) {
            super.onBackPressed()
        }
    }

    private fun onBackPressedControler(): Boolean {
        if (!controller.canBackPressed()) return false
        val tagBack = controller.onBackPressed(supportFragmentManager)
        onClickAfterBack(tagBack)

        return true
    }

    private fun onClickAfterBack(tag: String) {
        val menu = bottom_navigation.menu
        bottom_navigation.selectedItemId = menu.getItem(controller.getIndexState(tag)).itemId
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        controller.updateFragmentsAfterConfigChanged(supportFragmentManager)
    }
}
