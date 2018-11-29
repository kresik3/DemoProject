package com.krasovsky.dima.demoproject.base.util.picasso

import android.content.Context
import android.util.Log
import java.io.File

private const val DIR_IMAGES = "ImagesPicasso"

class FileUtil {
    companion object {

        internal fun createImageFile(fileName: String, context: Context): File? {
            if (isExistsFile(fileName, context)) return null
            return getImageFile(fileName, context)
        }

        internal fun getImageFile(fileName: String, context: Context): File {
            return File(createDirImages(context), fileName)
        }

        private fun createDirImages(context: Context): File {
            return File(context.filesDir, DIR_IMAGES).apply { if (!exists()) mkdirs() }
        }

        internal fun isExistsFile(fileName: String, context: Context): Boolean {
            return File(createDirImages(context), fileName).exists()
        }

        internal fun clearOldImagesFile(images: List<String>, context: Context) {
            createDirImages(context).listFiles { dir: File?, name: String? ->
                !images.contains(name)
            }.forEach { file: File? -> file?.delete() }
        }

        internal fun getCashSize(context: Context): Long {
            return folderSize(createDirImages(context))
        }

        private fun folderSize(directory: File): Long {
            var length: Long = 0
            directory.listFiles().forEach {
                length += if (it.isFile) it.length() else folderSize(it)
            }
            return length
        }
    }
}