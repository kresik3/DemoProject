package com.krasovsky.dima.demoproject.main.command.action.activity

import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.view.activity.DishActivity
import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.main.command.action.model.DishActionModel


const val KEY_ACTIVITY_DISH = 50
const val KEY_COUNT_DISH = "KEY_COUNT_DISH"
const val KEY_NAME_DISH = "KEY_NAME_DISH"

class DishItemAction(val fragment: Fragment, val model: DishActionModel) : ActionActivityCommand {

    override fun execute() {
        val intent = DishActivity.getInstance(fragment.context!!, model.dish)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.context as AppCompatActivity,
                model.options[0], model.options[1])
        fragment.startActivityForResult(intent, KEY_ACTIVITY_DISH, options.toBundle())
        fragment.startActivityForResult(intent, KEY_ACTIVITY_DISH, options.toBundle())
    }
}