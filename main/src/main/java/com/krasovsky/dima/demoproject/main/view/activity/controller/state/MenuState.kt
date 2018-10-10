package com.krasovsky.dima.demoproject.main.view.activity.controller.state

import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu
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
