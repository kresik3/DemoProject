package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.base.view.activity.BackToolbarActivity
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_COUNT_DISH
import com.krasovsky.dima.demoproject.main.util.applyEnable
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.view.model.DishItemViewModel
import com.krasovsky.dima.demoproject.storage.model.dish.DetailModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import kotlinx.android.synthetic.main.activity_dish.*
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.ZoomViewerDialog
import com.krasovsky.dima.demoproject.main.list.spinner.SpinnerAdapter
import com.krasovsky.dima.demoproject.main.view.model.PaymentViewModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel


private const val KEY_BASKET = "KEY_BASKET"

class PaymentActivity : BackToolbarActivity() {

    override fun getTitleBar() = R.string.toolbar_payment

    private val model: PaymentViewModel by lazy {
        ViewModelProviders.of(this).get(PaymentViewModel::class.java)
    }

    private val priceUtil: PriceUtil by lazy { PriceUtil() }

    companion object {
        fun getInstance(context: Context, data: BasketModel): Intent =
                Intent(context, PaymentActivity::class.java)
                        .apply {
                            putExtra(KEY_BASKET, data)
                        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        model.basket = intent.getParcelableExtra(KEY_BASKET)

        initToolbar()
        observeFields()

    }

    private fun observeFields() {
        observeLoading()
    }

    private fun observeLoading() {
        model.loadingLiveData.observe(this, Observer {
            showProgressDialog()
        }) { hideProgressDialog() }
    }




    override fun onClickBack() {
        onBackPressed()
    }

}
