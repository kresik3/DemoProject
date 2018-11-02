package com.krasovsky.dima.demoproject.main.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.krasovsky.dima.demoproject.main.R

class StatusLoadedPaging : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr)

    private enum class State { CLEAR, ERRORCONNECTION, ERRORLOADED }

    private var state: State? = null

    private val viewErrorConnection: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_error_connection, null, false)
    }

    private val viewErrorLoaded: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_error_loaded, null, false)
    }

    fun clear() {
        if (State.CLEAR == state) return
        state = State.CLEAR
        this.removeAllViews()
    }

    fun errorConnection() {
        if (State.ERRORCONNECTION == state) return
        state = State.ERRORCONNECTION
        this.removeAllViews()
        addView(viewErrorConnection)
    }

    fun errorLoaded() {
        if (State.ERRORLOADED == state) return
        state = State.ERRORLOADED
        this.removeAllViews()
        addView(viewErrorLoaded)
    }
}