package com.krasovsky.dima.demoproject.main.command.action.badge

import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.ViewGroup
import android.widget.TextView

import com.krasovsky.dima.demoproject.main.command.action.badge.model.BudgeModel
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionBudgeCommand


class AddBasketBadgeAction(private val value: Int) : BaseBasketBadgeAction(), ActionBudgeCommand {

    init {
        typeBadge = TypeBadge.BASKET
    }

    override fun execute(bottomNavigationView: BottomNavigationView, model: BudgeModel) {
        if (value == 0) return
        calculatePosition()
        setBudge(bottomNavigationView, model)
    }

    private fun setBudge(bottomNavigationView: BottomNavigationView, model: BudgeModel) {
        val menu = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val menuItem = menu.getChildAt(position) as ViewGroup
        model.applyBudgeToItem(menuItem)
        applyBackground(model)
        setCount(model)
    }

    private fun setCount(budgeModel: BudgeModel) {
        val view = budgeModel.getBadgeCounterView()
        val targetValueString = view.text.toString()
        val targetValue = if (targetValueString.isEmpty()) 0 else Integer.valueOf(targetValueString)
        val newValue = targetValue + value
        view.text = newValue.toString()
        if (newValue == 0)
            hideBudgeView(view)
        else
            showBudgeView(view)
    }

    private fun applyBackground(budgeModel: BudgeModel) {
        val view = budgeModel.getBadgeCounterView()
        if (value < 10) {
            view.background = budgeModel.circleBackground
        } else {
            view.background = budgeModel.baseBackground
        }
    }

}
