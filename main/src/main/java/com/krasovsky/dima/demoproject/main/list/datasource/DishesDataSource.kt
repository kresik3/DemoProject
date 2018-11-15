package com.krasovsky.dima.demoproject.main.list.datasource

import android.arch.paging.PageKeyedDataSource
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import com.krasovsky.dima.demoproject.repository.manager.PagingStorageManager
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.paging.DishesPage
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishesPageModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class DishesDataSource (private val disposable: CompositeDisposable,
                        private val manager: PagingStorageManager,
                        private val categoryId: String) : PageKeyedDataSource<Int, DishModel>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DishModel>) {

    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int?, DishModel>) {
        disposable.add(manager.getDishesFirstPage(generateBlockPageModel(categoryId, 1, params.requestedLoadSize))
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<DishesPage>() {
                    override fun onComplete() {
                    }

                    override fun onNext(model: DishesPage) {
                        callback.onResult(model.records, null, generateNextKey(model))
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int?, DishModel>) {
        disposable.add(manager.getDishesByPage(generateBlockPageModel(categoryId, params.key, params.requestedLoadSize))
                .wrapBySchedulers()
                .toObservable()
                .subscribeWith(object : DisposableObserver<DishesPage>() {
                    override fun onComplete() {
                    }

                    override fun onNext(model: DishesPage) {
                        callback.onResult(model.records, generateNextKey(model))
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    private fun generateBlockPageModel(categoryId: String, index: Int, pageSize: Int): DishesPageModel {
        return DishesPageModel(categoryId, index, pageSize)
    }

    private fun generateNextKey(model: DishesPage): Int? {
        return if (model.records.isEmpty() or (model.totalPages <= model.currentPage)) null else model.currentPage + 1
    }

}