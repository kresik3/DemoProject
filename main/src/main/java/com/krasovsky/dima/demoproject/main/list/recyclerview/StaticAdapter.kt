package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.map.InflaytingModel
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.util.Mapper
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject


class StaticAdapter(diffUtil: DiffUtil.ItemCallback<BlockInfoObject>) :
        PagedListAdapter<BlockInfoObject, StaticAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(container: ViewGroup, type: Int): StaticAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(container.context).inflate(R.layout.item_content_info,
                container, false))
    }

    override fun onBindViewHolder(holder: StaticAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.content_title)
        private val container = itemView.findViewById<LinearLayout>(R.id.content_container)

        fun bind(data: BlockInfoObject?) {
            if (data == null) return

            tvTitle.text = data.title
            val inflater = InflaytingModel(itemView.context, container)
            Mapper.mapInfoObjectArray(inflater, data.items).forEach { container.addView(it) }
        }
    }

}