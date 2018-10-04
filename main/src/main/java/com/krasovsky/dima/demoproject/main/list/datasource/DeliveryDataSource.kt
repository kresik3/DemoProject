package com.krasovsky.dima.demoproject.main.list.datasource

import com.krasovsky.dima.demoproject.main.list.datasource.base.BaseDataSource
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.typeDelivery
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.disposables.CompositeDisposable

class DeliveryDataSource(private val manager: PagingStorageManager,
                         disposable: CompositeDisposable) : BaseDataSource(disposable) {

    override fun typeFunction() = typeDelivery

    override fun historyFunction() = manager.checkDeliveryHistory()

    override fun pageFunction(model: BlockPageModel, typeHistory: Int) = manager.getDeliveryByPage(model, typeHistory)

}

