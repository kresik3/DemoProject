package com.krasovsky.dima.demoproject.main.util

import android.content.Context


fun Context.getDimenInt(resId: Int): Int {
    return (resources.getDimension(resId) / resources.displayMetrics.density).toInt()
}

fun Context.getDimenFloat(resId: Int): Float {
    return resources.getDimension(resId) / resources.displayMetrics.density
}