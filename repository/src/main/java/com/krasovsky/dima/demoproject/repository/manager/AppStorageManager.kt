package com.krasovsky.dima.demoproject.repository.manager

import android.util.Log
import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppStorageManager(val realmManager: RealmManager,
                        val apiManager: ApiManager) {

    fun checkDiscountHistory(): Flowable<Int> {
        return checkHistory(apiManager::getDiscountHistory, typeDiscount)
    }

    fun checkInfoHistory(): Flowable<Int> {
        return checkHistory(apiManager::getInfoHistory, typeInfo)
    }

    fun checkDeliveryHistory(): Flowable<Int> {
        return checkHistory(apiManager::getDeliveryHistory, typeDelivery)
    }

    fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<Int> {
        return request().map { history ->
            if (mapHistory(history, type)) clearDB else notNeedLoad
        }.onErrorResumeNext(Flowable.just(errorLoadHistory))
    }

    fun getDiscountByPage(model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getDiscountByPage, model, typeHistory)
    }

    fun getInfoByPage(model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getInfoByPage, model, typeHistory)
    }

    fun getDeliveryByPage(model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getDeliveryByPage, model, typeHistory)
    }

    private fun flatMapLoadPage(request: (BlockPageModel) -> Flowable<BlockPage>,
                                model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return when (typeHistory) {
            needLoad or clearDB -> {
                loadFromServer(request, model, typeHistory)
                        .onErrorResumeNext { _: Throwable -> onErrorLoading(model, typeHistory) }
            }
            notNeedLoad -> {
                if (realmManager.isExistBlockPage(model)) {
                    loadFromDB(model, typeHistory)
                } else {
                    loadFromServer(request, model, typeHistory).onErrorResumeNext(emptyResponse(typeHistory))
                }
            }
            errorLoadHistory or errorLoaded -> loadFromDB(model, typeHistory)
            else -> emptyResponse(typeHistory)
        }
    }

    private fun loadFromServer(request: (BlockPageModel) -> Flowable<BlockPage>,
                               model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return request(model)
                .map { blockPage ->
                    clearDB(typeHistory, model.type)
                    BlockPageResponse(needLoad, mapBlockPage(blockPage, model.type))
                }
    }

    private fun loadFromDB(model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return realmManager.getBlockPage(model).map { BlockPageResponse(typeHistory, it) }
    }

    private fun onErrorLoading(model: BlockPageModel, typeHistory: Int): Flowable<BlockPageResponse> {
        return when (typeHistory) {
            needLoad -> Flowable.just(BlockPageResponse(typeHistory, BlockPage()))
            clearDB -> realmManager.getBlockPage(model).map { BlockPageResponse(errorLoaded, it) }
            else -> emptyResponse(typeHistory)
        }
    }

    private fun emptyResponse(typeHistory: Int): Flowable<BlockPageResponse> {
        return Flowable.just(BlockPageResponse(typeHistory, BlockPage()))
    }

    private fun clearDB(typeHistory: Int, type: String) {
        if (typeHistory == clearDB) {
            realmManager.clearBlockPageByType(type)
        }
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

    private fun mapBlockPage(it: BlockPage, type: String): BlockPage {
        it.type = type
        realmManager.saveBlockPage(it)
        return it
    }

}