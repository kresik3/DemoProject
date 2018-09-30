package com.krasovsky.dima.demoproject.storage.retrofit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.krasovsky.dima.demoproject.storage.model.BlockPage
import com.krasovsky.dima.demoproject.storage.model.HistoryModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ApiManager(private val api: ApiClient) {

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

    private fun getBlockPage(model: BlockPageModel, method: (Int, Int) -> Call<ResponseBody>): Flowable<BlockPage> {
        return Flowable.create({
            val response = method(model.index, model.pageSize).execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<BlockPage>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.onError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    private fun getHistory(method: () -> Call<ResponseBody>): Flowable<HistoryModel> {
        return Flowable.create({
            val response = method().execute()
            if (response.isSuccessResponse()) {
                val result = Gson().fromJson<HistoryModel>(response.body()!!.string())
                it.onNext(result)
                it.onComplete()
            } else it.onError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

    private fun <T> Response<T>.isSuccessResponse() = isSuccessful && (body() != null)
}


