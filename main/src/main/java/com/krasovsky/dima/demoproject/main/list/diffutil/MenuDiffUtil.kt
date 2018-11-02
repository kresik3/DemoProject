package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import android.util.Log
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel


class MenuDiffUtil(private val oldList: List<MenuItemModel>?,
                   private val newList: List<MenuItemModel>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList?.get(oldItemPosition)?.id ==
                    newList?.get(newItemPosition)?.id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList?.get(oldItemPosition)
        val newProduct = newList?.get(newItemPosition)
        return oldProduct?.text == newProduct?.text
                && oldProduct?.iconPath == newProduct?.iconPath
    }
}