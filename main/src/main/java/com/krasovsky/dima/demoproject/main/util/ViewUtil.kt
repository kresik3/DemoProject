package com.krasovsky.dima.demoproject.main.util

import android.view.View


fun View.applyEnable(isEnable: Boolean?) {
    alpha = if (isEnable == false) 0.5f else 1f
    this.isEnabled = isEnable ?: true
}
