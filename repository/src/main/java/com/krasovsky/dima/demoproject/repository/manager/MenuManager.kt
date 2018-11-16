package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoadedWithHistory
import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeObject
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import io.reactivex.Flowable

class MenuManager(val realmManager: RealmManager,
                  val apiManager: ApiManager) {

    fun checkMenuHistory(): Flowable<TypeLoadedWithHistory> {
        return checkHistory(apiManager::getMenuHistory, TypeObject.TYPE_MENU.name)
    }

    fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<TypeLoadedWithHistory> {
        return request().map { history ->
            if (mapHistory(history, type)) TypeLoadedWithHistory.CLEAR_DB else TypeLoadedWithHistory.NOT_NEED_LOAD
        }.onErrorResumeNext(Flowable.just(TypeLoadedWithHistory.ERROR_LOAD_HISTORY))
    }

    fun getMenuItems(type: TypeLoadedWithHistory): Flowable<MenuItemsResponse> {
        return if (type == TypeLoadedWithHistory.CLEAR_DB) {
            apiManager.getMenuItems()
                    .map(this::mapMenuItems)
                    .map { MenuItemsResponse(TypeLoaded.SUCCESS_LOADING, it) }
                    .onErrorResumeNext { _: Throwable -> getMenuItemsFromDB(TypeLoaded.ERROR_LOADING) }
        } else {
            getMenuItemsFromDB(TypeLoaded.SUCCESS_LOADING)
        }
    }

    private fun mapMenuItems(it: ArrayList<MenuItemModel>): ArrayList<MenuItemModel> {
        it.forEach { it.order }
        realmManager.saveMenuItems(it)
        return it
    }

    private fun getMenuItemsFromDB(type: TypeLoaded): Flowable<MenuItemsResponse> {
        return realmManager.getMenuItems().map { MenuItemsResponse(type, it) }
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

}