package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.main.manager.ResourceManager
import com.krasovsky.dima.demoproject.main.util.Mapper
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject


class InfoObjectAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var titleEmpty = R.string.empty_list_title

    var array: List<BlockInfoObject>? = listOf()
        set(value) {
            if (field?.size == 0 && value?.size != 0) {
                notifyItemRemoved(0)
            }
            field = value
        }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is EmptyVH -> viewHolder.bind(titleEmpty)
            is ViewHolder -> viewHolder.bind(array!![position]!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            0 -> EmptyVH(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_list_empty, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_content_info,
                    parent, false))
        }
    }

    override fun getItemCount(): Int {
        val size = array?.size ?: 0
        return if (size == 0) 1 else size
    }

    override fun getItemViewType(position: Int): Int {
        return array?.size ?: 0
    }

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