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
import com.krasovsky.dima.demoproject.main.list.recyclerview.decorator.BaseItemDecorator
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.layout_basket_bottom_sheet.*
import com.krasovsky.dima.demoproject.main.list.behaviour.ScrollBehaviour
import android.support.design.widget.CoordinatorLayout


class BasketFragment : ToolbarFragment(), BasketAdapter.OnClickBasketListener {

    override fun getTitle() = R.string.toolbar_basket

    private val model: BasketViewModel by lazy {
        ViewModelProviders.of(this).get(BasketViewModel::class.java)
    }
    private val priceUtil: PriceUtil by lazy { PriceUtil() }

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
        (include_layout_basket.layoutParams as CoordinatorLayout.LayoutParams).apply {
            behavior = ScrollBehaviour()
        }
        basket_list.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = BasketAdapter().apply { listener = this@BasketFragment }
            addItemDecoration(BaseItemDecorator(R.dimen.base_space))
        }
    }

    override fun onClickRemove(model: BasketItemModel, isAll: Boolean) {
        this.model.removeItem(model, isAll)
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
            initBottomSheetBasket(it)
            val adapter = basket_list.adapter as BasketAdapter
            val productDiffUtilCallback = BasketDiffUtil(adapter.array, it?.items)
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

            adapter.array = it?.items
            productDiffResult.dispatchUpdatesTo(adapter)
        })
    }

    private fun initBottomSheetBasket(model: BasketModel?) {
        if (model == null) return
        if (model.items.size == 0) {
            include_layout_basket.visibility = View.GONE
        } else {
            include_layout_basket.visibility = View.VISIBLE
            basket_count.text = model.totalCount.toString()
            basket_total_price.text = priceUtil.parseToPrice(model.totalPrice)
        }
    }

    private fun observeDeleteItem() {
        model.deletedCount.observe(this, Observer {
            (context as AppCompatActivity? as IActionCommand).sendCommand(AddBasketBadgeAction(it!!))
        })
    }
}
