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


private const val KEY_DISH = "KEY_DISH"

class DishActivity : BackToolbarActivity() {

    override fun getTitleBar() = model.dish!!.title

    private val model: DishItemViewModel by lazy {
        ViewModelProviders.of(this).get(DishItemViewModel::class.java)
    }

    private val zoom: ZoomViewerDialog by lazy {
        ZoomViewerDialog.Builder(this).build()
    }

    private val priceUtil: PriceUtil by lazy { PriceUtil() }

    companion object {
        fun getInstance(context: Context, data: DishModel): Intent =
                Intent(context, DishActivity::class.java)
                        .apply {
                            putExtra(KEY_DISH, data)
                        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        model.dish = intent.getParcelableExtra(KEY_DISH)

        initToolbar()
        initView()
        initListeners()
        observeFields()
        if (savedInstanceState == null) {
            dish_kind_spinner.setSelection(0)
        }
    }

    private fun initView() {
        val dish = model.dish
        PicassoUtil.setImagePicasso(dish?.imagePath!!, dish_big_image)
        zoom.register(dish_big_image, dish.imagePath)
        if (dish.description != null) {
            dish_description.text = dish.description
            dish_description.visibility = View.VISIBLE
        }
        initSpinner(dish)
    }

    private fun initSpinner(dish: DishModel) {
        val details = dish.details.map { it.kind }
        val arrayAdapter = SpinnerAdapter(this, R.layout.spinner_item, details)
        dish_kind_spinner.apply {
            adapter = arrayAdapter
            isEnabled = details.size != 1
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    invalidateKind(dish.details.filter { it.kind == parent!!.adapter.getItem(position) }[0])
                }
            }
        }
    }

    private fun invalidateKind(dishDetails: DetailModel) {
        model.updateTargetDetail(dishDetails)
    }

    private fun initListeners() {
        btn_count_minus.setOnClickListener { model.count-- }
        btn_count_plus.setOnClickListener { model.count++ }
        btn_add_basket.setOnClickListener { model.addToBasket() }
    }

    private fun observeFields() {
        observeLoading()
        observeSuccess()
        observeCount()
        observeInfo()
        observeTotalPrice()
        observeMinusState()
    }

    private fun observeLoading() {
        model.loadingLiveData.observe(this, Observer {
            showProgressDialog()
        }) { hideProgressDialog() }
    }

    private fun observeSuccess() {
        model.addedSuccess.observe(this, Observer {
            setResult(RESULT_OK, Intent().apply {
                putExtra(KEY_COUNT_DISH, model.count)
            })
            this.supportFinishAfterTransition();
        })
    }

    private fun observeCount() {
        model.countLiveData.observe(this, Observer {
            dish_count.text = it.toString()
        })
    }

    private fun observeInfo() {
        model.infoLiveData.observe(this, Observer {
            val priceString = priceUtil.parseToPrice(it?.second)
            val priceSpan = SpannableString(String.format(getString(R.string.dish_price_format), priceString))
            val quantitySpan = SpannableString(String.format(getString(R.string.dish_quantity_format), it?.first))

            priceSpan.setSpan(StyleSpan(Typeface.BOLD), priceSpan.length - priceString.length,
                    priceSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            quantitySpan.setSpan(StyleSpan(Typeface.BOLD), quantitySpan.length.minus(it?.first?.length
                    ?: 0),
                    quantitySpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            dish_quantity.text = quantitySpan
            dish_price.text = priceSpan
        })
    }

    private fun observeTotalPrice() {
        model.totalPriceLiveData.observe(this, Observer {
            dish_total_price.text = priceUtil.parseToPrice(it!!)
        })
    }

    private fun observeMinusState() {
        model.enableMinusLiveData.observe(this, Observer {
            btn_count_minus.applyEnable(it)
        })
    }

    override fun onClickBack() {
        onBackPressed()
    }

}
