package com.krasovsky.dima.demoproject.storage.retrofit

import com.google.gson.reflect.TypeToken
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.paging.DishesPage
import com.krasovsky.dima.demoproject.storage.retrofit.executor.ApiExecutors
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.*
import io.reactivex.Flowable

class ApiManager(private val api: ApiClient) {

    private val executor: ApiExecutors = ApiExecutors()

    fun makeOrder(model: PaymentModel): Flowable<Boolean> {
        return executor.executeBooleanRequest { api.getApi().makeOrder(model) }
    }

    fun createBasket(model: CreateBasketModel): Flowable<String> {
        return executor.executeBooleanRequest { api.getApi().createBasket(model) }
                .map { model.id }
    }

    fun getBasket(id: String): Flowable<BasketModel> {
        return executor.executeRequest(getTokenType<BasketModel>()) { api.getApi().getBasket(id) }
    }

    fun addItem(id: String, model: DishItemModel): Flowable<BasketModel> {
        return executor.executeRequest(getTokenType<BasketModel>()) { api.getApi().addItem(id, model) }
    }

    fun removeItem(id: String, model: RemoveItemModel): Flowable<BasketModel> {
        return executor.executeRequest(getTokenType<BasketModel>()) { api.getApi().removeItem(id, model) }
    }

    fun getDishesByPage(model: DishesPageModel): Flowable<DishesPage> {
        return executor.executeRequest(getTokenType<DishesPage>()) {
            api.getApi().getDishesByPage(model.categoryId, model.index, model.pageSize)
        }
    }

    fun getDiscount(): Flowable<ArrayList<BlockInfoObject>> {
        return executor.executeRequest(getTokenType<ArrayList<BlockInfoObject>>()) { api.getApi().getDiscount() }
    }

    fun getInfo(): Flowable<ArrayList<BlockInfoObject>> {
        return executor.executeRequest(getTokenType<ArrayList<BlockInfoObject>>()) { api.getApi().getAbout() }
    }

    fun getDelivery(): Flowable<ArrayList<BlockInfoObject>> {
        return executor.executeRequest(getTokenType<ArrayList<BlockInfoObject>>()) { api.getApi().getDelivery() }
    }

    fun getMenuItems(): Flowable<ArrayList<MenuItemModel>> {
        return executor.executeRequest(getTokenType<ArrayList<MenuItemModel>>()) { api.getApi().getMenuItems() }
    }

    fun getDiscountHistory(): Flowable<HistoryModel> {
        return executor.executeRequest(getTokenType<HistoryModel>()) { api.getApi().getDiscountHistory() }
    }

    fun getInfoHistory(): Flowable<HistoryModel> {
        return executor.executeRequest(getTokenType<HistoryModel>()) { api.getApi().getAboutHistory() }
    }

    fun getDeliveryHistory(): Flowable<HistoryModel> {
        return executor.executeRequest(getTokenType<HistoryModel>()) { api.getApi().getDeliveryHistory() }
    }

    fun getMenuHistory(): Flowable<HistoryModel> {
        return executor.executeRequest(getTokenType<HistoryModel>()) { api.getApi().getMenuHistory() }
    }

    private inline fun <reified T> getTokenType() = object : TypeToken<T>() {}.type

}


