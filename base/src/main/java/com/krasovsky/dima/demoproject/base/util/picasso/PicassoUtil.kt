package com.krasovsky.dima.demoproject.base.util.picasso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream


class PicassoUtil {
    companion object {
        fun setImagePicasso(url: String, view: ImageView) {
            if (FileUtil.isExistsFile(getNameFile(url), view.context.applicationContext)) {
                loadFromStorage(getNameFile(url), view)
            } else loadFromInternet(url, view)
        }

        private fun loadFromInternet(url: String, view: ImageView) {
            Picasso.get()
                    .load(url)
                    .into(getTarget(getNameFile(url), view))
        }

        private fun getTarget(nameFile: String, view: ImageView): Target {
            return object : Target {
                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    view.setImageBitmap(null)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    view.setImageBitmap(null)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap == null) return
                    Thread(Runnable {
                        val file = FileUtil.createImageFile(nameFile, view.context.applicationContext)
                                ?: return@Runnable
                        FileOutputStream(file).use {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                            it.flush()
                            it.close()
                        }
                    }).start()
                    view.setImageBitmap(bitmap)
                }
            }
        }

        private fun loadFromStorage(url: String, view: ImageView) {
            Picasso.get()
                    .load(FileUtil.getImageFile(url, view.context.applicationContext))
                    .into(view)
        }

        private fun getNameFile(url: String): String {
            return url.substringAfterLast(File.separator)
        }

        fun clearOldImages(images: List<String>, context: Context) {
            FileUtil.clearOldImagesFile(images, context.applicationContext)
        }
    }
}