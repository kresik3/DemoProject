package com.krasovsky.dima.demoproject.base.util.picasso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.krasovsky.dima.demoproject.base.util.picasso.taget.LocalTarget
import com.krasovsky.dima.demoproject.base.util.picasso.taget.RemoteTarget
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream

private const val MAX_CASH_SIZE = 104857600L

class PicassoUtil {
    companion object {
        fun setImagePicasso(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            if (FileUtil.isExistsFile(getNameFile(url), view.context.applicationContext)) {
                loadFromStorage(getNameFile(url), view, listener)
            } else loadFromInternet(url, view, listener)
        }

        private fun loadFromInternet(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            val target = RemoteTarget(getNameFile(url), view, listener)
            Picasso.get()
                    .load(url)
                    .into(target)
        }

        private fun loadFromStorage(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            val target = LocalTarget(view, listener)
            Picasso.get()
                    .load(FileUtil.getImageFile(url, view.context.applicationContext))
                    .into(target)
        }


        private fun getNameFile(url: String): String {
            return url.substringAfterLast(File.separator)
        }

        fun clearOldImages(images: List<String>, context: Context) {
            FileUtil.clearOldImagesFile(images, context.applicationContext)
        }

        fun cashSizeFull(context: Context): Boolean {
            return MAX_CASH_SIZE <= FileUtil.getCashSize(context.applicationContext)
        }
    }
}