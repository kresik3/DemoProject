package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.repository.manager.LocalManager
import com.krasovsky.dima.demoproject.storage.realm.PreparingRealmManager
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

class SplashViewModel(application: Application) : BaseAndroidViewModel(application) {

    val initializingData = MutableLiveData<Boolean>()

    init {
        startHandler()
    }

    private fun startHandler() {
        clearCash()
        launch(UI) {
            delay(MIN_TIME_DELAY)
            initializingData.postValue(true)
        }
    }

    private fun clearCash() {
        launch(UI) {
            val manager = LocalManager(PreparingRealmManager())
            val isFullCashFile = async { PicassoUtil.cashSizeFull(getApplication()) }
            async {
                manager.resetDishesState()
                val images = manager.getAllImagesString()
                if (isFullCashFile.await()) {
                    PicassoUtil.clearOldImages(images, getApplication())
                }
            }
        }

    }
}