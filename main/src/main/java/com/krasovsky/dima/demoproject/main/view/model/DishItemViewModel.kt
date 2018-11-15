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
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
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
    val infoLiveData = MutableLiveData<Pair<String, Float>>()
    val totalPriceLiveData = MutableLiveData<Float>()

    val addedSuccess = MutableLiveData<Void>()
    val errorBasket = ClearedLiveData<DialogData>()
    val errorAdding = ClearedLiveData<DialogData>()

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
        infoLiveData.value = Pair(detail.quantity, detail.price)
        count = 1
    }

    private fun notifyCountChanged() {
        totalPriceLiveData.value = targetDetail.price * count
    }

    fun refresh() {
        createBasket()
    }

    private fun createBasket() {
        compositeDisposable.add(basketManager.createBasket()
                .doOnSubscribe {
                    errorBasket.clear()
                    loadingLiveData.call()
                }
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<String>() {
                    override fun onComplete() {
                        addItemToBasket()
                    }

                    override fun onNext(id: String) {
                        basketId = id
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

    fun addToBasket() {
        if (basketId.isEmpty()) {
            createBasket()
        } else addItemToBasket()
    }

    private fun addItemToBasket() {
        compositeDisposable.add(basketManager.addItem(basketId, targetDetail.id, count)
                .doOnSubscribe {
                    errorAdding.clear()
                    loadingLiveData.call()
                }
                .doOnTerminate { loadingLiveData.clear() }
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<BasketModel>() {
                    override fun onComplete() {
                        addedSuccess.value = null
                    }

                    override fun onNext(result: BasketModel) {

                    }

                    override fun onError(e: Throwable) {
                        errorAdding.call(getErrorAddingDialogData())
                    }

                }))
    }

    private fun getErrorAddingDialogData(): DialogData {
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