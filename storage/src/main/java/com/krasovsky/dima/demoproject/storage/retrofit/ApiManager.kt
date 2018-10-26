package com.krasovsky.dima.demoproject.storage.retrofit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.krasovsky.dima.demoproject.storage.model.page.BlockPage
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.RemoveItemModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ApiManager(private val api: ApiClient) {

    fun createBasket(id: String): Flowable<String> {
        return Flowable.create({
            val response = api.getApi().createBasket(id).execute()
            if (response.isSuccessful) {
                it.onNext(id)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun getBasket(id: String): Flowable<BasketModel> {
        return Flowable.create({
            val response = api.getApi().getBasket(id).execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<BasketModel>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun addItem(id: String, model: DishItemModel): Flowable<Boolean> {
        return Flowable.create({
            val response = api.getApi().addItem(id, model).execute()
            if (response.isSuccessful) {
                it.onNext(true)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun removeItem(id: String, model: RemoveItemModel): Flowable<Boolean> {
        return Flowable.create({
            val response = api.getApi().removeItem(id, model).execute()
            if (response.isSuccessful) {
                it.onNext(true)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun getMenuHistory(): Flowable<HistoryModel> {
        return getHistory(api.getApi()::getMenuHistory)
    }

    fun getDiscountByPage(model: BlockPageModel): Flowable<BlockPage> {
        return getBlockPage(model, api.getApi()::getDiscountByPage)
    }

    fun getDiscountHistory(): Flowable<HistoryModel> {
        return getHistory(api.getApi()::getDiscountHistory)
    }

    fun getInfoByPage(model: BlockPageModel): Flowable<BlockPage> {
        return getBlockPage(model, api.getApi()::getAboutByPage)
    }

    fun getInfoHistory(): Flowable<HistoryModel> {
        return getHistory(api.getApi()::getAboutHistory)
    }

    fun getDeliveryByPage(model: BlockPageModel): Flowable<BlockPage> {
        return getBlockPage(model, api.getApi()::getDeliveryByPage)
    }

    fun getDeliveryHistory(): Flowable<HistoryModel> {
        return getHistory(api.getApi()::getDeliveryHistory)
    }

    fun getDishesByCategory(menuItemId: String): Flowable<ArrayList<DishModel>> {
        return Flowable.create({
            val response = api.getApi().getDishesByCategory(menuItemId).execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<ArrayList<DishModel>>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }


    fun getMenuItems(): Flowable<ArrayList<MenuItemModel>> {
        return Flowable.create({
            val response = api.getApi().getMenuItems().execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<ArrayList<MenuItemModel>>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    private fun getBlockPage(model: BlockPageModel, method: (Int, Int) -> Call<ResponseBody>): Flowable<BlockPage> {
        return Flowable.create({
            val response = method(model.index, model.pageSize).execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<BlockPage>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    private fun getHistory(method: () -> Call<ResponseBody>): Flowable<HistoryModel> {
        return Flowable.create({
            val response = method().execute()
            if (response.isSuccessful) {
                val result = if (response.code() == 204) {
                    HistoryModel().apply { timeOfChange = "No content" }
                } else Gson().fromJson<HistoryModel>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

    private fun <T> Response<T>.isSuccessResponse() = isSuccessful && (body() != null)
}


