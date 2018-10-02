package com.krasovsky.dima.demoproject.base.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.krasovsky.dima.demoproject.base.R


abstract class ToolbarFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        view?.findViewById<Toolbar>(R.id.toollbar)?.also {
            (context as AppCompatActivity).setSupportActionBar(it)
        }
        (context as AppCompatActivity).supportActionBar?.setTitle(getTitle())
    }

    protected abstract fun getTitle(): Int

}