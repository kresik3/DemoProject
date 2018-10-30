package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import android.util.Log
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel


class BasketDiffUtil(private val oldList: List<BasketItemModel>?,
                     private val newList: List<BasketItemModel>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList?.get(oldItemPosition)?.shopItemDetailId ==
                    newList?.get(newItemPosition)?.shopItemDetailId


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList?.get(oldItemPosition)
        val newProduct = newList?.get(newItemPosition)
        Log.e("MYLOG", "oldProduct?.count = ${oldProduct?.count}" )
        Log.e("MYLOG", "newProduct?.count = ${newProduct?.count}" )
        return oldProduct?.title == newProduct?.title
                && oldProduct?.kind == newProduct?.kind
                && oldProduct?.price == newProduct?.price
                && oldProduct?.count == newProduct?.count
    }
}