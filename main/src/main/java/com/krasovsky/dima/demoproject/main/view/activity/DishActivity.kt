package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.base.view.fragment.activity.BackToolbarActivity
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_COUNT_DISH
import com.krasovsky.dima.demoproject.main.util.applyEnable
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.view.model.DishItemViewModel
import com.krasovsky.dima.demoproject.storage.model.dish.DetailModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import kotlinx.android.synthetic.main.activity_dish.*


private const val KEY_DISH = "KEY_DISH"

class DishActivity : BackToolbarActivity() {

    override fun getTitleBar() = model.dish!!.title

    private val model: DishItemViewModel by lazy {
        ViewModelProviders.of(this).get(DishItemViewModel::class.java)
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
        if (dish.description != null) {
            dish_description.text = dish.description
            dish_description.visibility = View.VISIBLE
        }
        initSpinner(dish)
    }

    private fun initSpinner(dish: DishModel) {
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dish.details.map { it.kind })
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dish_kind_spinner.apply {
            adapter = arrayAdapter
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
        observeSuccess()
        observeCount()
        observePrice()
        observeQuantity()
        observeTotalPrice()
        observeMinusState()
    }

    private fun observeSuccess() {
        model.addedSuccess.observe(this, Observer {
            setResult(RESULT_OK, Intent().apply {
                putExtra(KEY_COUNT_DISH, model.count)
            })
            this.finish()
        })
    }

    private fun observeCount() {
        model.countLiveData.observe(this, Observer {
            dish_count.text = it.toString()
        })
    }

    private fun observePrice() {
        model.priceLiveData.observe(this, Observer {
            dish_price.text = priceUtil.parseToPrice(it!!)
        })
    }

    private fun observeQuantity() {
        model.quantityLiveData.observe(this, Observer {
            dish_quantity.text = it
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
