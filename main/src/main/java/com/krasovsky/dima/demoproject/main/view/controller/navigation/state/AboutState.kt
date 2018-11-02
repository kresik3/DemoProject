package com.krasovsky.dima.demoproject.main.view.controller.navigation.state

import com.krasovsky.dima.demoproject.main.view.fragment.AboutFragment
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu

class AboutState : BaseStateMenu() {

    override fun getRootFragment(): BaseMenuFragment {
        if (root == null) {
            root = AboutFragment()
        }
        return root!!
    }
}
