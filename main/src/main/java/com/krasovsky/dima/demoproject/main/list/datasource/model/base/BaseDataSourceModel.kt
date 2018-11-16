package com.krasovsky.dima.demoproject.main.list.datasource.model.base

import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.list.datasource.model.list.RecyclerViewStateModel
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeConnection
import io.reactivex.disposables.CompositeDisposable

open class BaseDataSourceModel<T>(val disposable: CompositeDisposable, val manager: T) {

    var liveDataConnection: MutableLiveData<TypeConnection>? = null
    var stateSwiping: MutableLiveData<Boolean>? = null
    var listState: RecyclerViewStateModel? = null

}