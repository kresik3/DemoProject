package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.RemoveItemModel
import io.reactivex.Flowable
import java.util.*
import kotlin.collections.ArrayList

class BasketManager(val realmManager: RealmManager,
                    val apiManager: ApiManager) {

    fun createBasket(): Flowable<String> {
        return apiManager.createBasket(UUID.randomUUID().toString())
    }

    fun getBasket(id: String): Flowable<BasketModel> {
        return apiManager.getBasket(id)
                .map { it ->
                    val splitItems: MutableList<BasketItemModel> = mutableListOf()
                    it.items.forEach {
                        for (i in 0 until it.count) splitItems.add(it)
                    }
                    it.apply {
                        items = splitItems.apply { forEach { it.count = 1 } }
                    }
                }
    }

    fun addItem(id: String, shopItemDetailId: String, count: Int): Flowable<Boolean> {
        return apiManager.addItem(id, DishItemModel(shopItemDetailId, count))
    }

    fun removeItem(id: String, shopItemDetailId: String): Flowable<Boolean> {
        return apiManager.removeItem(id, RemoveItemModel(shopItemDetailId))
    }
}