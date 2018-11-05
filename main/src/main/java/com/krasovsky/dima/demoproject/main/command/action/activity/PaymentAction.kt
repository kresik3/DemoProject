package com.krasovsky.dima.demoproject.main.command.action.activity

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.view.activity.DishActivity
import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.main.view.activity.PaymentActivity
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel


class PaymentAction(val context: Context, val model: BasketModel) : ActionActivityCommand {

    override fun execute() {
        context.startActivity(PaymentActivity.getInstance(context, model))
    }
}