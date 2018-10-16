package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.util.Log
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.InfoDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.main.view.activity.MenuActivity
import com.krasovsky.dima.demoproject.repository.manager.LocalManager
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import java.util.*


private const val MIN_TIME_DELAY = 2000L

class SplashViewModel(application: Application) : BaseAndroidViewModel(application) {

    val initializingData = MutableLiveData<Boolean>()
    val time: Long

    init {
        time = Date().time
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
            initializingData.postValue(true)
        }
    }

}