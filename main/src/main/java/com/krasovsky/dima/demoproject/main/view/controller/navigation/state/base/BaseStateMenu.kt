package com.krasovsky.dima.demoproject.main.view.activity.controller.state.base

import android.support.v4.app.FragmentManager
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import java.util.ArrayList


abstract class BaseStateMenu {

    protected var root: BaseMenuFragment? = null
    protected var stack = ArrayList<BaseMenuFragment>()

    val lastFragment: BaseMenuFragment
        get() = if (stack.size == 0) getRootFragment() else stack[stack.size - 1]

    val lastRemoveFragment: BaseMenuFragment?
        get() = if (stack.size == 0) null else stack.removeAt(stack.size - 1)

    fun addFragment(fragment: BaseMenuFragment) {
        stack.add(fragment)
    }

    fun updateFragments(fragmentManager: FragmentManager) {
        val newStack = ArrayList<BaseMenuFragment>()
        for (fragment in stack) {
            val newFragment = fragmentManager.findFragmentByTag(fragment.javaClass.name) as BaseMenuFragment?
            if (newFragment != null) newStack.add(newFragment)
        }
        stack = newStack
        root = fragmentManager.findFragmentByTag(root!!.javaClass.name) as BaseMenuFragment
    }

    fun canBackPressed(): Boolean {
        return stack.size > 0
    }

    protected abstract fun getRootFragment(): BaseMenuFragment
}
