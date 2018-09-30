package com.krasovsky.dima.demoproject.base.map

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.model.ObjectTypeContent
import com.krasovsky.dima.demoproject.base.util.PicassoUtil
import com.krasovsky.dima.demoproject.base.util.typeHTML
import com.krasovsky.dima.demoproject.base.util.typeImage
import com.krasovsky.dima.demoproject.base.util.typeString

class ContentViewMapper {
    companion object {
        fun mapInfoObjectArray(inflater: InflaytingModel, item: ObjectTypeContent): View {
            return when (item.type) {
                typeHTML -> mapHtmlView(inflater, item.contant)
                typeString -> mapStringView(inflater, item.contant)
                typeImage -> mapImageView(inflater, item.contant)
                else -> mapStringView(inflater, item.contant)
            }
        }

        fun mapHtmlView(inflater: InflaytingModel, content: String): View {
            val view = LayoutInflater.from(inflater.context)
                    .inflate(R.layout.view_text, inflater.container, false)
                    .findViewById<TextView>(R.id.view_text)
            view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(content)
            }
            return view
        }

        fun mapStringView(inflater: InflaytingModel, content: String): View {
            val view = LayoutInflater.from(inflater.context)
                    .inflate(R.layout.view_text, inflater.container, false)
                    .findViewById<TextView>(R.id.view_text)
            view.text = content
            return view
        }

        fun mapImageView(inflater: InflaytingModel, content: String): View {
            val view = LayoutInflater.from(inflater.context)
                    .inflate(R.layout.view_image, inflater.container, false)
                    .findViewById<ImageView>(R.id.view_image)
            PicassoUtil.getPicasso(content).into(view)
            return view
        }
    }
}