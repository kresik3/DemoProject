package com.krasovsky.dima.demoproject.storage.retrofit

import com.krasovsky.dima.demoproject.storage.retrofit.model.request.CreateBasketModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.PaymentModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.RemoveItemModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("api/Discount/page/{index}")
    fun getDiscountByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/Discount/all")
    fun getDiscount(): Call<ResponseBody>

    @GET("api/About/page/{index}")
    fun getAboutByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/About/all")
    fun getAbout(): Call<ResponseBody>

    @GET("api/Delivery/page/{index}")
    fun getDeliveryByPage(@Path("index") index: Int, @Query("pageSize") pageSize: Int): Call<ResponseBody>

    @GET("api/Delivery/all")
    fun getDelivery(): Call<ResponseBody>

    @GET("api/Discount/history")
    fun getDiscountHistory(): Call<ResponseBody>

    @GET("api/About/history")
    fun getAboutHistory(): Call<ResponseBody>

    @GET("api/Delivery/history")
    fun getDeliveryHistory(): Call<ResponseBody>

    @GET("api/{menuItemId}/shopitem/all")
    fun getDishesByCategory(@Path("menuItemId") menuItemId: String): Call<ResponseBody>

    @GET("api/MenuItem/all")
    fun getMenuItems(): Call<ResponseBody>

    @GET("api/MenuItem/history")
    fun getMenuHistory(): Call<ResponseBody>

    @POST("api/Order")
    fun makeOrder(@Body body: PaymentModel): Call<ResponseBody>

    @POST("api/Cart")
    fun createBasket(@Body body: CreateBasketModel): Call<ResponseBody>

    @GET("api/Cart/{id}")
    fun getBasket(@Path("id") id: String): Call<ResponseBody>

    @POST("api/Cart/{id}/addItem")
    fun addItem(@Path("id") id: String, @Body mode: DishItemModel): Call<ResponseBody>

    @POST("api/Cart/{id}/removeItem")
    fun removeItem(@Path("id") id: String, @Body model: RemoveItemModel): Call<ResponseBody>
}