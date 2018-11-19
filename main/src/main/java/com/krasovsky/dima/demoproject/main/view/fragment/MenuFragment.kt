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
import com.krasovsky.dima.demoproject.base.dialog.alert.ErrorDialog
import com.krasovsky.dima.demoproject.base.dialog.alert.base.BaseDialog
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeConnection
import com.krasovsky.dima.demoproject.main.list.diffutil.MenuDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.MenuAdapter
import com.krasovsky.dima.demoproject.main.list.recyclerview.decorator.BaseItemDecorator
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
        initSwipeRefresh()
        initToolbarListeners()
        menu_list.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = MenuAdapter()
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
                    menu_list.scrollToPosition(0)
                }
    }

    private fun observeFields() {
        observeMenu()
        observeConnection()
        observeSwiping()
        observeError()
    }

    private fun observeMenu() {
        model.getMenu().observe(viewLifecycleOwner, Observer {
            val adapter = menu_list.adapter as MenuAdapter
            val productDiffUtilCallback = MenuDiffUtil(adapter.array, it)
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

            adapter.array = it
            productDiffResult.dispatchUpdatesTo(adapter)
        })
    }

    private fun observeConnection() {
        model.liveDataConnection.observe(viewLifecycleOwner, Observer {
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
        model.stateSwiping.observe(viewLifecycleOwner, Observer {
            swipe_refresh.isRefreshing = it ?: false
        })
    }

    private fun observeError() {
        val dialog = model.error
        dialog.observe(viewLifecycleOwner, Observer { data ->
            if (data == null) return@Observer
            ErrorDialog.Builder().apply {
                initView(context!!)
                setTitle(data.title)
                setMessage(data.message)
                setPositiveBtn(data.btnOk) {
                    dialog.clear()
                }
            }.build().run {
                show(this@MenuFragment.fragmentManager, "dialog")
            }
        })
    }
}
