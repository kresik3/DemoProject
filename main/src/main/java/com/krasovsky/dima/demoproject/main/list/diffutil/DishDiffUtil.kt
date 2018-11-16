package com.krasovsky.dima.demoproject.main.list.diffutil

import android.support.v7.util.DiffUtil
import android.util.Log
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel


class DishDiffUtil : DiffUtil.ItemCallback<DishModel>() {

    override fun areItemsTheSame(oldItem: DishModel, newItem: DishModel) =
            oldItem.id ==
                    newItem.id

    override fun areContentsTheSame(oldItem: DishModel, newItem: DishModel): Boolean {
        return oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.categoryId == newItem.categoryId
                && oldItem.imagePath == newItem.imagePath
                && oldItem.details == newItem.details
    }

}