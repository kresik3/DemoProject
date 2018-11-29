package com.krasovsky.dima.demoproject.base.util.picasso.taget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.krasovsky.dima.demoproject.base.R
import com.squareup.picasso.Picasso

class LocalTarget(view: ImageView, val listener: ((Bitmap) -> Unit)? = null) : BaseTarget(view) {

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        onBitmapLoadedLocally(view, bitmap, listener)
    }

    private fun onBitmapLoadedLocally(view: ImageView, bitmap: Bitmap?, listener: ((Bitmap) -> Unit)? = null) {
        view.setImageBitmap(bitmap)
        if (bitmap != null) listener?.invoke(bitmap)
    }
}