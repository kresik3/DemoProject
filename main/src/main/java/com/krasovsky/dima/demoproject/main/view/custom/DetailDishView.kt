package com.krasovsky.dima.demoproject.main.view.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import android.widget.LinearLayout.HORIZONTAL
import org.jetbrains.anko.*
import kotlin.properties.Delegates


class DetailDishView(context: Context) {

    val view: LinearLayout

    private var typeTextView: TextView by Delegates.notNull()
    private var priceTextView: TextView by Delegates.notNull()

    var type: String? = ""
        set(value) {
            field = value
            typeTextView.text = value
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
                typeTextView = textView {
                    textSize = context.resources.getDimension(R.dimen.dish_detail_info)
                }
                textView {
                    textSize = context.resources.getDimension(R.dimen.dish_detail_info)
                    maxLines = 1
                    setText(R.string.separator)
                }.lparams { weight = 1f }
                priceTextView = textView {
                    textSize = context.resources.getDimension(R.dimen.dish_detail_info)
                    setTextColor(ContextCompat.getColor(context, R.color.priceColor))
                }
            }
        }

    }

}