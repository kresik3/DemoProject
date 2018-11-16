package com.krasovsky.dima.demoproject.base.dialog.alert

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.Log
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.dialog.alert.base.BaseDialog
import com.krasovsky.dima.demoproject.base.util.getCompatColor

class ErrorDialog : BaseDialog() {

    class Builder : BaseDialog.Builder() {
        override fun getPalette(context: Context): ColorPalette {
            return with(context) {
                ColorPalette(
                        getCompatColor(android.R.color.holo_red_light),
                        getCompatColor(R.color.darkTextColor),
                        getCompatColor(R.color.accentColor),
                        getCompatColor(R.color.accentColor)
                )
            }
        }
    }
}