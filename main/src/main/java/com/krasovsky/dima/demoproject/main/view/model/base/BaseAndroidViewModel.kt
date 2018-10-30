package com.krasovsky.dima.demoproject.main.view.model.base

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.krasovsky.dima.demoproject.main.view.model.livedata.ClearedLiveData
import io.reactivex.disposables.CompositeDisposable

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    open var loadingLiveData: ClearedLiveData<Void> = ClearedLiveData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
