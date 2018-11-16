package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.ZoomViewerDialog
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.model.DishActionModel
import com.krasovsky.dima.demoproject.main.list.datasource.model.list.RecyclerViewStateModel
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.main.manager.ResourceManager
import com.krasovsky.dima.demoproject.main.util.Mapper
import com.krasovsky.dima.demoproject.main.util.price.PriceUtil
import com.krasovsky.dima.demoproject.main.view.custom.DetailDishView
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject


class DishesAdapter(diffUtil: DiffUtil.ItemCallback<DishModel>) :
        PagedListAdapter<DishModel, RecyclerView.ViewHolder>(diffUtil) {

    private var titleEmpty = R.string.empty_list_title
    private var isLoading = false
    private var isEmpty = true

    var listener: ((item: DishActionModel) -> Unit)? = null

    fun setEmpty(empty: Boolean) {
        if (empty == isEmpty) return
        this.isEmpty = empty
        if (!empty) {
            notifyItemRemoved(0)
        }
    }

    fun setLoading(loading: Boolean) {
        if (loading == isLoading) return
        this.isLoading = loading
        if (loading) {
            notifyItemInserted(super.getItemCount())
        } else notifyItemRemoved(super.getItemCount() - 1)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading) 1 else 0 + if (isEmpty) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            0 -> EmptyVH(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_empty, parent, false))
            R.layout.layout_progress -> LoadingViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_progress, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_dish_item,
                    parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is EmptyVH -> viewHolder.bind(titleEmpty)
            is ViewHolder -> viewHolder.bind(getItem(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == itemCount - 1) {
            R.layout.layout_progress
        } else if (super.getItemCount() != 0) {
            R.layout.layout_dish_item
        } else 0
    }

    open class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

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

