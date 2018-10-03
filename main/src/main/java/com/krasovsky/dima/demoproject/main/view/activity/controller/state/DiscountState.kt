package com.krasovsky.dima.demoproject.main.view.activity.controller.state

import com.krasovsky.dima.demoproject.main.view.activity.controller.state.base.BaseStateMenu
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment
import com.krasovsky.dima.demoproject.main.view.fragment.DiscountFragment


class DiscountState : BaseStateMenu() {

    override fun getRoot(): BaseMenuFragment {
        if (rootFragment == null) {
            rootFragment = DiscountFragment()
        }
        return rootFragment
    }
}
