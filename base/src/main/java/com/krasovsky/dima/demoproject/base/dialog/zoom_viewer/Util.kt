package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.R.attr.y
import android.R.attr.x
import android.content.Context
import android.graphics.Point
import android.support.v7.app.AppCompatActivity


class Util {

    companion object {
        fun getScreenSize(context: Context): Point {
            val display = (context as AppCompatActivity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }
    }
}