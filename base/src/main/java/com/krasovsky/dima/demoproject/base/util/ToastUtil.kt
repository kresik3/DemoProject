package com.krasovsky.dima.demoproject.base.util

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.krasovsky.dima.demoproject.base.R
import org.jetbrains.anko.margin
import org.jetbrains.anko.padding
import android.support.v4.content.ContextCompat.getSystemService
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View


fun Context.getPositiveToast(message: String): Toast {
    val view = getLayout(R.layout.layout_positive_toast)
    view.findViewById<TextView>(R.id.toast_message).text = message
    return Toast(this).also {
        it.duration = Toast.LENGTH_LONG
        it.view = view
        it.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0,
                getDimenInt(R.dimen.base_space_8x))
    }
}

fun Context.getLayout(layoutId: Int): View {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(layoutId, null)
    return view
}
