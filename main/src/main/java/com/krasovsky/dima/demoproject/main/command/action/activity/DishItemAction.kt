package com.krasovsky.dima.demoproject.main.command.action.activity

import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand
import com.krasovsky.dima.demoproject.main.view.activity.DishActivity
import com.krasovsky.dima.demoproject.main.view.fragment.DishesFragment
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel

const val KEY_ACTIVITY_DISH = 50
const val KEY_COUNT_DISH = "KEY_COUNT_DISH"

class DishItemAction(val fragment: Fragment, val dish: DishModel) : ActionActivityCommand {

    override fun execute() {
        fragment.startActivityForResult(DishActivity.getInstance(fragment.context!!, dish), KEY_ACTIVITY_DISH)
    }
}