package com.krasovsky.dima.demoproject.main.command.action.fragment

import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand
import com.krasovsky.dima.demoproject.main.view.fragment.DishesFragment

class ShowDishesByCategoryAction(val categoryId: String,
                                 val categoryName: String) : ActionFragmentCommand {

    override fun execute(): Fragment {
        return DishesFragment.getInstance(categoryId, categoryName)
    }
}