package com.krasovsky.dima.demoproject.main.command.action.badge

import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.ViewGroup
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.command.action.badge.model.BudgeModel
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionBudgeCommand

class CleanBasketBadgeAction : BaseBasketBadgeAction(), ActionBudgeCommand {
    init {
        typeBadge = TypeBadge.BASKET
    }

    override fun execute(bottomNavigationView: BottomNavigationView, model: BudgeModel) {
        calculatePosition()
        setBudge(bottomNavigationView, model)
    }

    private fun setBudge(bottomNavigationView: BottomNavigationView, model: BudgeModel) {
        val menu = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val menuItem = menu.getChildAt(position) as ViewGroup
        model.applyBudgeToItem(menuItem)
        setCount(model)
    }

    private fun setCount(budgeModel: BudgeModel) {
        val view = budgeModel.getBadgeCounterView()
        view.text = 0.toString()
        hideBudgeView(view)
    }

}
