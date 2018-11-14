package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.arch.paging.PagedList
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
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.main.manager.ResourceManager
import com.krasovsky.dima.demoproject.main.util.Mapper
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject


/*DEPRECATED.NOT USE. For pagging*/
class StaticAdapter(diffUtil: DiffUtil.ItemCallback<BlockInfoObject>) :
        PagedListAdapter<BlockInfoObject, RecyclerView.ViewHolder>(diffUtil) {

    private var titleEmpty = R.string.empty_list_title
    private var isLoading = false

    fun setLoading(loading: Boolean) {
        if (loading == isLoading) return
        this.isLoading = loading
        if (loading) {
            notifyItemInserted(super.getItemCount())
        } else notifyItemRemoved(super.getItemCount() - 1)
    }

    override fun submitList(pagedList: PagedList<BlockInfoObject>?) {
        super.submitList(pagedList)
        pagedList?.addWeakCallback(pagedList.snapshot(), object : PagedList.Callback() {
            override fun onChanged(position: Int, count: Int) {
            }

            override fun onInserted(position: Int, count: Int) {
                if (itemCount == count) {
                    notifyItemRemoved(0)
                }
            }

            override fun onRemoved(position: Int, count: Int) {
            }
        })
    }

    override fun getItemCount(): Int {
        val size = super.getItemCount()
        return (if (size == 0) 1 else size) + (if (isLoading) 1 else 0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            0 -> EmptyVH(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_empty, parent, false))
            R.layout.layout_progress -> LoadingViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_progress, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_content_info,
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
            R.layout.item_content_info
        } else 0
    }

    open class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.content_title)
        private val container = itemView.findViewById<LinearLayout>(R.id.content_container)

        fun bind(data: BlockInfoObject) {
            if (data.title == null) {
                tvTitle.visibility = View.GONE
            } else {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = data.title
            }
            container.removeAllViews()
            Mapper.mapInfoObjectArray(itemView.context, data.items).forEach {
                ResourceManager.applyParams(it)
                container.addView(it)
            }
        }
    }

}