package com.krasovsky.dima.demoproject.base.util

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import com.krasovsky.dima.demoproject.base.R
import android.view.Gravity
import android.widget.LinearLayout


fun Context.getPositiveToast(message: String): Toast {
    val view = getLayout(R.layout.layout_positive_toast)
    view.findViewById<TextView>(R.id.toast_message).apply { background.alpha = 190 }.text = message
    return Toast(this).also {
        it.duration = Toast.LENGTH_LONG
        it.view = view
        it.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0,
                getDimenInt(R.dimen.base_space_9x))
    }
}

