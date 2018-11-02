package com.krasovsky.dima.demoproject.main.command.interfaces;


import android.support.design.widget.BottomNavigationView;

import com.krasovsky.dima.demoproject.main.command.action.badge.model.BudgeModel;

public interface ActionBudgeCommand {
    void execute(BottomNavigationView bottomNavigationView, BudgeModel model);
}
