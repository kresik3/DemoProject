package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.krasovsky.dima.demoproject.main.list.datasource.DishesDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.DishesDataSourceModel
import com.krasovsky.dima.demoproject.main.list.datasource.model.list.RecyclerViewStateModel
import com.krasovsky.dima.demoproject.main.util.MainThreadExecutor
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeConnection
import com.krasovsky.dima.demoproject.repository.manager.MenuManager
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.realm.PagingRealmManager
import io.reactivex.observers.DisposableObserver


class DishesViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: PagingStorageManager by lazy { PagingStorageManager(PagingRealmManager(), ApiManager(ApiClient())) }
    private val config: PagedList.Config by lazy {
        PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build()
    }

    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()
    val stateList = RecyclerViewStateModel().apply {
        stateLoading = MutableLiveData()
        stateEmpty = MutableLiveData()
    }

    private var pagedList: PagedList<DishModel>? = null

    var categoryItemId = ""

    fun getData(): PagedList<DishModel> {
        if (pagedList == null) {
            refresh()
        }
        return pagedList!!
    }

    fun refresh(): PagedList<DishModel> {
        pagedList = PagedList.Builder(getDataSource(), config)
                .setFetchExecutor(MainThreadExecutor())
                .setNotifyExecutor(MainThreadExecutor())
                .build()
        return pagedList!!
    }

    private fun getDataSource(): DishesDataSource {
        return DishesDataSource(getDataSourceModel())
    }

    private fun getDataSourceModel(): DishesDataSourceModel<PagingStorageManager> {
        return DishesDataSourceModel(categoryItemId, compositeDisposable, manager)
                .apply {
                    liveDataConnection = this@DishesViewModel.liveDataConnection
                    stateSwiping = this@DishesViewModel.stateSwiping
                    listState = this@DishesViewModel.stateList
                }
    }

}