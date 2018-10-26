package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockPage
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable

class PagingStorageManager(val realmManager: RealmManager,
                           val apiManager: ApiManager) {

    fun checkDiscountHistory(): Flowable<TypePagePaging> {
        return checkHistory(apiManager::getDiscountHistory, TypeObject.TYPE_DISCOUNT.nameType)
    }

    fun checkInfoHistory(): Flowable<TypePagePaging> {
        return checkHistory(apiManager::getInfoHistory, TypeObject.TYPE_INFO.nameType)
    }

    fun checkDeliveryHistory(): Flowable<TypePagePaging> {
        return checkHistory(apiManager::getDeliveryHistory, TypeObject.TYPE_DELIVERY.nameType)
    }

    fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<TypePagePaging> {
        return request().map { history ->
            if (mapHistory(history, type)) TypePagePaging.CLEAR_DB else TypePagePaging.NOT_NEED_LOAD
        }.onErrorResumeNext(Flowable.just(TypePagePaging.ERROR_LOAD_HISTORY))
    }

    fun getDiscountByPage(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getDiscountByPage, model, typeHistory)
    }

    fun getInfoByPage(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getInfoByPage, model, typeHistory)
    }

    fun getDeliveryByPage(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return flatMapLoadPage(apiManager::getDeliveryByPage, model, typeHistory)
    }

    private fun flatMapLoadPage(request: (BlockPageModel) -> Flowable<BlockPage>,
                                model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return when (typeHistory) {
            TypePagePaging.NEED_LOAD, TypePagePaging.CLEAR_DB -> {
                loadFromServer(request, model, typeHistory)
                        .onErrorResumeNext { _: Throwable -> onErrorLoading(model, typeHistory) }
            }
            TypePagePaging.NOT_NEED_LOAD -> {
                if (realmManager.isExistBlockPage(model)) {
                    loadFromDB(model, typeHistory)
                } else {
                    loadFromServer(request, model, typeHistory).onErrorResumeNext(emptyResponse(typeHistory))
                }
            }
            TypePagePaging.ERROR_LOAD_HISTORY, TypePagePaging.ERROR_LOADED -> loadFromDB(model, typeHistory)
        }
    }

    private fun loadFromServer(request: (BlockPageModel) -> Flowable<BlockPage>,
                               model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return request(model)
                .map { blockPage ->
                    clearDB(typeHistory, model.type)
                    BlockPageResponse(TypePagePaging.NEED_LOAD, mapBlockPage(blockPage, model.type))
                }
    }

    private fun loadFromDB(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return realmManager.getBlockPage(model).map { BlockPageResponse(typeHistory, it) }
    }

    private fun onErrorLoading(model: BlockPageModel, typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return when (typeHistory) {
            TypePagePaging.NEED_LOAD -> Flowable.just(BlockPageResponse(typeHistory, BlockPage()))
            TypePagePaging.CLEAR_DB -> realmManager.getBlockPage(model).map { BlockPageResponse(TypePagePaging.ERROR_LOADED, it) }
            else -> emptyResponse(typeHistory)
        }
    }

    private fun emptyResponse(typeHistory: TypePagePaging): Flowable<BlockPageResponse> {
        return Flowable.just(BlockPageResponse(typeHistory, BlockPage()))
    }

    private fun clearDB(typeHistory: TypePagePaging, type: String) {
        if (typeHistory == TypePagePaging.CLEAR_DB) {
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