package com.krasovsky.dima.demoproject.main.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.badge.AddBasketBadgeAction
import com.krasovsky.dima.demoproject.main.command.view.IActionCommand
import com.krasovsky.dima.demoproject.main.view.model.BasketViewModel
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import com.krasovsky.dima.demoproject.main.list.diffutil.BasketDiffUtil
import com.krasovsky.dima.demoproject.main.list.recyclerview.BasketAdapter
import kotlinx.android.synthetic.main.fragment_basket.*


class BasketFragment : ToolbarFragment(), BasketAdapter.OnClickBasketListener {

    override fun getTitle() = R.string.toolbar_basket

    private val model: BasketViewModel by lazy {
        ViewModelProviders.of(this).get(BasketViewModel::class.java)
    }
    private val adapter: BasketAdapter by lazy {
        BasketAdapter()
                .apply { listener = this@BasketFragment }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeFields()
    }

    private fun initView() {
        basket_list.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        basket_list.adapter = adapter
    }

    override fun onClickRemove(shopItemDetailId: String) {
        model.removeItem(shopItemDetailId)
    }

    override fun onShowFragment() {
        super.onShowFragment()
        model.getBasketItems()
    }

    private fun observeFields() {
        observeBasket()
        observeDeleteItem()
    }

    private fun observeBasket() {
        model.basket.observe(this, Observer {
            val productDiffUtilCallback = BasketDiffUtil(adapter.array, it?.items)
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

            adapter.array = it?.items
            productDiffResult.dispatchUpdatesTo(adapter)
        })
    }

    private fun observeDeleteItem() {
        model.deletedCount.observe(this, Observer {
            (context as AppCompatActivity? as IActionCommand).sendCommand(AddBasketBadgeAction(it!!))
        })
    }
}
