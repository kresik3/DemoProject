package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.paging.PagedList
import android.os.Handler
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.AppStorageManager
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import android.os.Looper.getMainLooper
import com.krasovsky.dima.demoproject.main.list.datasource.DiscountDataSource
import java.util.concurrent.Executor


class DiscountViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: AppStorageManager by lazy { AppStorageManager(RealmManager(), ApiManager(ApiClient())) }
    private val config: PagedList.Config by lazy {
        PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build()
    }
    private var pagedList: PagedList<BlockInfoObject>? = null

    fun getData(): PagedList<BlockInfoObject> {
        if (pagedList == null) {
            pagedList = PagedList.Builder(DiscountDataSource(manager, compositeDisposable), config)
                    .setFetchExecutor(MainThreadExecutor())
                    .setNotifyExecutor(MainThreadExecutor())
                    .build()
        }
        return pagedList!!
    }

    internal inner class MainThreadExecutor : Executor {
        private val mHandler = Handler(getMainLooper())

        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }
}