package com.krasovsky.dima.demoproject.main.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.renderscript.RenderScript
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.AdapterView
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
import android.text.style.StyleSpan
import android.util.Log
import com.krasovsky.dima.demoproject.base.dialog.alert.ErrorDialog
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.ZoomViewerDialog
import com.krasovsky.dima.demoproject.base.util.RSBlurProcessor
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_NAME_DISH
import com.krasovsky.dima.demoproject.main.list.spinner.SpinnerAdapter
import com.krasovsky.dima.demoproject.main.util.image.sliceHalfBitmap
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


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
    private val blurProcessor: RSBlurProcessor by lazy { RSBlurProcessor(RenderScript.create(this)) }

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
        PicassoUtil.setImagePicasso(dish?.imagePath!!, dish_big_image) { bitmap ->
            blurMainImage(bitmap)
        }
        zoom.register(dish_big_image, dish.imagePath)
        if (dish.description != null) {
            dish_description.text = dish.description
            dish_description.visibility = View.VISIBLE
        }
        btn_count_minus.applyEnable(false)
        initSpinner(dish)
    }

    private fun blurMainImage(bitmap: Bitmap) {
        launch(UI) {
            val leftBitmap = async { blurProcessor.blur(sliceHalfBitmap(bitmap)) }
            val rightBitmap = async { blurProcessor.blur(sliceHalfBitmap(bitmap, false)) }
            left_side_image.setImageBitmap(leftBitmap.await())
            right_side_image.setImageBitmap(rightBitmap.await())
        }
    }

    private fun initSpinner(dish: DishModel) {
        val details = dish.details.map { it.kind }
        val arrayAdapter = SpinnerAdapter(this, R.layout.spinner_item, details)
        setSpinnerEnable(details.size)
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

    private fun setSpinnerEnable(size: Int) {
        val isSingleItem = size == 1
        dish_kind_spinner.isEnabled = !isSingleItem
        spinner_arrow.visibility = if (isSingleItem) View.INVISIBLE else View.VISIBLE
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
        observeErrorBasket()
        observeErrorAdding()
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
                putExtra(KEY_NAME_DISH, model.dish!!.title)
            })
            this.supportFinishAfterTransition()
        })
    }

    private fun observeCount() {
        model.countLiveData.observe(this, Observer {
            dish_count.text = it.toString()
            btn_count_minus.applyEnable(it != 1)
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

    private fun observeErrorBasket() {
        val dialog = model.errorBasket
        dialog.observe(this, Observer { data ->
            if (data == null) return@Observer
            ErrorDialog.Builder().apply {
                initView(this@DishActivity)
                setTitle(data.title)
                setMessage(data.message)
                setPositiveBtn(data.btnOk) {
                    model.refresh()
                    dialog.clear()
                }
                data.btnCancel?.let {
                    setNegativeBtn(it) {
                        this@DishActivity.supportFinishAfterTransition()
                        dialog.clear()
                    }
                }
            }.build().run {
                show(this@DishActivity.supportFragmentManager, "dialog")
            }
        })
    }

    private fun observeErrorAdding() {
        val dialog = model.errorAdding
        dialog.observe(this, Observer { data ->
            if (data == null) return@Observer
            ErrorDialog.Builder().apply {
                initView(this@DishActivity)
                setTitle(data.title)
                setMessage(data.message)
                setPositiveBtn(data.btnOk) {
                    dialog.clear()
                }
            }.build().run {
                show(this@DishActivity.supportFragmentManager, "dialog")
            }
        })
    }

    override fun onClickBack() {
        onBackPressed()
    }

}
