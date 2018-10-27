package com.krasovsky.dima.demoproject.base.view.activity

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R


abstract class BackToolbarActivity : AppCompatActivity() {

    fun initToolbar() {
        findViewById<View>(R.id.toollbar)?.also { it ->
            setTitle(it)
            it.findViewById<ImageView>(R.id.toolbar_back)?.setOnClickListener { onClickBack() }
        }
    }

    private fun setTitle(view: View) {
        val title = getTitleBar()
        when(title) {
            is Int -> view.findViewById<TextView>(R.id.toolbar_title)?.setText(title)
            is String -> view.findViewById<TextView>(R.id.toolbar_title)?.text = title
        }
    }

    protected abstract fun getTitleBar(): Any

    protected abstract fun onClickBack()

}