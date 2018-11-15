package com.krasovsky.dima.demoproject.main.command.action.activity

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.view.activity.DishActivity
import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.main.view.activity.PaymentActivity
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel

const val KEY_ACTIVITY_PAYMENT = 70
const val KEY_PAYMENT_RESULT = "KEY_PAYMENT_RESULT"

class PaymentAction(val fragment: Fragment, val model: BasketModel) : ActionActivityCommand {

    override fun execute() {
        fragment.startActivityForResult(PaymentActivity.getInstance(fragment.context!!, model), KEY_ACTIVITY_PAYMENT)
    }
}