package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import com.krasovsky.dima.demoproject.storage.model.InfoObject

class InfoObjectDiffUtil(private val oldList: List<InfoObject>,
                         private val newList: List<InfoObject>) : DiffUtil.Callback() {

    override fun areItemsTheSame(p0: Int, p1: Int) = oldList[p0].id == newList[p1].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(p0: Int, p1: Int) = oldList[p0].content == newList[p1].content
}