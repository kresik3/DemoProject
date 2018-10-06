package com.krasovsky.dima.demoproject.main.list.datasource.base

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.storage.model.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver


abstract class BaseDataSource(private val disposable: CompositeDisposable,
                              private val liveDataConnection: MutableLiveData<TypeConnection>,
                              private val stateSwiping: MutableLiveData<Boolean>,
                              private val stateLoading: MutableLiveData<Boolean>) : PageKeyedDataSource<Int, BlockInfoObject>() {

    private var isErrorLoadHistory = false
    private var isNeedLoading = false
    private var isErrorLoaded = false

    protected var pages = 0
    protected lateinit var typeResponse: TypePagePaging

    abstract fun typeFunction(): String
    abstract fun historyFunction(): Flowable<TypePagePaging>
    abstract fun pageFunction(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse>

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int?, BlockInfoObject>) {
        liveDataConnection.value = TypeConnection.CLEAR
        stateSwiping.value = true
        stateLoading.value = false
        disposable.add(ExecutorUtil.wrapBySchedulers(historyFunction().flatMap {
            if (it == TypePagePaging.ERROR_LOAD_HISTORY) isErrorLoadHistory = true
            if (it == TypePagePaging.CLEAR_DB) isNeedLoading = true
            pageFunction(generateBlockPageModel(1, params.requestedLoadSize, typeFunction()), it)
        }.map(this::getParamsResponse))
                .toObservable()
                .subscribeWith(object : DisposableObserver<Pair<Int, List<BlockInfoObject>>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Pair<Int, List<BlockInfoObject>>) {
                        stateSwiping.value = false
                        buildResponse()
                        callback.onResult(t.second, null, generateNextKey(t.first, t.second))
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int?, BlockInfoObject>) {
        stateLoading.value = true
        disposable.add(ExecutorUtil.wrapBySchedulers(pageFunction(
                generateBlockPageModel(params.key, params.requestedLoadSize, typeFunction()), typeResponse)
                .map(this::getParamsResponse))
                .toObservable()
                .subscribeWith(object : DisposableObserver<Pair<Int, List<BlockInfoObject>>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Pair<Int, List<BlockInfoObject>>) {
                        stateLoading.value = false
                        buildResponse()
                        callback.onResult(t.second, generateNextKey(t.first, t.second))
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    private fun buildResponse() {
        if (isErrorLoadHistory) {
            liveDataConnection.value = TypeConnection.ERROR_CONNECTION
        } else if (isNeedLoading) {
            if (isErrorLoaded) {
                liveDataConnection.value = TypeConnection.ERROR_LOADED
            }
        }
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
        return Pair(response.data.currentPage, (response.data.records as List<BlockInfoObject>).sortedBy { it.order })
    }
}

