package com.krasovsky.dima.demoproject.main.view.fragment


import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.base.view.fragment.BackToolbarFragment
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.IToolbarCommand
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.main.command.action.activity.DishItemAction
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_ACTIVITY_DISH
import com.krasovsky.dima.demoproject.main.command.action.activity.KEY_COUNT_DISH
import com.krasovsky.dima.demoproject.main.command.action.badge.AddBasketBadgeAction
import com.krasovsky.dima.demoproject.main.command.view.IActionCommand
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.list.recyclerview.DishesAdapter
import com.krasovsky.dima.demoproject.main.list.recyclerview.decorator.BaseItemDecorator
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.COMMAND_BACK
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.COMMAND_CANCEL
import com.krasovsky.dima.demoproject.main.view.model.DishesViewModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import kotlinx.android.synthetic.main.fragment_dishes.*




private const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
private const val KEY_CATEGORY_NAME = "KEY_CATEGORY_NAME"

class DishesFragment : BackToolbarFragment() {

    override fun getTitle() = arguments?.getString(KEY_CATEGORY_NAME) ?: ""

    private val model: DishesViewModel by lazy {
        ViewModelProviders.of(this).get(DishesViewModel::class.java)
    }

    companion object {
        fun getInstance(categoryID: String, categoryName: String): BaseMenuFragment {
            return DishesFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_ID, categoryID)
                    putString(KEY_CATEGORY_NAME, categoryName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.categoryItemId = arguments?.getString(KEY_CATEGORY_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dishes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeFields()
    }


    private fun initView() {
        initSwipeRefresh()
        initToolbarListeners()
        dishes_list.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = DishesAdapter().apply {
                listener = object : DishesAdapter.OnClickDishItem {
                    override fun onClickDishItem(item: DishModel) {
                        ((context as AppCompatActivity) as IActionCommand)
                                .sendCommand(DishItemAction(this@DishesFragment, item))
                    }
                }
            }
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
                    dishes_list.scrollToPosition(0)
                }
    }

    private fun observeFields() {
        observeDishes()
        observeConnection()
        observeSwiping()
    }

    private fun observeDishes() {
        model.dishes.observe(this, Observer {
            (dishes_list.adapter as DishesAdapter).array = it
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

    override fun onClickBack() {
        (context as AppCompatActivity as IToolbarCommand).sendCommand(COMMAND_BACK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == KEY_ACTIVITY_DISH && resultCode == RESULT_OK && data != null) {
            (context as AppCompatActivity? as IActionCommand).sendCommand(AddBasketBadgeAction(data.getIntExtra(KEY_COUNT_DISH, 0)))
            (context as AppCompatActivity as IToolbarCommand).sendCommand(COMMAND_CANCEL)
        }
    }

}
