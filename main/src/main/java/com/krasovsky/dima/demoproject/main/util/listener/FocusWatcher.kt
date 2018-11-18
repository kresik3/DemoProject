package com.krasovsky.dima.demoproject.main.util.listener

import android.text.Editable
import android.text.TextWatcher
import android.view.View

class FocusWatcher(private val nextView: View, private val endSize: Int) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length == endSize) {
            nextView.requestFocus()
        }
    }
}