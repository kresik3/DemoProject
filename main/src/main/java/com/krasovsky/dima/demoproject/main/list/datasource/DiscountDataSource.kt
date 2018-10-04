package com.krasovsky.dima.demoproject.main.list.datasource

import com.krasovsky.dima.demoproject.main.list.datasource.base.BaseDataSource
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.typeDiscount
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.disposables.CompositeDisposable

class DiscountDataSource(private val manager: PagingStorageManager,
                         disposable: CompositeDisposable) : BaseDataSource(disposable) {

    override fun typeFunction() = typeDiscount

    override fun historyFunction() = manager.checkDiscountHistory()

    override fun pageFunction(model: BlockPageModel, typeHistory: Int) = manager.getDiscountByPage(model, typeHistory)

}

