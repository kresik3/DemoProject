package com.krasovsky.dima.demoproject.main.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import android.support.v4.content.ContextCompat.getSystemService
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil


class DetailDishView : FrameLayout {

    private val quantityView: TextView by lazy { findViewById<TextView>(R.id.detail_quantity) }
    private val priceView: TextView by lazy { findViewById<TextView>(R.id.detail_price) }
    private val priceUtil: PriceUtil by lazy { PriceUtil() }

    var quantity = ""
        set(value) {
            field = value
            quantityView.text = value
        }
    var price = 0f
        set(value) {
            field = value
            priceView.text = priceUtil.parseToPrice(value)
        }

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        initView(attr)
    }

    private fun initView(attr: AttributeSet?) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.detail_view, this, true)
    }

}