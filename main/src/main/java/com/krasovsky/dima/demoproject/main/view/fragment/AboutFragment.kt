package com.krasovsky.dima.demoproject.main.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.diffutil.InfoObjectDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.StaticAdapter
import com.krasovsky.dima.demoproject.main.view.model.InfoViewModel
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : ToolbarFragment() {

    private val model: InfoViewModel by lazy {
        ViewModelProviders.of(this).get(InfoViewModel::class.java)
    }

    override fun getTitle() = R.string.toolbar_about

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeFields()
    }

    private fun initView() {
        about_list.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        about_list.adapter = StaticAdapter(InfoObjectDiffUtil()).apply { submitList(model.getData()) }
    }

    private fun observeFields() {
    }


}
