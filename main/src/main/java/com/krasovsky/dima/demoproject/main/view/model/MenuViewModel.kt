package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.DeliveryDataSource
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.repository.manager.MenuManager
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import io.reactivex.observers.DisposableObserver


class MenuViewModel(application: Application) : BaseAndroidViewModel(application) {

    private var menuItems = MutableLiveData<List<MenuItemModel>>()
    private val manager: MenuManager by lazy { MenuManager(RealmManager(), ApiManager(ApiClient())) }

    init {
        getMenuFomStorage()
    }

    fun getMenu(): MutableLiveData<List<MenuItemModel>> {
        return menuItems
    }

    private fun getMenuFomStorage() {
        compositeDisposable.add(ExecutorUtil.wrapBySchedulers(manager.getMenuItems())
                .toObservable()
                .subscribeWith(object : DisposableObserver<MenuItemsResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: MenuItemsResponse) {
                        menuItems.postValue(t.data)
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

}