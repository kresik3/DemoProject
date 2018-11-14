package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.repository.manager.InfoObjectStorageManager
import com.krasovsky.dima.demoproject.repository.model.TypeItems
import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.repository.model.response.InfoObjectsResponse
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver


class InfoViewModel(application: Application) : BaseAndroidViewModel(application) {

    private var isErrorLoadHistory = false
    private var isNeedLoading = false

    private val manager: InfoObjectStorageManager by lazy { InfoObjectStorageManager(RealmManager(), ApiManager(ApiClient())) }

    val info = MutableLiveData<List<BlockInfoObject>>()

    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()

    fun refresh() {
        getInfo()
    }

    private fun getInfo() {
        compositeDisposable.add(manager.checkInfoHistory()
                .flatMap(this::flatMapHistory)
                .wrapBySchedulers()
                .doOnSubscribe { clearData() }
                .toObservable()
                .doOnTerminate { stateSwiping.value = false }
                .subscribeWith(object : DisposableObserver<InfoObjectsResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: InfoObjectsResponse) {
                        processResponse(t.type)
                        info.postValue(t.data)
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

    private fun flatMapHistory(type: TypePagePaging): Flowable<InfoObjectsResponse> {
        if (type == TypePagePaging.ERROR_LOAD_HISTORY) isErrorLoadHistory = true
        if (type == TypePagePaging.CLEAR_DB) isNeedLoading = true
        return manager.getInfoItems(type)
    }

    private fun processResponse(response: TypeItems) {
        if (isErrorLoadHistory) {
            liveDataConnection.value = TypeConnection.ERROR_CONNECTION
        } else if (isNeedLoading and (response == TypeItems.ERROR_LOADING)) {
            liveDataConnection.value = TypeConnection.ERROR_LOADED
        }
    }
}