package com.krasovsky.dima.demoproject.main.command.manager

import android.support.design.widget.BottomNavigationView
import com.krasovsky.dima.demoproject.main.command.action.badge.model.BudgeModel
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionBudgeCommand


class ActionBudgeCommandManager(private val bottomNavigationView: BottomNavigationView,
                                private val budgeModel: BudgeModel) {

    fun executeCommand(command: ActionBudgeCommand) {
        command.execute(bottomNavigationView, budgeModel)
    }

}
