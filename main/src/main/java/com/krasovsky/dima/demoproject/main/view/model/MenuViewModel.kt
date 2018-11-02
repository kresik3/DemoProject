package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.repository.manager.MenuManager
import com.krasovsky.dima.demoproject.repository.model.TypeItems
import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver


class MenuViewModel(application: Application) : BaseAndroidViewModel(application) {

    private var isErrorLoadHistory = false
    private var isNeedLoading = false

    private var menuItems = MutableLiveData<List<MenuItemModel>>()
    private val manager: MenuManager by lazy { MenuManager(RealmManager(), ApiManager(ApiClient())) }
    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()

    init {
        getMenuFomStorage()
    }

    fun getMenu(): MutableLiveData<List<MenuItemModel>> {
        return menuItems
    }

    fun refresh() {
        getMenuFomStorage()
    }

    private fun getMenuFomStorage() {
        compositeDisposable.add(manager.checkMenuHistory()
                .flatMap(this::flatMapHistory)
                .wrapBySchedulers()
                .doOnSubscribe { clearData() }
                .toObservable()
                .doOnTerminate { stateSwiping.value = false }
                .subscribeWith(object : DisposableObserver<MenuItemsResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: MenuItemsResponse) {
                        processResponse(t.type)
                        menuItems.postValue(t.data)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    private fun clearData() {
        stateSwiping.value = true
        isErrorLoadHistory = false
        isNeedLoading = false
        liveDataConnection.value = TypeConnection.CLEAR
    }

    private fun flatMapHistory(type: TypePagePaging): Flowable<MenuItemsResponse> {
        if (type == TypePagePaging.ERROR_LOAD_HISTORY) isErrorLoadHistory = true
        if (type == TypePagePaging.CLEAR_DB) isNeedLoading = true
        return manager.getMenuItems(type)
    }

    private fun processResponse(response: TypeItems) {
        if (isErrorLoadHistory) {
            liveDataConnection.value = TypeConnection.ERROR_CONNECTION
        } else if (isNeedLoading and (response == TypeItems.ERROR_LOADING)) {
            liveDataConnection.value = TypeConnection.ERROR_LOADED
        }
    }

}