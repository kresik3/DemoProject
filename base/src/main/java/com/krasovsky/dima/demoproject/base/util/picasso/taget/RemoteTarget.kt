package com.krasovsky.dima.demoproject.base.util.picasso.taget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.krasovsky.dima.demoproject.base.util.picasso.FileUtil
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.FileOutputStream

class RemoteTarget(val nameFile: String, view: ImageView,
                   val listener: ((Bitmap) -> Unit)? = null) : BaseTarget(view) {

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        onBitmapLoadedUri(nameFile, view, bitmap, listener)
    }

    private fun onBitmapLoadedUri(nameFile: String, view: ImageView, bitmap: Bitmap?, listener: ((Bitmap) -> Unit)? = null) {
        view.setImageBitmap(bitmap)
        if (bitmap == null) return

        listener?.invoke(bitmap)
        Thread(Runnable {
            val file = FileUtil.createImageFile(nameFile, view.context.applicationContext)
                    ?: return@Runnable
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.flush()
                it.close()
            }
        }).start()
    }
}