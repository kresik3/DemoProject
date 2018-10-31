package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import android.util.Log
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel


class DishDiffUtil(private val oldList: List<DishModel>?,
                   private val newList: List<DishModel>?) : DiffUtil.Callback() {

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
        return oldProduct?.title == newProduct?.title
                && oldProduct?.description == newProduct?.description
                && oldProduct?.categoryId == newProduct?.categoryId
                && oldProduct?.imagePath == newProduct?.imagePath
                && oldProduct?.details == newProduct?.details
    }
}