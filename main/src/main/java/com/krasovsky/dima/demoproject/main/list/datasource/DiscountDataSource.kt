package com.krasovsky.dima.demoproject.main.list.datasource

import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.list.datasource.base.BaseDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.storage.model.TypeObject
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.disposables.CompositeDisposable

class DiscountDataSource(private val manager: PagingStorageManager,
                         disposable: CompositeDisposable) :
        BaseDataSource(disposable) {

    override fun typeFunction() = TypeObject.TYPE_DISCOUNT.nameType

    override fun historyFunction() = manager.checkDiscountHistory()

    override fun pageFunction(model: BlockPageModel, typeHistory: TypePagePaging) = manager.getDiscountByPage(model, typeHistory)

}
