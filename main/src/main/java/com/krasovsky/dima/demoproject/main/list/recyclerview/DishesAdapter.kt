package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.view.custom.DetailDishView
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel


class DishesAdapter() : RecyclerView.Adapter<DishesAdapter.ViewHolder>() {

    interface OnClickDishItem {
        fun onClickDishItem(item: DishModel)
    }

    var listener: OnClickDishItem? = null
    var array: List<DishModel>? = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.layout_dish_item, p0, false))
    }

    override fun getItemCount() = array?.size ?: 0

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(array!![p1])
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.dish_item_image)
        private val title = itemView.findViewById<TextView>(R.id.dish_item_title)
        private val description = itemView.findViewById<TextView>(R.id.dish_item_message)
        private val detailsViewGroup = itemView.findViewById<LinearLayout>(R.id.container_details)

        fun bind(data: DishModel) {
            PicassoUtil.setImagePicasso(data.imagePath, image)
            title.text = data.title
            setDescription(data)
            setDetails(data)
            itemView.setOnClickListener { v: View? ->
                listener?.onClickDishItem(data)
            }
        }

        private fun setDescription(data: DishModel) {
            if (data.description != null) {
                description.text = data.description
            } else description.visibility = View.INVISIBLE
        }

        private fun setDetails(data: DishModel) {
            detailsViewGroup.removeAllViews()
            data.details.forEach {
                detailsViewGroup.addView(DetailDishView(itemView.context)
                        .apply {
                            quantity = it.quantity
                            price = it.price
                        })
            }
        }

    }

}