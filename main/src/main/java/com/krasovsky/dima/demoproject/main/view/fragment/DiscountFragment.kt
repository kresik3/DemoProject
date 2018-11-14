package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.list.diffutil.InfoObjectDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.InfoObjectAdapter
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
            adapter = InfoObjectAdapter()
            addItemDecoration(BaseItemDecorator())
        }
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    private fun initToolbarListeners() {
        view?.findViewById<View>(com.krasovsky.dima.demoproject.base.R.id.toollbar)
                ?.setOnClickListener {
                    discount_list.scrollToPosition(0)
                }
    }


    private fun observeFields() {
        observeDiscount()
        observeConnection()
        observeSwiping()
    }

    private fun observeDiscount() {
        model.discount.observe(this, Observer {
            val adapter = discount_list.adapter as InfoObjectAdapter
            val infoDiffUtilCallback = InfoObjectDiffUtil(adapter.array, it)
            val infoDiffResult = DiffUtil.calculateDiff(infoDiffUtilCallback)

            adapter.array = it
            infoDiffResult.dispatchUpdatesTo(adapter)
        })
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

}
