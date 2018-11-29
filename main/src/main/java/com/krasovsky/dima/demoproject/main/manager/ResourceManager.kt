package com.krasovsky.dima.demoproject.main.manager

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.util.getCompatColor
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.base.util.getDimenFloat
import com.krasovsky.dima.demoproject.base.util.getDimenInt
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
            applyToView(view)
            view.textColor = view.context.getCompatColor(com.krasovsky.dima.demoproject.base.R.color.darkTextColor)
            view.textSize = view.context.getDimenFloat(R.dimen.content_info_object)
        }

        private fun applyToImageView(view: ImageView) {
            applyToView(view)
        }

        private fun applyToView(view: View) {
            (view.layoutParams as LinearLayout.LayoutParams)
                    .apply { bottomMargin = view.context.getDimenInt(R.dimen.base_space) }
        }
    }
}