package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.util.applyEnable
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel

class BasketAdapter() : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    interface OnClickBasketListener {
        fun onClickRemove(model: BasketItemModel, isAll: Boolean)
    }

    private val priceUtil: PriceUtil by lazy { PriceUtil() }
    var listener: OnClickBasketListener? = null
    var array: List<BasketItemModel>? = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context)
                .inflate(R.layout.layout_basket_item, p0, false))
    }

    override fun getItemCount() = array?.size ?: 0

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(array?.get(p1)!!)
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete = itemView.findViewById<ImageButton>(R.id.basket_item_delete)
        private val deleteOne = itemView.findViewById<ImageButton>(R.id.basket_item_delete_one)

        private val image = itemView.findViewById<ImageView>(R.id.basket_item_image)
        private val tvTitle = itemView.findViewById<TextView>(R.id.basket_item_title)
        private val tvPrice = itemView.findViewById<TextView>(R.id.basket_item_price)
        private val tvKind = itemView.findViewById<TextView>(R.id.basket_item_kind)

        fun bind(model: BasketItemModel) {
            with(model) {
                PicassoUtil.setImagePicasso(imagePath, image)
                tvTitle.text = title
                tvPrice.text = priceUtil.parseToPrice(price)
                tvKind.text = kind
                deleteOne.applyEnable(count != 1)
                initListeners(this)
            }
        }

        private fun initListeners(model: BasketItemModel) {
            delete.setOnClickListener { listener?.onClickRemove(model, true) }
            deleteOne.setOnClickListener { listener?.onClickRemove(model, false) }
        }
    }

}