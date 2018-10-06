package com.krasovsky.dima.demoproject.base.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment


abstract class BackToolbarFragment : BaseMenuFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        view?.findViewById<View>(R.id.toollbar)?.also { it ->
            setTitle(it)
            it.findViewById<ImageView>(R.id.toolbar_back)?.setOnClickListener { onClickBack() }
        }
    }

    private fun setTitle(view: View) {
        val title = getTitle()
        when(title) {
            is Int -> view.findViewById<TextView>(R.id.toolbar_title)?.setText(title)
            is String -> view.findViewById<TextView>(R.id.toolbar_title)?.text = title
        }
    }

    protected abstract fun getTitle(): Any

    protected abstract fun onClickBack()

}