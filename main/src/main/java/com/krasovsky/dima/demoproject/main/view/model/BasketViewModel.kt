package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import io.reactivex.observers.DisposableObserver


class BasketViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: BasketManager by lazy { BasketManager(RealmManager(), ApiManager(ApiClient())) }

    val basket = MutableLiveData<BasketModel>()
    val deletedCount = MutableLiveData<Int>()

    init {
        getBasketItems()
    }

    fun getBasketItems() {
        compositeDisposable.add(manager.getBasket(basketId)
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<BasketModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(basketModel: BasketModel) {
                        basket.value = basketModel
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    fun removeItem(model: BasketItemModel, isAll: Boolean) {
        compositeDisposable.add(manager.removeItem(basketId, model.shopItemDetailId, isAll)
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        getBasketItems()
                        deletedCount.value = if (isAll) model.count else 1
                    }

                    override fun onNext(response: Boolean) {

                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }
}