package com.krasovsky.dima.demoproject.main.view.activity.controller.state.base

import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.view.fragment.BasketFragment
import com.krasovsky.dima.demoproject.main.view.fragment.MenuFragment


class BasketState : BaseStateMenu() {

    override fun getRootFragment(): BaseMenuFragment {
        if (root == null) {
            root = BasketFragment()
        }
        return root!!
    }
}
