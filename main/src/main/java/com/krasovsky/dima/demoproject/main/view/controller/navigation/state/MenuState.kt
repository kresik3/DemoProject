package com.krasovsky.dima.demoproject.main.view.controller.navigation.state

import com.krasovsky.dima.demoproject.main.view.controller.navigation.state.base.BaseStateMenu
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.view.fragment.MenuFragment


class MenuState : BaseStateMenu() {

    override fun getRootFragment(): BaseMenuFragment {
        if (root == null) {
            root = MenuFragment()
        }
        return root!!
    }
}
