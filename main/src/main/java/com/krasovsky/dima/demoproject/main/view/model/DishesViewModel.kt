package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.repository.manager.MenuManager
import com.krasovsky.dima.demoproject.repository.model.TypeMenuItems
import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.repository.model.response.DishItemsResponse
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver


class DishesViewModel(application: Application) : BaseAndroidViewModel(application) {

    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()
    var dishes = MutableLiveData<List<DishModel>>()

    private val manager: MenuManager by lazy { MenuManager(RealmManager(), ApiManager(ApiClient())) }

    var categoryItemId = ""
        set(value) {
            field = value
            getDishes()
        }

    fun refresh() {
        getDishes()
    }

    private fun getDishes() {
        compositeDisposable.add(ExecutorUtil.wrapBySchedulers(
                manager.getDishesByCategory(categoryItemId))
                .doOnSubscribe {
                    stateSwiping.value = true
                    liveDataConnection.value = TypeConnection.CLEAR
                }
                .doOnTerminate { stateSwiping.value = false }
                .toObservable()
                .subscribeWith(object : DisposableObserver<DishItemsResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: DishItemsResponse) {
                        processResponse(t.type)
                        dishes.value = t.data
                    }

                    override fun onError(e: Throwable) {
                    }

                })
        )
    }

    private fun processResponse(response: TypeMenuItems) {
        if (response == TypeMenuItems.ERROR_LOADING) {
            liveDataConnection.value = TypeConnection.ERROR_CONNECTION
        }
    }

}