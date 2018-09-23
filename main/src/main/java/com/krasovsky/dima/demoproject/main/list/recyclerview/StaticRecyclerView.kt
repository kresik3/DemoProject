package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.storage.model.InfoObject

class StaticRecyclerView(diffUtil: DiffUtil.ItemCallback<InfoObject>) :
        PagedListAdapter<InfoObject, StaticRecyclerView.StaticVH>(diffUtil) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StaticVH {

    }

    override fun onBindViewHolder(p0: StaticVH, p1: Int) {

    }


    inner class StaticVH(view: View) : RecyclerView.ViewHolder(view) {

    }

}