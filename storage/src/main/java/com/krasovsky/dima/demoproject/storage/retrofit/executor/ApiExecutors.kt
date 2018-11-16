package com.krasovsky.dima.demoproject.storage.retrofit.executor

import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import java.lang.reflect.Type

class ApiExecutors {

    fun <T> executeRequest(type: Type, method: () -> Call<ResponseBody>): Flowable<T> {
        return Flowable.create({
            val response = method().execute()
            if (response.isSuccessful) {
                val result = Gson().fromJson<T>(response.body()!!.string(), type)
                it.onNext(result)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

    fun executeBooleanRequest(method: () -> Call<ResponseBody>): Flowable<Boolean> {
        return Flowable.create({
            val response = method().execute()
            if (response.isSuccessful) {
                it.onNext(true)
                it.onComplete()
            } else it.tryOnError(Throwable(response.message()))
        }, BackpressureStrategy.BUFFER)
    }

}


