package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel


class BasketAdapter() : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    interface OnClickBasketListener {
        fun onClickRemove(shopItemDetailId: String)
    }

    var listener: OnClickBasketListener? = null
    var array: List<BasketItemModel>? = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context)
                .inflate(R.layout.layout_basket_item, p0, false))
    }

    override fun getItemCount() = array?.size ?: 0

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(array?.get(p1))
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(model: BasketItemModel?) {

        }
    }

}