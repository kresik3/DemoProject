package com.krasovsky.dima.demoproject.main.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import com.krasovsky.dima.demoproject.base.dialog.alert.ErrorDialog
import com.krasovsky.dima.demoproject.base.view.activity.BackToolbarActivity
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_PAYMENT_RESULT
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.util.validate.PaymentValidator
import com.krasovsky.dima.demoproject.main.util.validate.model.ValidationModel
import com.krasovsky.dima.demoproject.main.view.model.PaymentViewModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import kotlinx.android.synthetic.main.activity_payment.*

private const val KEY_BASKET = "KEY_BASKET"

class PaymentActivity : BackToolbarActivity() {

    override fun getTitleBar() = R.string.toolbar_payment

    private val model: PaymentViewModel by lazy {
        ViewModelProviders.of(this).get(PaymentViewModel::class.java)
    }

    private val validator: PaymentValidator by lazy { PaymentValidator() }
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

        initView()
        initToolbar()
        initListeners()
        observeFields()
    }

    private fun initView() {
        total_payment.text = priceUtil.parseToPrice(model.basket.totalPrice)
    }

    private fun initListeners() {
        btn_payment.setOnClickListener { payment() }
    }

    private fun payment() {
        if (validateData()) {
            val name = name_edit.text.toString()
            val telephone = "+375" + telephone_code_edit.text.toString() + telephone_edit.text.toString()
            val address = address_edit.text.toString()
            val comment = comment_edit.text.toString()
            model.payment(name, telephone, address, comment)
        }
    }

    private fun validateData(): Boolean {
        val name = ValidationModel(name_layout, name_edit, model.getNameValidateParams())
        val code = ValidationModel(telephone_layout, telephone_code_edit, model.getTelephoneCodeValidateParams())
        val telephone = ValidationModel(telephone_layout, telephone_edit, model.getTelephoneValidateParams())
        val address = ValidationModel(address_layout, address_edit, model.getAddressValidateParams())

        var result = validator.validateName(name)
        result = result and validator.validateTelephone(code, telephone)
        result = result and validator.validateAddress(address)
        return result
    }

    private fun observeFields() {
        observeLoading()
        observeSuccess()
        observeErrorBasket()
    }

    private fun observeLoading() {
        model.loadingLiveData.observe(this, Observer {
            showProgressDialog()
        }) { hideProgressDialog() }
    }


    private fun observeErrorBasket() {
        val dialog = model.error
        dialog.observe(this, Observer { data ->
            if (data == null) return@Observer
            ErrorDialog.Builder().apply {
                initView(this@PaymentActivity)
                setTitle(data.title)
                setMessage(data.message)
                setPositiveBtn(data.btnOk) {
                    dialog.clear()
                }
            }.build().run {
                show(this@PaymentActivity.supportFragmentManager, "dialog")
            }
        })
    }

    private fun observeSuccess() {
        model.success.observe(this, Observer { model ->
            setResult(RESULT_OK, Intent().apply {
                putExtra(KEY_PAYMENT_RESULT, model)
            })
            finish()
        })
    }

    override fun onClickBack() {
        onBackPressed()
    }

}
