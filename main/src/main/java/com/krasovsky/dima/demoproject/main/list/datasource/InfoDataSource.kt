package com.krasovsky.dima.demoproject.main.list.datasource

import com.krasovsky.dima.demoproject.main.list.datasource.base.BaseDataSource
import com.krasovsky.dima.demoproject.repository.manager.AppStorageManager
import com.krasovsky.dima.demoproject.storage.model.typeInfo
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.disposables.CompositeDisposable

class InfoDataSource(private val manager: AppStorageManager,
                     disposable: CompositeDisposable) : BaseDataSource(disposable) {

    override fun typeFunction() = typeInfo

    override fun historyFunction() = manager.checkInfoHistory()

    override fun pageFunction(model: BlockPageModel, typeHistory: Int) = manager.getInfoByPage(model, typeHistory)

}

