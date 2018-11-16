package com.krasovsky.dima.demoproject.base.util

import android.content.Context
import android.support.v4.content.ContextCompat


fun Context.getCompatColor(colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.getDimenInt(resId: Int): Int {
    return (resources.getDimension(resId) / resources.displayMetrics.density).toInt()
}

fun Context.getDimenFloat(resId: Int): Float {
    return resources.getDimension(resId) / resources.displayMetrics.density
}