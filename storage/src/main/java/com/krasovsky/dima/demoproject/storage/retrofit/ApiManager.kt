package com.krasovsky.dima.demoproject.storage.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.krasovsky.dima.demoproject.storage.model.page.BlockPage
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.retrofit.executor.ApiExecutors
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

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

    fun getDishesByCategory(menuItemId: String): Flowable<ArrayList<DishModel>> {
        return executor.executeRequest(getTokenType<ArrayList<DishModel>>()) { api.getApi().getDishesByCategory(menuItemId) }
    }

    fun getDiscountByPage(model: BlockPageModel): Flowable<BlockPage> {
        return executor.executeRequest(getTokenType<BlockPage>()) { api.getApi().getDiscountByPage(model.index, model.pageSize) }
    }

    fun getInfoByPage(model: BlockPageModel): Flowable<BlockPage> {
        return executor.executeRequest(getTokenType<BlockPage>()) { api.getApi().getAboutByPage(model.index, model.pageSize) }
    }

    fun getDeliveryByPage(model: BlockPageModel): Flowable<BlockPage> {
        return executor.executeRequest(getTokenType<BlockPage>()) { api.getApi().getDeliveryByPage(model.index, model.pageSize) }
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


