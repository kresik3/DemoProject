package com.krasovsky.dima.demoproject.storage.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/Discount/page/{index}")
    fun getDiscountByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/About/page/{index}")
    fun getAboutByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/Delivery/page/{index}")
    fun getDeliveryByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/Discount/history")
    fun getDiscountHistory(): Call<ResponseBody>

    @GET("api/About/history")
    fun getAboutHistory(): Call<ResponseBody>

    @GET("api/Delivery/history")
    fun getDeliveryHistory(): Call<ResponseBody>

    @GET("api/MenuItem/all")
    fun getMenuItems(): Call<ResponseBody>

    @GET("api/MenuItem/history")
    fun getMenuHistory(): Call<ResponseBody>

}