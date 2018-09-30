package com.krasovsky.dima.demoproject.base.util

import android.content.Context
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class PicassoUtil {
    companion object {
        fun getPicasso(url: String): RequestCreator =
                Picasso.get().load(url)
    }
}