package com.krasovsky.dima.demoproject.main.view.activity.controller

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Pair

import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.AboutState
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.DeliveryState
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.DiscountState
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.MenuState
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu

import java.lang.ref.WeakReference
import java.util.ArrayList


class NavigationMenuController {


    private var tagLastFragment = ""

    private val stack = ArrayList<BaseStateMenu>()
    private val trash = ArrayList<BaseStateMenu>()

    val isEmpty: Boolean
        get() = stack.size == 0

    val aboutFragment: NavigationFragmentModel?
        get() {
            val oldFragment = oldFragment
            val state = findState(AboutState::class.java.name)
            if (state != null) {
                stack.add(state)
            } else {
                stack.add(AboutState())
            }
            val newFragment = newFragment
            return createResponse(oldFragment, newFragment)
        }

    val deliveryFragment: NavigationFragmentModel?
        get() {
            val oldFragment = oldFragment
            val state = findState(DeliveryState::class.java.name)
            if (state != null) {
                stack.add(state)
            } else {
                stack.add(DeliveryState())
            }
            val newFragment = newFragment
            return createResponse(oldFragment, newFragment)
        }

    val discountFragment: NavigationFragmentModel?
        get() {
            val oldFragment = oldFragment
            val state = findState(DiscountState::class.java.name)
            if (state != null) {
                stack.add(state)
            } else {
                stack.add(DiscountState())
            }
            val newFragment = newFragment
            return createResponse(oldFragment, newFragment)
        }

    val menuFragment: NavigationFragmentModel?
        get() {
            val oldFragment = oldFragment
            val state = findState(MenuState::class.java.name)
            if (state != null) {
                stack.add(state)
            } else {
                stack.add(MenuState())
            }
            val newFragment = newFragment
            return createResponse(oldFragment, newFragment)
        }

    private val oldFragment: BaseMenuFragment?
        get() {
            val state = if (stack.size == 0) null else stack[stack.size - 1]
            return state?.lastFragment
        }

    private val newFragment: BaseMenuFragment
        get() {
            val state = stack[stack.size - 1]
            return state.lastFragment
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

    private fun createResponse(oldFragment: BaseMenuFragment?, newFragment: BaseMenuFragment): NavigationFragmentModel? {
        if (newFragment.javaClass.name == tagLastFragment) return null
        tagLastFragment = newFragment.javaClass.name
        return NavigationFragmentModel(oldFragment, newFragment)
    }

    private fun getBackPressedFragment(state: BaseStateMenu): BaseMenuFragment? {
        return state.lastRemoveFragment
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

    private fun hideFragment(fragmentManager: FragmentManager, fragment: BaseMenuFragment?) {
        if (fragment == null) return
        val transition = fragmentManager.beginTransaction()
        transition.hide(fragment)
        transition.commit()
    }

    inner class NavigationFragmentModel internal constructor(var oldFragment: BaseMenuFragment, var newFragment: BaseMenuFragment)

}
