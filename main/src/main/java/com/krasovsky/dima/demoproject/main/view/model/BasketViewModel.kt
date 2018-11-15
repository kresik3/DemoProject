package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.base.dialog.alert.model.DialogData
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.main.view.model.livedata.ClearedLiveData
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.model.basket.BasketItemModel
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import io.reactivex.observers.DisposableObserver


class BasketViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: BasketManager by lazy { BasketManager(RealmManager(), ApiManager(ApiClient())) }

    val errorItems = ClearedLiveData<DialogData>()
    val errorBasket = ClearedLiveData<DialogData>()

    val basket = MutableLiveData<BasketModel>()
    val updateCount = MutableLiveData<Int>()
    val stateSwiping = MutableLiveData<Boolean>()

    init {
        getBasketItems()
    }

    fun refresh() {
        getBasketItems()
    }

    fun getBasketItems() {
        if (basketId.isEmpty()) return
        compositeDisposable.add(manager.getBasket(basketId)
                .wrapBySchedulers()
                .doOnSubscribe {
                    errorBasket.clear()
                    stateSwiping.value = true
                }
                .doOnTerminate {
                    stateSwiping.value = false
                }
                .toObservable()
                .subscribeWith(object : DisposableObserver<BasketModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(basketModel: BasketModel) {
                        basket.value = basketModel
                    }

                    override fun onError(e: Throwable) {
                        errorBasket.call(getErrorBasketDialogData())
                    }

                }))
    }

    private fun getErrorBasketDialogData(): DialogData {
        return with(getApplication<Application>()) {
            DialogData(
                    getString(R.string.title_error),
                    getString(R.string.error_message_with_try_again),
                    getString(R.string.btn_retry),
                    getString(R.string.btn_close)
            )
        }
    }

    fun removeItem(model: BasketItemModel, isAll: Boolean) {
        compositeDisposable.add(manager.removeItem(basketId, model.shopItemDetailId, isAll)
                .wrapBySchedulers()
                .doOnSubscribe {
                    errorItems.clear()
                    loadingLiveData.call()
                }
                .doOnTerminate {
                    loadingLiveData.clear()
                }
                .toObservable()
                .subscribeWith(object : DisposableObserver<BasketModel>() {
                    override fun onComplete() {
                        updateCount.value = -(if (isAll) model.count else 1)
                    }

                    override fun onNext(basketModel: BasketModel) {
                        basket.value = basketModel
                    }

                    override fun onError(e: Throwable) {
                        errorItems.call(getErrorItemsDialogData())
                    }

                }))
    }

    fun addItem(model: BasketItemModel, isAll: Boolean) {
        val count = if (isAll) model.count else 1
        compositeDisposable.add(manager.addItem(basketId, model.shopItemDetailId, count)
                .wrapBySchedulers()
                .doOnSubscribe {
                    errorItems.clear()
                    loadingLiveData.call()
                }
                .doOnTerminate {
                    loadingLiveData.clear()
                }
                .toObservable()
                .subscribeWith(object : DisposableObserver<BasketModel>() {
                    override fun onComplete() {
                        updateCount.value = count
                    }

                    override fun onNext(basketModel: BasketModel) {
                        basket.value = basketModel
                    }

                    override fun onError(e: Throwable) {
                        errorItems.call(getErrorItemsDialogData())
                    }

                }))
    }

    private fun getErrorItemsDialogData(): DialogData {
        return with(getApplication<Application>()) {
            DialogData(
                    getString(R.string.title_error),
                    getString(R.string.error_message_try_again),
                    getString(R.string.btn_close),
                    null
            )
        }
    }
}