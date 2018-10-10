package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
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
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.list.diffutil.InfoObjectDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.StaticAdapter
import com.krasovsky.dima.demoproject.main.list.recyclerview.decorator.BaseItemDecorator
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
        initSwipeRefresh()
        initToolbarListeners()
        about_list.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = StaticAdapter(InfoObjectDiffUtil()).apply { submitList(model.getData()) }
            addItemDecoration(BaseItemDecorator())
        }
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun initToolbarListeners() {
        view?.findViewById<View>(com.krasovsky.dima.demoproject.base.R.id.toollbar)
                ?.setOnClickListener {
                    about_list.scrollToPosition(0)
                }
    }

    private fun refresh() {
        (about_list.adapter as StaticAdapter).submitList(model.refresh())
    }

    private fun observeFields() {
        observeConnection()
        observeSwiping()
        observeLoading()
    }

    private fun observeConnection() {
        model.liveDataConnection.observe(this, Observer {
            when (it) {
                TypeConnection.ERROR_CONNECTION -> {
                    swipe_refresh.isEnabled = true
                    header_status_connection.errorConnection()
                }
                TypeConnection.ERROR_LOADED -> {
                    swipe_refresh.isEnabled = true
                    header_status_connection.errorLoaded()
                }
                TypeConnection.CLEAR -> {
                    swipe_refresh.isEnabled = false
                    header_status_connection.clear()
                }
            }
        })
    }

    private fun observeSwiping() {
        model.stateSwiping.observe(this, Observer {
            swipe_refresh.isRefreshing = it ?: false
        })
    }

    private fun observeLoading() {
        model.stateLoading.observe(this, Observer {
            (about_list.adapter as StaticAdapter).setLoading(it ?: false)
        })
    }

}
