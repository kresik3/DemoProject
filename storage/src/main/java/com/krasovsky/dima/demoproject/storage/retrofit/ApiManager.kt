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
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.CreateBasketModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.RemoveItemModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ApiManager(private val api: ApiClient) {

    fun createBasket(model: CreateBasketModel): Flowable<String> {
        return Flowable.create({
            val response = api.getApi().createBasket(model).execute()
            if (response.isSuccessful) {
                it.onNext(model.id)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun getBasket(id: String): Flowable<BasketModel> {
        return Flowable.create({
           /* val response = "{\n" +
                    "      \"id\": \"3d0a1381-a20a-4d36-af9f-5ab2316e6c22\",\n" +
                    "      \"items\": [\n" +
                    "        {\n" +
                    "          \"shopItemDetailId\": \"65c66da8-118a-4b39-d7c6-08d636b32780\",\n" +
                    "          \"count\": 3,\n" +
                    "          \"price\": 11.00,\n" +
                    "          \"title\": \"Пицца #1\",\n" +
                    "          \"kind\": \"Большая\",\n" +
                    "          \"imagePath\": \"Images/image-placeholder.png\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"shopItemDetailId\": \"18bb8df9-71ae-4e26-d7cb-08d636b32780\",\n" +
                    "          \"count\": 2,\n" +
                    "          \"price\": 13.00,\n" +
                    "          \"title\": \"Пицца #3\",\n" +
                    "          \"kind\": \"Большая\",\n" +
                    "          \"imagePath\": \"Images/image-placeholder.png\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"shopItemDetailId\": \"4bba25c1-310f-4d8f-d7d8-08d636b32780\",\n" +
                    "          \"count\": 2,\n" +
                    "          \"price\": 2.50,\n" +
                    "          \"title\": \"Пирог #2\",\n" +
                    "          \"kind\": \"Порция\",\n" +
                    "          \"imagePath\": \"Images/image-placeholder.png\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"shopItemDetailId\": \"9f1166d9-2003-4a57-d7db-08d636b32780\",\n" +
                    "          \"count\": 1,\n" +
                    "          \"price\": 2.80,\n" +
                    "          \"title\": \"Напиток #2\",\n" +
                    "          \"kind\": \"Большая\",\n" +
                    "          \"imagePath\": \"Images/image-placeholder.png\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"totalCount\": 8,\n" +
                    "      \"totalPrice\": 66.80\n" +
                    "    }"
            val result = Gson().fromJson<BasketModel>(response)
            it.onNext(result)*/
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


