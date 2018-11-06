package com.krasovsky.dima.demoproject.repository.manager

import android.util.Log
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.CreateBasketModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.PaymentModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.RemoveItemModel
import io.reactivex.Flowable
import java.util.*
import kotlin.collections.ArrayList

class BasketManager(private val realmManager: RealmManager,
                    private val apiManager: ApiManager) {

    fun makeOrder(name: String, telephone: String, address: String, comment: String, basketId: String) : Flowable<Boolean> {
        return apiManager.makeOrder(PaymentModel(name, telephone, address, comment, basketId))
    }

    fun createBasket(): Flowable<String> {
        return apiManager.createBasket(CreateBasketModel(UUID.randomUUID().toString()))
    }

    fun getBasket(id: String): Flowable<BasketModel> {
        return apiManager.getBasket(id)
    }

    fun addItem(id: String, shopItemDetailId: String, count: Int): Flowable<BasketModel> {
        return apiManager.addItem(id, DishItemModel(shopItemDetailId, count))
    }

    fun removeItem(id: String, shopItemDetailId: String, isAll: Boolean): Flowable<BasketModel> {
        return apiManager.removeItem(id, RemoveItemModel(shopItemDetailId, isAll))
    }
}