package com.krasovsky.dima.demoproject.repository.manager

import android.util.Log
import com.krasovsky.dima.demoproject.repository.model.*
import com.krasovsky.dima.demoproject.repository.model.response.DishItemsResponse
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import io.reactivex.Flowable

class MenuManager(val realmManager: RealmManager,
                  val apiManager: ApiManager) {

    fun getDishesByCategory(categoryItemId: String): Flowable<DishItemsResponse> {
        return if (realmManager.isNeedReloadItems(categoryItemId)) {
            apiManager.getDishesByCategory(categoryItemId)
                    .map { mapDishItems(it, categoryItemId) }
                    .onErrorResumeNext { _: Throwable ->
                        getDishesFromDB(categoryItemId, TypeItems.ERROR_LOADING)
                    }
        } else {
            getDishesFromDB(categoryItemId, TypeItems.SUCCESS_LOADING)
        }
    }

    private fun getDishesFromDB(categoryItemId: String, type: TypeItems): Flowable<DishItemsResponse> =
            Flowable.just(DishItemsResponse(type, realmManager.getDishItems(categoryItemId)))

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
                    .map { MenuItemsResponse(TypeItems.SUCCESS_LOADING, it) }
                    .onErrorResumeNext { _: Throwable -> getMenuItemsFromDB(TypeItems.ERROR_LOADING) }
        } else {
            getMenuItemsFromDB(TypeItems.SUCCESS_LOADING)
        }
    }

    private fun mapMenuItems(it: ArrayList<MenuItemModel>): ArrayList<MenuItemModel> {
        realmManager.saveMenuItems(it)
        return it
    }

    private fun getMenuItemsFromDB(type: TypeItems): Flowable<MenuItemsResponse> {
        return realmManager.getMenuItems().map { MenuItemsResponse(type, it) }
    }

    private fun mapDishItems(model: List<DishModel>, categoryItemId: String): DishItemsResponse {
        model.forEach { it.categoryId = categoryItemId }
        model.forEach { array -> array.details.sortBy { it.subOrder } }

        realmManager.saveDishItems(categoryItemId, model)
        return DishItemsResponse(TypeItems.SUCCESS_LOADING, model)
    }

    private fun mapHistory(it: HistoryModel, type: String): Boolean {
        it.type = type
        return realmManager.checkHistory(it)
    }

}