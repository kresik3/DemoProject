package com.krasovsky.dima.demoproject.base.dialog.alert

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.Log
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.dialog.alert.base.BaseDialog

class ErrorDialog : BaseDialog() {

    class Builder : BaseDialog.Builder() {
        override fun getPalette(context: Context): ColorPalette {
            return with(context) {
                ColorPalette(
                        ContextCompat.getColor(this, android.R.color.holo_red_light),
                        ContextCompat.getColor(this, R.color.darkTextColor),
                        ContextCompat.getColor(this, R.color.accentColor),
                        ContextCompat.getColor(this, R.color.accentColor)
                )
            }
        }
    }
}