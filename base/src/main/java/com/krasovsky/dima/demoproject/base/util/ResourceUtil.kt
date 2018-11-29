package com.krasovsky.dima.demoproject.base.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun Context.getLayout(layoutId: Int, container: ViewGroup? = null): View {
    return if (container == null) {
        LayoutInflater.from(this).inflate(layoutId, null)
    } else LayoutInflater.from(this).inflate(layoutId, container, false)
}

fun Context.getCompatColor(colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.getDimenInt(resId: Int): Int {
    return (resources.getDimension(resId) / resources.displayMetrics.density).toInt()
}

fun Context.getDimenFloat(resId: Int): Float {
    return resources.getDimension(resId) / resources.displayMetrics.density
}