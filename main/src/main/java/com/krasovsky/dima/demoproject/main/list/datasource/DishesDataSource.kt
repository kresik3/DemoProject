package com.krasovsky.dima.demoproject.main.list.datasource

import android.arch.paging.PageKeyedDataSource
import com.krasovsky.dima.demoproject.main.list.datasource.model.DishesDataSourceModel
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeConnection
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.repository.model.response.DishesPageResponse
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.paging.DishesPage
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishesPageModel
import io.reactivex.observers.DisposableObserver

class DishesDataSource(private val dataSourceModel: DishesDataSourceModel<PagingStorageManager>) : PageKeyedDataSource<Int, DishModel>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DishModel>) {
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int?, DishModel>) {
        dataSourceModel.disposable.add(
                dataSourceModel.manager.getDishesFirstPage(generateBlockPageModel(dataSourceModel.categoryId, 1, params.requestedLoadSize))
                        .wrapBySchedulers()
                        .doOnSubscribe {
                            startFirstData()
                        }
                        .doOnTerminate { dataSourceModel.stateSwiping?.value = false }
                        .toObservable()
                        .subscribeWith(object : DisposableObserver<DishesPageResponse>() {
                            override fun onComplete() {
                            }

                            override fun onNext(model: DishesPageResponse) {
                                processResponse(model.type)
                                dataSourceModel.listState?.stateEmpty?.value = model.data.records.size == 0
                                callback.onResult(model.data.records, null, generateNextKey(model.data))
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                dataSourceModel.liveDataConnection?.value = TypeConnection.ERROR_LOADED
                            }

                        }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int?, DishModel>) {
        dataSourceModel.disposable.add(
                dataSourceModel.manager.getDishesByPage(generateBlockPageModel(dataSourceModel.categoryId, params.key, params.requestedLoadSize))
                        .wrapBySchedulers()
                        .doOnSubscribe {
                            startData()
                        }
                        .doOnTerminate { dataSourceModel.listState?.stateLoading?.value = false }
                        .toObservable()
                        .subscribeWith(object : DisposableObserver<DishesPageResponse>() {
                            override fun onComplete() {
                            }

                            override fun onNext(model: DishesPageResponse) {
                                callback.onResult(model.data.records, generateNextKey(model.data))
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                dataSourceModel.liveDataConnection?.value = TypeConnection.ERROR_LOADED
                            }

                        }))
    }

    private fun startFirstData() {
        dataSourceModel.stateSwiping?.value = true
        dataSourceModel.liveDataConnection?.value = TypeConnection.CLEAR
        dataSourceModel.listState?.stateLoading?.value = false
    }

    private fun startData() {
        dataSourceModel.stateSwiping?.value = false
        dataSourceModel.liveDataConnection?.value = TypeConnection.CLEAR
        dataSourceModel.listState?.stateLoading?.value = true
    }


    private fun processResponse(response: TypeLoaded) {
        if (response == TypeLoaded.ERROR_LOADING) {
            dataSourceModel.liveDataConnection?.value = TypeConnection.ERROR_CONNECTION
        }
    }

    private fun generateBlockPageModel(categoryId: String, index: Int, pageSize: Int): DishesPageModel {
        return DishesPageModel(categoryId, index, pageSize)
    }

    private fun generateNextKey(model: DishesPage): Int? {
        return if (model.records.isEmpty() or (model.totalPages <= model.currentPage)) null else model.currentPage + 1
    }

}