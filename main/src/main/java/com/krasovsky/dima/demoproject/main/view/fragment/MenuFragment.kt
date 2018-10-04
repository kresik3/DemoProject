package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.recyclerview.MenuAdapter
import com.krasovsky.dima.demoproject.main.view.model.MenuViewModel
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : ToolbarFragment() {

    override fun getTitle() = R.string.toolbar_menu

    private val model: MenuViewModel by lazy {
        ViewModelProviders.of(this).get(MenuViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeFields()
    }

    private fun initView() {
        menu_list.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        menu_list.adapter = MenuAdapter()
    }

    private fun observeFields() {
        obsetveMenu()
    }

    private fun obsetveMenu() {
        model.getMenu().observe(this, Observer {
            (menu_list.adapter as MenuAdapter).array = it
        })
    }

}
