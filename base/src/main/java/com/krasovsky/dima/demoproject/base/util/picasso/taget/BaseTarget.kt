package com.krasovsky.dima.demoproject.base.util.picasso.taget

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Target
import com.krasovsky.dima.demoproject.base.R


abstract class BaseTarget(val view: ImageView) : Target {

    init {
        view.tag = this
    }

    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
        view.setImageBitmap(BitmapFactory.decodeResource(view.context.resources, R.drawable.bg_error_picasso))
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        view.setImageBitmap(BitmapFactory.decodeResource(view.context.resources, R.drawable.bg_loading_picasso))
    }

}