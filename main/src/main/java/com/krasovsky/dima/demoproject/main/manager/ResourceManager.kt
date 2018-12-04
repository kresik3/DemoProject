package com.krasovsky.dima.demoproject.main.manager

import android.support.v4.content.ContextCompat
import android.text.util.Linkify
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
            with(view) {
                applyToView(this)
                textColor = this.context.getCompatColor(com.krasovsky.dima.demoproject.base.R.color.darkTextColor)
                textSize = this.context.getDimenFloat(R.dimen.content_info_object)
                applyLinksParams(this)
            }
        }

        private fun applyToImageView(view: ImageView) {
            applyToView(view)
        }

        private fun applyToView(view: View) {
            (view.layoutParams as LinearLayout.LayoutParams)
                    .apply { bottomMargin = view.context.getDimenInt(R.dimen.base_space) }
        }

        private fun applyLinksParams(view: TextView) {
            view.let {
                it.setLinkTextColor(ContextCompat.getColor(it.context, com.krasovsky.dima.demoproject.base.R.color.linkColor))
                Linkify.addLinks(it, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
                Linkify.addLinks(it, Linkify.ALL)
            }
        }
    }
}