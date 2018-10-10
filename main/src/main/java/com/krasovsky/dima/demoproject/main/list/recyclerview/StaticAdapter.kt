package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.manager.ResourceManager
import com.krasovsky.dima.demoproject.main.util.Mapper
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject


class StaticAdapter(diffUtil: DiffUtil.ItemCallback<BlockInfoObject>) :
        PagedListAdapter<BlockInfoObject, RecyclerView.ViewHolder>(diffUtil) {

    private var isLoading = false

    fun setLoading(loading: Boolean) {
        if (loading == isLoading) return
        this.isLoading = loading
        if (loading) {
            notifyItemInserted(super.getItemCount())
        } else notifyItemRemoved(super.getItemCount() - 1)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading) 1 else 0
    }

    override fun onCreateViewHolder(container: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            R.layout.layout_progress -> LoadingViewHolder(LayoutInflater.from(container.context).inflate(R.layout.layout_progress,
                    container, false))
            else -> ViewHolder(LayoutInflater.from(container.context).inflate(R.layout.item_content_info,
                    container, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == itemCount - 1) {
            R.layout.layout_progress
        } else {
            R.layout.item_content_info
        }
    }

    open class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.content_title)
        private val container = itemView.findViewById<LinearLayout>(R.id.content_container)

        fun bind(data: BlockInfoObject?) {
            if (data == null) return
            if (data.title == null) {
                tvTitle.visibility = View.GONE
            } else {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = data.title
            }
            Mapper.mapInfoObjectArray(itemView.context, data.items).forEach {
                ResourceManager.applyParams(it)
                container.addView(it)
            }
        }
    }

}