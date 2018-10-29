package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.model.dish.DetailModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import io.reactivex.observers.DisposableObserver
import kotlin.properties.Delegates


class DishItemViewModel(application: Application) : BaseAndroidViewModel(application) {

    val basketManager: BasketManager by lazy {
        BasketManager(RealmManager(), ApiManager(ApiClient()))
    }

    val enableMinusLiveData = MutableLiveData<Boolean>()
    val countLiveData = MutableLiveData<Int>()
    val priceLiveData = MutableLiveData<Float>()
    val totalPriceLiveData = MutableLiveData<Float>()
    val quantityLiveData = MutableLiveData<String>()

    val addedSuccess = MutableLiveData<Void>()

    var count = 1
        set(value) {
            if (value == 1) {
                enableMinusLiveData.value = false
            } else if (value > 1) {
                if (enableMinusLiveData.value == false) enableMinusLiveData.value = true
            }
            field = value
            countLiveData.value = value
            notifyCountChanged()
        }

    var dish: DishModel? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    var targetDetail: DetailModel by Delegates.notNull()

    fun updateTargetDetail(detail: DetailModel) {
        targetDetail = detail
        priceLiveData.value = detail.price
        quantityLiveData.value = detail.quantity
        count = 1
    }

    private fun notifyCountChanged() {
        totalPriceLiveData.value = targetDetail.price * count
    }

    fun addToBasket() {
        compositeDisposable.add(basketManager.addItem(basketId, targetDetail.id, count)
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        addedSuccess.value = null
                    }

                    override fun onNext(result: Boolean) {

                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

}