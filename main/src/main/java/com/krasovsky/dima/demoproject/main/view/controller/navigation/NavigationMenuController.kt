package com.krasovsky.dima.demoproject.main.view.controller.navigation

import android.support.v4.app.FragmentManager

import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand
import com.krasovsky.dima.demoproject.main.command.manager.ActionCommandManager
import com.krasovsky.dima.demoproject.main.view.activity.MenuActivity
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BasketState
import com.krasovsky.dima.demoproject.main.view.controller.navigation.state.AboutState
import com.krasovsky.dima.demoproject.main.view.controller.navigation.state.DeliveryState
import com.krasovsky.dima.demoproject.main.view.controller.navigation.state.DiscountState
import com.krasovsky.dima.demoproject.main.view.controller.navigation.state.MenuState

import java.util.ArrayList


class NavigationMenuController {

    private val manager = ActionCommandManager()

    private var tagLastFragment = ""

    private val stack = ArrayList<BaseStateMenu>()
    private val trash = ArrayList<BaseStateMenu>()

    fun isEmpty(): Boolean {
        return stack.size == 0
    }

    fun canBackPressed(): Boolean {
        return if (stack.size == 1) {
            stack[stack.size - 1].canBackPressed()
        } else
            stack.size > 1
    }

    fun onBackPressed(fragmentManager: FragmentManager): String {
        val lastState = stack[stack.size - 1]
        if (lastState.canBackPressed()) {
            removeFragment(fragmentManager, getBackPressedFragment(lastState))
            return lastState.javaClass.name
        } else {
            trash.add(stack.removeAt(stack.size - 1))
            hideFragment(fragmentManager, lastState.lastFragment)
            return stack[stack.size - 1].javaClass.name
        }
    }

    fun onCancelPressed(fragmentManager: FragmentManager) {
        val lastState = stack[stack.size - 1]
        while (lastState.canBackPressed()) {
            removeFragment(fragmentManager, getBackPressedFragment(lastState))
        }
        showFragment(fragmentManager, lastState.lastFragment)
    }

    fun getAboutFragment(): NavigationFragmentModel? {
        val oldFragment = getOldFragment()
        val state = findState(AboutState::class.java.name)
        if (state != null) {
            stack.add(state)
        } else {
            stack.add(AboutState())
        }
        val newFragment = getNewFragment()
        return createResponse(oldFragment, newFragment)
    }

    fun getDeliveryFragment(): NavigationFragmentModel? {
        val oldFragment = getOldFragment()
        val state = findState(DeliveryState::class.java.name)
        if (state != null) {
            stack.add(state)
        } else {
            stack.add(DeliveryState())
        }
        val newFragment = getNewFragment()
        return createResponse(oldFragment, newFragment)
    }

    fun getDiscountFragment(): NavigationFragmentModel? {
        val oldFragment = getOldFragment()
        val state = findState(DiscountState::class.java.name)
        if (state != null) {
            stack.add(state)
        } else {
            stack.add(DiscountState())
        }
        val newFragment = getNewFragment()
        return createResponse(oldFragment, newFragment)
    }

    fun getMenuFragment(): NavigationFragmentModel? {
        val oldFragment = getOldFragment()
        val state = findState(MenuState::class.java.name)
        if (state != null) {
            stack.add(state)
        } else {
            stack.add(MenuState())
        }
        val newFragment = getNewFragment()
        return createResponse(oldFragment, newFragment)
    }

    fun getBasketFragment(): NavigationFragmentModel? {
        val oldFragment = getOldFragment()
        val state = findState(BasketState::class.java.name)
        if (state != null) {
            stack.add(state)
        } else {
            stack.add(BasketState())
        }
        val newFragment = getNewFragment()
        return createResponse(oldFragment, newFragment)
    }

    private fun createResponse(oldFragment: BaseMenuFragment?, newFragment: BaseMenuFragment): NavigationFragmentModel? {
        if (newFragment.javaClass.name == tagLastFragment) return null
        tagLastFragment = newFragment.javaClass.name
        return NavigationFragmentModel(oldFragment, newFragment)
    }

    private fun getBackPressedFragment(state: BaseStateMenu): BaseMenuFragment? {
        return state.lastRemoveFragment
    }

    private fun getOldFragment(): BaseMenuFragment? {
        val state = if (stack.size == 0) null else stack[stack.size - 1]
        return state?.lastFragment
    }

    private fun getNewFragment(): BaseMenuFragment {
        val state = stack[stack.size - 1]
        return state.lastFragment
    }

    private fun findState(name: String): BaseStateMenu? {
        var state = removeStateInList(stack, name)
        if (state == null) state = removeStateInList(trash, name)
        return state
    }

    private fun removeStateInList(list: ArrayList<BaseStateMenu>, name: String): BaseStateMenu? {
        val state = getStateInList(list, name)
        if (state != null) {
            list.remove(state)
        }
        return state
    }

    private fun getStateInList(list: ArrayList<BaseStateMenu>, name: String): BaseStateMenu? {
        for (state in list) {
            if (state.javaClass.name == name) {
                return state
            }
        }
        return null
    }

    fun updateFragmentsAfterConfigChanged(fragmentManager: FragmentManager) {
        for (state in stack) {
            state.updateFragments(fragmentManager)
        }
        for (state in trash) {
            state.updateFragments(fragmentManager)
        }
    }

    private fun removeFragment(fragmentManager: FragmentManager, fragment: BaseMenuFragment?) {
        if (fragment == null) return
        val transition = fragmentManager.beginTransaction()
        transition.remove(fragment)
        transition.commit()
    }

    private fun showFragment(fragmentManager: FragmentManager, fragment: BaseMenuFragment?) {
        if (fragment == null) return
        val transition = fragmentManager.beginTransaction()
        tagLastFragment = fragment::class.java.name
        fragment.onShowFragment()
        transition.show(fragment)
        transition.commit()
    }

    private fun hideFragment(fragmentManager: FragmentManager, fragment: BaseMenuFragment?) {
        if (fragment == null) return
        val transition = fragmentManager.beginTransaction()
        fragment.onHideFragment()
        transition.hide(fragment)
        transition.commit()
    }

    fun getIndexState(tag: String): Int {
        return when (tag) {
            MenuState::class.java.name -> MenuActivity.NavigationPositionItem.menu
            DeliveryState::class.java.name -> MenuActivity.NavigationPositionItem.delivery
            DiscountState::class.java.name -> MenuActivity.NavigationPositionItem.discount
            AboutState::class.java.name -> MenuActivity.NavigationPositionItem.about
            BasketState::class.java.name -> MenuActivity.NavigationPositionItem.basket
            else -> MenuActivity.NavigationPositionItem.menu
        }
    }

    fun setActionCommand(command: ActionFragmentCommand): NavigationFragmentModel? {
        val newFragment = manager.executeCommand(command) as BaseMenuFragment
        val lastState = stack[stack.size - 1]
        val oldFragment = lastState.lastFragment
        lastState.addFragment(newFragment)
        return createResponse(oldFragment, newFragment)
    }

    fun setActionCommand(command: ActionActivityCommand) {
        manager.executeCommand(command)
    }

    inner class NavigationFragmentModel
    internal constructor(var oldFragment: BaseMenuFragment?, var newFragment: BaseMenuFragment)
}
