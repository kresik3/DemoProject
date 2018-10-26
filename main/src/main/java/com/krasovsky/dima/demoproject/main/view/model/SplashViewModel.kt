package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.repository.manager.LocalManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*
import kotlin.properties.Delegates


private const val MIN_TIME_DELAY = 2000L
private const val readyLine = 2

class SplashViewModel(application: Application) : BaseAndroidViewModel(application) {

    val initializingData = MutableLiveData<Boolean>()
    val time: Long = Date().time
    var isAppReady: Int by Delegates.observable(0) { prop, old, new ->
        if (new == readyLine) {
            initializingData.postValue(true)
        }
    }

    init {
        createBasket()
        startHandler()
    }

    private fun startHandler() {
        launch(UI) {
            val manager = LocalManager(RealmManager())
            val result = async {
                manager.resetDishesState()
                if (manager.isDataChanged()) {
                    PicassoUtil.clearOldImages(manager.getAllImagesString(), getApplication())
                }
                if (Date().time - time < MIN_TIME_DELAY) {
                    delay(MIN_TIME_DELAY - (Date().time - time))
                }
            }.await()
            isAppReady += 1
        }
    }

    private fun createBasket() {
        val basketManager = BasketManager(RealmManager(), ApiManager(ApiClient()))
        compositeDisposable.add(basketManager.createBasket()
                .toObservable()
                .subscribeWith(object : DisposableObserver<String>() {
                    override fun onComplete() {
                        isAppReady += 1
                    }

                    override fun onNext(id: String) {
                        basketId = id
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

}