package com.krasovsky.dima.demoproject.main.list.datasource.base

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver


abstract class BaseDataSource(private val disposable: CompositeDisposable) : PageKeyedDataSource<Int, BlockInfoObject>() {

    open var liveDataConnection: MutableLiveData<TypeConnection>? = null
    open var stateSwiping: MutableLiveData<Boolean>? = null
    open var stateLoading: MutableLiveData<Boolean>? = null

    private var isErrorLoadHistory = false
    private var isNeedLoading = false
    private var isErrorLoaded = false

    private var pages = 0
    private lateinit var typeResponse: TypePagePaging

    abstract fun typeFunction(): String
    abstract fun historyFunction(): Flowable<TypePagePaging>
    abstract fun pageFunction(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse>

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int?, BlockInfoObject>) {
        clearData()
        disposable.add(ExecutorUtil.wrapBySchedulers(historyFunction()
                .flatMap { flatMapHistory(it, params.requestedLoadSize) }
                .map(this::getParamsResponse))
                .toObservable()
                .subscribeWith(object : DisposableObserver<Pair<Int, List<BlockInfoObject>>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Pair<Int, List<BlockInfoObject>>) {
                        stateSwiping?.value = false
                        handleResponse()
                        callback.onResult(t.second, null, generateNextKey(t.first, t.second))
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int?, BlockInfoObject>) {
        stateLoading?.value = true
        disposable.add(ExecutorUtil.wrapBySchedulers(pageFunction(
                generateBlockPageModel(params.key, params.requestedLoadSize, typeFunction()), typeResponse)
                .map(this::getParamsResponse))
                .toObservable()
                .subscribeWith(object : DisposableObserver<Pair<Int, List<BlockInfoObject>>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Pair<Int, List<BlockInfoObject>>) {
                        stateLoading?.value = false
                        handleResponse()
                        callback.onResult(t.second, generateNextKey(t.first, t.second))
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    private fun clearData() {
        liveDataConnection?.value = TypeConnection.CLEAR
        stateSwiping?.value = true
        stateLoading?.value = false
    }

    private fun handleResponse() {
        if (isErrorLoadHistory) {
            liveDataConnection?.value = TypeConnection.ERROR_CONNECTION
        } else if (isNeedLoading and isErrorLoaded) {
            liveDataConnection?.value = TypeConnection.ERROR_LOADED
        }
    }

    private fun flatMapHistory(type: TypePagePaging, loadSize: Int): Flowable<BlockPageResponse> {
        if (type == TypePagePaging.ERROR_LOAD_HISTORY) isErrorLoadHistory = true
        if (type == TypePagePaging.CLEAR_DB) isNeedLoading = true
        return pageFunction(generateBlockPageModel(1, loadSize, typeFunction()), type)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BlockInfoObject>) {
    }

    private fun generateNextKey(key: Int, data: List<BlockInfoObject>): Int? {
        return if (data.isEmpty() or (pages <= key)) null else key + 1
    }

    private fun generateBlockPageModel(index: Int, pageSize: Int, type: String): BlockPageModel {
        return BlockPageModel(index, pageSize, type)
    }

    private fun getParamsResponse(response: BlockPageResponse): Pair<Int, List<BlockInfoObject>> {
        if (response.type == TypePagePaging.ERROR_LOADED) isErrorLoaded = true
        typeResponse = response.type
        pages = response.data.totalPages
        val sortedData = (response.data.records as List<BlockInfoObject>)
                .sortedBy { it.order }
                .also { sorterList -> sorterList.forEach { it.items.sortBy { subItem -> subItem.subOrder } } }
        return Pair(response.data.currentPage, sortedData)
    }
}

