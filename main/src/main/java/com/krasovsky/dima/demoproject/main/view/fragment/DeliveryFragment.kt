package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.list.diffutil.InfoObjectDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.InfoObjectAdapter
import com.krasovsky.dima.demoproject.main.list.recyclerview.StaticAdapter
import com.krasovsky.dima.demoproject.main.list.recyclerview.decorator.BaseItemDecorator
import com.krasovsky.dima.demoproject.main.view.model.DeliveryViewModel
import com.krasovsky.dima.demoproject.main.view.model.InfoViewModel
import kotlinx.android.synthetic.main.fragment_delivery.*

class DeliveryFragment : ToolbarFragment() {

    private val model: DeliveryViewModel by lazy {
        ViewModelProviders.of(this).get(DeliveryViewModel::class.java)
    }

    override fun getTitle() = R.string.toolbar_delivery

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeFields()
    }

    private fun initView() {
        initSwipeRefresh()
        initToolbarListeners()
        delivery_list.apply {
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
                    delivery_list.scrollToPosition(0)
                }
    }

    private fun observeFields() {
        observeDelivery()
        observeConnection()
        observeSwiping()
    }

    private fun observeDelivery() {
        model.delivery.observe(this, Observer {
            val adapter = delivery_list.adapter as InfoObjectAdapter
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
