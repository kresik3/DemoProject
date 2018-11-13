package com.krasovsky.dima.demoproject.base.util.picasso.taget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class LocalTarget(view: ImageView, val listener: ((Bitmap) -> Unit)? = null) : BaseTarget(view) {

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        onBitmapLoadedLocally(view, bitmap, listener)
    }

    private fun onBitmapLoadedLocally(view: ImageView, bitmap: Bitmap?, listener: ((Bitmap) -> Unit)? = null) {
        view.setImageBitmap(bitmap)
        if (bitmap != null) listener?.invoke(bitmap)
    }
}