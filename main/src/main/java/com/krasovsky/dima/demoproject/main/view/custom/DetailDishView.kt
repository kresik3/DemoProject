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
import android.widget.LinearLayout.HORIZONTAL
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import org.jetbrains.anko.*
import kotlin.properties.Delegates


class DetailDishView(context: Context) {

    val view: LinearLayout

    private var quantityTextView: TextView by Delegates.notNull()
    private var priceTextView: TextView by Delegates.notNull()

    var quantity = ""
        set(value) {
            field = value
            quantityTextView.text = value
        }
    var price = ""
        set(value) {
            field = value
            priceTextView.text = value
        }

    init {
        view = initView(context, null)
    }

    private fun initView(context: Context, attr: AttributeSet?): LinearLayout {
        return with(context) {
            linearLayout {
                orientation = HORIZONTAL
                quantityTextView = textView { }
                textView {
                    maxLines = 1
                    setText(R.string.separator)
                }.lparams { weight = 1f }
                priceTextView = textView { }
            }
        }

    }

}