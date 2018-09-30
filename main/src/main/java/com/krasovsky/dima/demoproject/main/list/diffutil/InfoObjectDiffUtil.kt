package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject

class InfoObjectDiffUtil() : DiffUtil.ItemCallback<BlockInfoObject>() {
    override fun areItemsTheSame(p0: BlockInfoObject, p1: BlockInfoObject): Boolean {
        if (p0.id != p1.id) return false
        if (p0.items.size != p1.items.size) return false
        val e0 = p0.items.iterator()
        val e1 = p1.items.iterator()
        while (e0.hasNext() && e1.hasNext()) {
            if (e0.next().id != e1.next().id)
                return false
        }
        return true

    }

    override fun areContentsTheSame(p0: BlockInfoObject, p1: BlockInfoObject): Boolean {
        if (p0.title != p1.title) return false
        val e0 = p0.items.iterator()
        val e1 = p1.items.iterator()
        while (e0.hasNext() && e1.hasNext()) {
            if (e0.next().content != e1.next().content) return false
        }
        return true
    }

}