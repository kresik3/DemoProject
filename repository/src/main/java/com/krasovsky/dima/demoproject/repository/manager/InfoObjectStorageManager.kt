package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoadedWithHistory
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeObject
import com.krasovsky.dima.demoproject.repository.model.response.InfoObjectsResponse
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.info.InfoObjectsType
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import io.reactivex.Flowable

class InfoObjectStorageManager(val realmManager: RealmManager,
                               val apiManager: ApiManager) {

    fun checkDiscountHistory(): Flowable<TypeLoadedWithHistory> {
        return checkHistory(apiManager::getDiscountHistory, TypeObject.TYPE_DISCOUNT.name)
    }

    fun checkInfoHistory(): Flowable<TypeLoadedWithHistory> {
        return checkHistory(apiManager::getInfoHistory, TypeObject.TYPE_INFO.name)
    }

    fun checkDeliveryHistory(): Flowable<TypeLoadedWithHistory> {
        return checkHistory(apiManager::getDeliveryHistory, TypeObject.TYPE_DELIVERY.name)
    }

    private fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<TypeLoadedWithHistory> {
        return request().map { history ->
            if (mapHistory(history, type)) TypeLoadedWithHistory.CLEAR_DB else TypeLoadedWithHistory.NOT_NEED_LOAD
        }.onErrorResumeNext(Flowable.just(TypeLoadedWithHistory.ERROR_LOAD_HISTORY))
    }

    fun getDiscountItems(type: TypeLoadedWithHistory): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getDiscount, TypeObject.TYPE_DISCOUNT.name)
    }

    fun getInfoItems(type: TypeLoadedWithHistory): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getInfo, TypeObject.TYPE_INFO.name)
    }

    fun getDeliveryItems(type: TypeLoadedWithHistory): Flowable<InfoObjectsResponse> {
        return getInfoObjects(type, apiManager::getDelivery, TypeObject.TYPE_DELIVERY.name)
    }

    private fun getInfoObjects(typeState: TypeLoadedWithHistory, method: () -> Flowable<ArrayList<BlockInfoObject>>, type: String): Flowable<InfoObjectsResponse> {
        return if (typeState == TypeLoadedWithHistory.CLEAR_DB) {
            method.invoke()
                    .map { mapInfoObjects(it, type)}
                    .map { InfoObjectsResponse(TypeLoaded.SUCCESS_LOADING, it) }
                    .onErrorResumeNext { _: Throwable -> getMenuItemsFromDB(TypeLoaded.ERROR_LOADING, type) }
        } else {
            getMenuItemsFromDB(TypeLoaded.SUCCESS_LOADING, type)
        }
    }

    private fun mapInfoObjects(it: ArrayList<BlockInfoObject>, type: String): ArrayList<BlockInfoObject> {
        realmManager.saveInfoObjects(InfoObjectsType().apply {
            this.type = type
            this.records.addAll(it.subList(0, it.size))
        })
        return it
    }

    private fun getMenuItemsFromDB(type: TypeLoaded, typePage: String): Flowable<InfoObjectsResponse> {
        return realmManager.getInfoObjects(typePage).map { InfoObjectsResponse(type, it) }
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

}