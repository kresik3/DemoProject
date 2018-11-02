package com.krasovsky.dima.demoproject.base.view.fragment

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment


abstract class ToolbarFragment : BaseMenuFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        view?.findViewById<View>(R.id.toollbar)?.also {
            setTitle(it)
        }
    }

    private fun setTitle(view: View) {
        val title = getTitle()
        when (title) {
            is Int -> view.findViewById<TextView>(R.id.toolbar_title)?.setText(title)
            is String -> view.findViewById<TextView>(R.id.toolbar_title)?.text = title
        }
    }

    protected abstract fun getTitle(): Any

}