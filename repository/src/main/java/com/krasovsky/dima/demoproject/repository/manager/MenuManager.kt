package com.krasovsky.dima.demoproject.repository.manager

import android.util.Log
import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.BlockPageResponse
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MenuManager(val realmManager: RealmManager,
                  val apiManager: ApiManager) {

    fun checkMenuHistory(): Flowable<TypePagePaging> {
        return checkHistory(apiManager::getMenuHistory, TypeObject.TYPE_MENU.nameType)
    }

    fun checkHistory(request: () -> Flowable<HistoryModel>, type: String): Flowable<TypePagePaging> {
        return request().map { history ->
            if (mapHistory(history, type)) TypePagePaging.CLEAR_DB else TypePagePaging.NOT_NEED_LOAD
        }.onErrorResumeNext(Flowable.just(TypePagePaging.ERROR_LOAD_HISTORY))
    }

    fun getMenuItems(type: TypePagePaging): Flowable<MenuItemsResponse> {
        return if (type == TypePagePaging.CLEAR_DB) {
            apiManager.getMenuItems()
                    .map(this::mapMenuItems)
                    .map { MenuItemsResponse(TypeMenuItems.SUCCESS_LOADING, it) }
                    .onErrorResumeNext { _: Throwable -> getMenuItemsFromDB(TypeMenuItems.ERROR_LOADING) }
        } else {
            getMenuItemsFromDB(TypeMenuItems.SUCCESS_LOADING)
        }
    }

    private fun mapMenuItems(it: ArrayList<MenuItemModel>): ArrayList<MenuItemModel> {
        realmManager.saveMenuItems(it)
        return it
    }

    private fun getMenuItemsFromDB(type: TypeMenuItems): Flowable<MenuItemsResponse> {
        return realmManager.getMenuItems().map { MenuItemsResponse(type, it) }
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

}