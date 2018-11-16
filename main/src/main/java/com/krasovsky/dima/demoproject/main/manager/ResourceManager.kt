package com.krasovsky.dima.demoproject.main.manager

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.util.getDimenFloat
import com.krasovsky.dima.demoproject.main.util.getDimenInt
import org.jetbrains.anko.textColor

class ResourceManager {
    companion object {
        fun applyParams(view: View) {
            when (view) {
                is ImageView -> {
                    applyToImageView(view)
                }
                is TextView -> {
                    applyToTextView(view)
                }
            }
        }

        private fun applyToTextView(view: TextView) {
            (view.layoutParams as LinearLayout.LayoutParams)
                    .apply { bottomMargin = view.context.getDimenInt(R.dimen.base_space) }
            view.textColor = ContextCompat.getColor(view.context, com.krasovsky.dima.demoproject.base.R.color.activeColor)
            view.textSize = view.context.getDimenFloat(R.dimen.content_info_object)
        }

        private fun applyToImageView(view: ImageView) {

        }
    }
}