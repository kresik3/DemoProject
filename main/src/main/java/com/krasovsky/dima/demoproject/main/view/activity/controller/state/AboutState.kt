package com.krasovsky.dima.demoproject.main.view.activity.controller.state

import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu
import com.krasovsky.dima.demoproject.main.view.fragment.AboutFragment
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment

class AboutState : BaseStateMenu() {

    override fun getRoot(): BaseMenuFragment {
        if (rootFragment == null) {
            rootFragment = AboutFragment()
        }
        return rootFragment
    }
}
