package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.ZoomViewerDialog
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.main.util.applyEnable
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel

class BasketAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickBasketListener {
        fun onClickRemove(model: BasketItemModel, isAll: Boolean)
        fun onClickAdd(model: BasketItemModel, isAll: Boolean)
    }

    private var titleEmpty = R.string.empty_basket_title
    private val priceUtil: PriceUtil by lazy { PriceUtil() }
    var listener: OnClickBasketListener? = null
    var array: List<BasketItemModel>? = listOf()
        set(value) {
            if (field?.size == 0 && value?.size != 0) {
                notifyItemRemoved(0)
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            0 -> EmptyVH(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_list_empty, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_basket_item, parent, false))
        }

    }

    override fun getItemCount(): Int {
        val size = array?.size ?: 0
        return if (size == 0) 1 else size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when(viewHolder) {
            is EmptyVH -> viewHolder.bind(titleEmpty)
            is ViewHolder -> viewHolder.bind(array?.get(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return array?.size ?: 0
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val addOne = itemView.findViewById<ImageView>(R.id.basket_item_add_one)

        private val delete = itemView.findViewById<ImageView>(R.id.basket_item_delete)
        private val deleteOne = itemView.findViewById<ImageView>(R.id.basket_item_delete_one)

        private val image = itemView.findViewById<ImageView>(R.id.basket_item_image)
        private val tvTitle = itemView.findViewById<TextView>(R.id.basket_item_title)
        private val tvPrice = itemView.findViewById<TextView>(R.id.basket_item_price)
        private val tvKind = itemView.findViewById<TextView>(R.id.basket_item_kind)
        private val tvCount = itemView.findViewById<TextView>(R.id.basket_item_count)

        private val zoom: ZoomViewerDialog by lazy {
            ZoomViewerDialog.Builder((itemView.context as AppCompatActivity)).build()
        }

        fun bind(model: BasketItemModel) {
            with(model) {
                PicassoUtil.setImagePicasso(imagePath, image)
                tvTitle.text = title
                tvPrice.text = priceUtil.parseToPrice(price)
                tvKind.text = kind
                tvCount.text = count.toString()
                deleteOne.applyEnable(count != 1)
                initListeners(this)
            }
        }

        private fun initListeners(model: BasketItemModel) {
            delete.setOnClickListener { listener?.onClickRemove(model, true) }
            deleteOne.setOnClickListener { listener?.onClickRemove(model, false) }
            addOne.setOnClickListener { listener?.onClickAdd(model, false) }
        }
    }

}