package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject

class InfoObjectDiffUtil(private val oldList: List<BlockInfoObject>?,
                         private val newList: List<BlockInfoObject>?) : DiffUtil.Callback()  {
    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        val oldItem = oldList?.get(p0)
        val newItem = newList?.get(p0)
        if (oldItem?.id != newItem?.id) return false
        if (oldItem?.items?.size != newItem?.items?.size) return false
        val e0 = oldItem?.items?.iterator()
        val e1 = newItem?.items?.iterator()
        while (e0?.hasNext() == true && e1?.hasNext() == true) {
            if (e0.next()?.id != e1.next()?.id)
                return false
        }
        return true
    }

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        val oldItem = oldList?.get(p0)
        val newItem = newList?.get(p0)
        if (oldItem?.title != newItem?.title) return false
        val e0 = oldItem?.items?.iterator()
        val e1 = newItem?.items?.iterator()
        while (e0?.hasNext() == true && e1?.hasNext() == true) {
            if (e0.next().content != e1.next().content) return false
        }
        return true
    }

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }


}