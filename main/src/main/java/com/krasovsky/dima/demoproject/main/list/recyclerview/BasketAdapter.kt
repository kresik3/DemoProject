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
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel

class BasketAdapter() : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    interface OnClickBasketListener {
        fun onClickRemove(shopItemDetailId: String)
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
        p0.bind(array?.get(p1))
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete = itemView.findViewById<ImageButton>(R.id.basket_item_delete)

        private val image = itemView.findViewById<ImageView>(R.id.basket_item_image)
        private val title = itemView.findViewById<TextView>(R.id.basket_item_title)
        private val price = itemView.findViewById<TextView>(R.id.basket_item_price)
        private val kind = itemView.findViewById<TextView>(R.id.basket_item_kind)

        fun bind(model: BasketItemModel?) {
            if (model == null) return
            PicassoUtil.setImagePicasso(model.imagePath, image)
            title.text = model.title
            price.text = priceUtil.parseToPrice(model.price)
            kind.text = model.kind
            delete.setOnClickListener {
                listener?.onClickRemove(model.shopItemDetailId)
            }
        }
    }

}