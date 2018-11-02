package com.krasovsky.dima.demoproject.main.command.action.badge

import android.view.View

import com.krasovsky.dima.demoproject.main.view.activity.MenuActivity

abstract class BaseBasketBadgeAction {

    var typeBadge: TypeBadge? = null
    var position: Int = 0

    fun calculatePosition() {
        if (typeBadge === TypeBadge.BASKET) {
            position = MenuActivity.NavigationPositionItem.basket
        }
    }

    fun hideBudgeView(root: View) {
        root.visibility = View.INVISIBLE
    }

    fun showBudgeView(root: View) {
        root.visibility = View.VISIBLE
    }

}
