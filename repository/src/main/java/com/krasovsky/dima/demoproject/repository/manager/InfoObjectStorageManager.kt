package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.InfoObjectsResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.page.InfoObjectsType
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import io.reactivex.Flowable

class InfoObjectStorageManager(val realmManager: RealmManager,
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

    private fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<TypePagePaging> {
        return request().map { history ->
            if (mapHistory(history, type)) TypePagePaging.CLEAR_DB else TypePagePaging.NOT_NEED_LOAD
        }.onErrorResumeNext(Flowable.just(TypePagePaging.ERROR_LOAD_HISTORY))
    }

    fun getDiscountItems(type: TypePagePaging): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getDiscount, TypeObject.TYPE_DISCOUNT.nameType)
    }

    fun getInfoItems(type: TypePagePaging): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getInfo, TypeObject.TYPE_INFO.nameType)
    }

    fun getDeliveryItems(type: TypePagePaging): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getDelivery, TypeObject.TYPE_DELIVERY.nameType)
    }

    private fun getInfoObjects(typeState: TypePagePaging, method: () -> Flowable<ArrayList<BlockInfoObject>>, type: String): Flowable<InfoObjectsResponse> {
        return if (typeState == TypePagePaging.CLEAR_DB) {
            method.invoke()
                    .map { mapInfoObjects(it, type)}
                    .map { InfoObjectsResponse(TypeItems.SUCCESS_LOADING, it) }
                    .onErrorResumeNext { _: Throwable -> getMenuItemsFromDB(TypeItems.ERROR_LOADING, type) }
        } else {
            getMenuItemsFromDB(TypeItems.SUCCESS_LOADING, type)
        }
    }

    private fun mapInfoObjects(it: ArrayList<BlockInfoObject>, type: String): ArrayList<BlockInfoObject> {
        realmManager.saveInfoObjects(InfoObjectsType().apply {
            this.type = type
            this.records.addAll(it.subList(0, it.size))
        })
        return it
    }

    private fun getMenuItemsFromDB(type: TypeItems, typePage: String): Flowable<InfoObjectsResponse> {
        return realmManager.getInfoObjects(typePage).map { InfoObjectsResponse(type, it) }
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

}