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


abstract class BackToolbarFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        view?.findViewById<View>(R.id.toollbar)?.also { it ->
            it.findViewById<TextView>(R.id.toolbar_title)?.setText(getTitle())
            it.findViewById<ImageView>(R.id.toolbar_back)?.setOnClickListener { onClickBack() }
        }
    }

    protected abstract fun getTitle(): Int

    protected abstract fun onClickBack()

}