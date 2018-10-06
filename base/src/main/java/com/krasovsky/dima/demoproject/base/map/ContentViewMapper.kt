package com.krasovsky.dima.demoproject.base.map

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.model.ObjectTypeContent
import com.krasovsky.dima.demoproject.base.util.PicassoUtil
import com.krasovsky.dima.demoproject.base.util.typeHTML
import com.krasovsky.dima.demoproject.base.util.typeImage
import com.krasovsky.dima.demoproject.base.util.typeString

class ContentViewMapper {
    companion object {
        fun mapInfoObjectArray(context: Context, item: ObjectTypeContent): View {
            return when (item.type) {
                typeHTML -> mapHtmlView(context, item.contant)
                typeString -> mapStringView(context, item.contant)
                typeImage -> mapImageView(context, item.contant)
                else -> mapStringView(context, item.contant)
            }
        }

        fun mapHtmlView(context: Context, content: String): View {
            val view = TextView(context).apply { layoutParams = getBaseParams() }
            view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(content)
            }
            return view
        }

        fun mapStringView(context: Context, content: String): View {
            return TextView(context).apply {
                layoutParams = getBaseParams()
                text = content
            }
        }

        fun mapImageView(context: Context, content: String): View {
            val view = ImageView(context)
            PicassoUtil.getPicasso(content).into(view)
            return view
        }

        private fun getBaseParams() = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    }
}