package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.MenuItem
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand
import com.krasovsky.dima.demoproject.main.command.view.IActionCommand
import com.krasovsky.dima.demoproject.main.view.activity.controller.NavigationMenuController
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.COMMAND_BACK
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.IToolbarCommand
import com.krasovsky.dima.demoproject.main.view.model.NavigationViewModel
import kotlinx.android.synthetic.main.activity_menu.*

private const val KEY_MENU_SELECTED = "KEY_MENU_SELECTED"

class MenuActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        IActionCommand, IToolbarCommand {

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
            model.newFragment.onShowFragment()
        } else {
            transition.add(R.id.navigation_container, model.newFragment, model.newFragment.javaClass.name)
        }
        transition.commit()
    }

    override fun sendCommand(command: ActionFragmentCommand) {
        openFragment(controller.setActionCommand(command))
    }

    override fun sendCommand(command: ActionActivityCommand) {
        controller.setActionCommand(command)
    }

    override fun sendCommand(command: Int) {
        when (command) {
            COMMAND_BACK -> onBackPressedControler()
        }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(KEY_MENU_SELECTED, bottom_navigation.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        controller.updateFragmentsAfterConfigChanged(supportFragmentManager)
        bottom_navigation.selectedItemId = savedInstanceState.getInt(KEY_MENU_SELECTED)
    }
}
