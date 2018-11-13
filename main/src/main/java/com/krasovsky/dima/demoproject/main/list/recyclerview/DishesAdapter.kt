package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.app.AppCompatActivity
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
import com.krasovsky.dima.demoproject.main.command.action.model.DishActionModel
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.view.custom.DetailDishView
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.ZoomViewerDialog


class DishesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var titleEmpty = R.string.empty_list_title
    var listener: ((item: DishActionModel) -> Unit)? = null
    var array: List<DishModel>? = listOf()
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
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_dish_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        val size = array?.size ?: 0
        return if (size == 0) 1 else size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is EmptyVH -> viewHolder.bind(titleEmpty)
            is ViewHolder -> viewHolder.bind(array!![position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return array?.size ?: 0
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val priceUtil: PriceUtil by lazy { PriceUtil() }

        private val image = itemView.findViewById<ImageView>(R.id.dish_item_image)
        private val title = itemView.findViewById<TextView>(R.id.dish_item_title)
        private val description = itemView.findViewById<TextView>(R.id.dish_item_message)
        private val detailsViewGroup = itemView.findViewById<LinearLayout>(R.id.container_details)

        private val zoom: ZoomViewerDialog by lazy {
            ZoomViewerDialog.Builder((itemView.context as AppCompatActivity)).build()
        }

        fun bind(data: DishModel) {
            PicassoUtil.setImagePicasso(data.imagePath, image)
            title.text = data.title
            setDescription(data)
            setDetails(data)
            itemView.setOnClickListener { v: View? ->
                val p1 = android.support.v4.util.Pair(image as View, image.transitionName)
                val p2 = android.support.v4.util.Pair(title as View, title.transitionName)
                listener?.invoke(DishActionModel(data, arrayOf(p1, p2)))
            }
            zoom.register(image, data.imagePath)
        }

        private fun setDescription(data: DishModel) {
            if (data.description != null) {
                description.text = data.description
            } else description.visibility = View.INVISIBLE
        }

        private fun setDetails(data: DishModel) {
            detailsViewGroup.removeAllViews()
            var detailView: DetailDishView
            data.details.forEach {
                detailView = DetailDishView(itemView.context).apply {
                    type = it.kind
                    price = priceUtil.parseToPrice(it.price)
                }
                detailsViewGroup.addView(detailView.view)
            }
        }

    }


}