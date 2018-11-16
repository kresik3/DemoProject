package com.krasovsky.dima.demoproject.main.util.image

import android.graphics.Bitmap


fun sliceHalfBitmap(bitmap: Bitmap, isLeft: Boolean = true): Bitmap {
    val halfWidth = bitmap.width / 2
    val bmDownRightPartial = Bitmap.createBitmap(halfWidth, bitmap.height, Bitmap.Config.ARGB_8888)
    val pixels5 = IntArray(halfWidth * bitmap.height)
    if (isLeft) {
        bitmap.getPixels(pixels5, 0, halfWidth, 0, 0, halfWidth, bitmap.height)
    } else {
        bitmap.getPixels(pixels5, 0, halfWidth, halfWidth, 0, halfWidth, bitmap.height)
    }
    bmDownRightPartial.setPixels(pixels5, 0, halfWidth, 0, 0, halfWidth, bitmap.height)
    return bmDownRightPartial
}