package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeConnection
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.repository.manager.InfoObjectStorageManager
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoadedWithHistory
import com.krasovsky.dima.demoproject.repository.model.response.InfoObjectsResponse
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver


class DeliveryViewModel(application: Application) : BaseAndroidViewModel(application) {

    private var isErrorLoadHistory = false
    private var isNeedLoading = false

    private val manager: InfoObjectStorageManager by lazy { InfoObjectStorageManager(RealmManager(), ApiManager(ApiClient())) }

    val delivery = MutableLiveData<List<BlockInfoObject>>()

    val liveDataConnection = MutableLiveData<TypeConnection>()
    val stateSwiping = MutableLiveData<Boolean>()


    init {
        getDelivary()
    }

    fun refresh() {
        getDelivary()
    }

    private fun getDelivary() {
        compositeDisposable.add(manager.checkDeliveryHistory()
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
                        delivery.postValue(t.data)
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

    private fun flatMapHistory(type: TypeLoadedWithHistory): Flowable<InfoObjectsResponse> {
        if (type == TypeLoadedWithHistory.ERROR_LOAD_HISTORY) isErrorLoadHistory = true
        if (type == TypeLoadedWithHistory.CLEAR_DB) isNeedLoading = true
        return manager.getDeliveryItems(type)
    }

    private fun processResponse(response: TypeLoaded) {
        if (isErrorLoadHistory) {
            liveDataConnection.value = TypeConnection.ERROR_CONNECTION
        } else if (isNeedLoading and (response == TypeLoaded.ERROR_LOADING)) {
            liveDataConnection.value = TypeConnection.ERROR_LOADED
        }
    }

}