package com.krasovsky.dima.demoproject.base.util.picasso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.krasovsky.dima.demoproject.base.util.picasso.taget.LocalTarget
import com.krasovsky.dima.demoproject.base.util.picasso.taget.RemoteTarget
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream


class PicassoUtil {
    companion object {
        fun setImagePicasso(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            if (FileUtil.isExistsFile(getNameFile(url), view.context.applicationContext)) {
                loadFromStorage(getNameFile(url), view, listener)
            } else loadFromInternet(url, view, listener)
        }

        private fun loadFromInternet(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            Picasso.get()
                    .load(url)
                    .into(RemoteTarget(getNameFile(url), view, listener))
        }

        private fun loadFromStorage(url: String, view: ImageView, listener: ((Bitmap) -> Unit)? = null) {
            Picasso.get()
                    .load(FileUtil.getImageFile(url, view.context.applicationContext))
                    .into(LocalTarget(view, listener))
        }


        private fun getNameFile(url: String): String {
            return url.substringAfterLast(File.separator)
        }

        fun clearOldImages(images: List<String>, context: Context) {
            FileUtil.clearOldImagesFile(images, context.applicationContext)
        }
    }
}