package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.krasovsky.dima.demoproject.main.view.model.DiscountViewModel
import kotlinx.android.synthetic.main.fragment_discount.*

class DiscountFragment : ToolbarFragment() {

    override fun getTitle() = R.string.toolbar_discount

    private val model: DiscountViewModel by lazy {
        ViewModelProviders.of(this).get(DiscountViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeFields()
    }

    private fun initView() {
        initSwipeRefresh()
        initToolbarListeners()
        discount_list.apply {
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
                    discount_list.scrollToPosition(0)
                }
    }

    private fun refresh() {
        (discount_list.adapter as StaticAdapter).submitList(model.refresh())
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
            (discount_list.adapter as StaticAdapter).setLoading(it ?: false)
        })
    }
}
