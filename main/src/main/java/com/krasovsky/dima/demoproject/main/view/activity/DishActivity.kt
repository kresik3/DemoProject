package com.krasovsky.dima.demoproject.main.view.activity

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
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.view.model.DishItemViewModel
import com.krasovsky.dima.demoproject.storage.model.dish.DetailModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import kotlinx.android.synthetic.main.activity_dish.*


private const val KEY_DISH = "KEY_DISH"

class DishActivity : BackToolbarActivity() {

    override fun getTitleBar() = R.string.toolbar_dish

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
        initToolbar()

        model.dish = intent.getParcelableExtra(KEY_DISH)
        initView()
    }

    private fun initView() {
        val dish = model.dish
        PicassoUtil.setImagePicasso(dish?.imagePath!!, dish_big_image)
        dish_title.text = dish.title
        if (dish.description != null) {
            dish_description.text = dish.description
            dish_description.visibility = View.VISIBLE
        }
        initSpenner(dish)

    }

    private fun initSpenner(dish: DishModel) {
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dish.details.map { it.kind })
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dish_kind_spinner.apply {
            adapter = arrayAdapter
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    invalidateKind(dish.details.filter { it.kind == parent!!.adapter.getItem(position) }[0])
                }
            }
        }
    }

    private fun invalidateKind(dish: DetailModel) {
        dish_quantity.text = dish.quantity
        dish_price.text = priceUtil.parseToPrice(dish.price)
        dish_count.text = "0"
    }

    override fun onClickBack() {
        onBackPressed()
    }

}
