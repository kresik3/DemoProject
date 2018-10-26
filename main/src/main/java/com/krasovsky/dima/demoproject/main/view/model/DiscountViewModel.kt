package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.DiscountDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil


class DiscountViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: PagingStorageManager by lazy { PagingStorageManager(RealmManager(), ApiManager(ApiClient())) }
    private val config: PagedList.Config by lazy {
        PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build()
    }
    private var pagedList: PagedList<BlockInfoObject>? = null
    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()
    val stateLoading = MutableLiveData<Boolean>()

    fun getData(): PagedList<BlockInfoObject> {
        if (pagedList == null) {
            refresh()
        }
        return pagedList!!
    }

    fun refresh(): PagedList<BlockInfoObject> {
        pagedList = PagedList.Builder(getDataSource(), config)
                .setFetchExecutor(ExecutorUtil.MainThreadExecutor())
                .setNotifyExecutor(ExecutorUtil.MainThreadExecutor())
                .build()
        return pagedList!!
    }

    private fun getDataSource(): DiscountDataSource {
        return DiscountDataSource(manager, compositeDisposable)
                .apply {
                    liveDataConnection = this@DiscountViewModel.liveDataConnection
                    stateSwiping = this@DiscountViewModel.stateSwiping
                    stateLoading = this@DiscountViewModel.stateLoading
                }
    }

}