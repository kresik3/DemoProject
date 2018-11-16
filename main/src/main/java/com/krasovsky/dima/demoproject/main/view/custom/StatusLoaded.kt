package com.krasovsky.dima.demoproject.main.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.krasovsky.dima.demoproject.main.R
import kotlinx.android.synthetic.main.layout_status_loading.view.*
import android.support.v4.content.ContextCompat.getSystemService



class StatusLoaded : FrameLayout {

    constructor(context: Context) : super(context) {
        initView(context, null)
    }
    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        initView(context, attr)
    }

    private enum class State { CLEAR, ERROR_CONNECTION, ERROR_LOADED }

    private var state: State? = null

    private fun initView(context: Context, attr: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_status_loading, this, true)
    }

    fun clear() {
        if (State.CLEAR == state) return
        state = State.CLEAR
        setVisibleState(View.GONE, View.GONE)
    }

    fun errorConnection() {
        if (State.ERROR_CONNECTION == state) return
        state = State.ERROR_CONNECTION
        setVisibleState(View.VISIBLE, View.GONE)
    }

    fun errorLoaded() {
        if (State.ERROR_LOADED == state) return
        state = State.ERROR_LOADED
        setVisibleState(View.GONE, View.VISIBLE)
    }

    private fun setVisibleState(loadedState: Int, connectionState: Int) {
        view_loaded.visibility = loadedState
        view_connection.visibility = connectionState
    }
}