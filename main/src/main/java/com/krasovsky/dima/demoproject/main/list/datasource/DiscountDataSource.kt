package com.krasovsky.dima.demoproject.main.list.datasource

import android.arch.paging.PageKeyedDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.base.BaseDataSource
import com.krasovsky.dima.demoproject.repository.manager.AppStorageManager
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.typeDiscount
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class DiscountDataSource(private val manager: AppStorageManager,
                         disposable: CompositeDisposable) : BaseDataSource(disposable) {

    override fun typeFunction() = typeDiscount

    override fun historyFunction() = manager.checkDiscountHistory()

    override fun pageFunction(model: BlockPageModel, typeHistory: Int) = manager.getDiscountByPage(model, typeHistory)

}

