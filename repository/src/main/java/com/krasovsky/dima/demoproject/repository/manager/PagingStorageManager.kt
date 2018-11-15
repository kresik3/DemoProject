package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.repository.model.response.DishesPageResponse
import com.krasovsky.dima.demoproject.storage.model.paging.DishesPage
import com.krasovsky.dima.demoproject.storage.realm.PagingRealmManager
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.DishesPageModel
import io.reactivex.Flowable

class PagingStorageManager(val realmManager: PagingRealmManager,
                           val apiManager: ApiManager) {

    fun getDishesFirstPage(model: DishesPageModel): Flowable<DishesPageResponse> {
        return flatMapLoadFirstPage(apiManager::getDishesByPage, model)
    }

    fun getDishesByPage(model: DishesPageModel): Flowable<DishesPageResponse> {
        return flatMapLoadPage(apiManager::getDishesByPage, model)
    }

    private fun flatMapLoadFirstPage(request: (DishesPageModel) -> Flowable<DishesPage>,
                                     model: DishesPageModel): Flowable<DishesPageResponse> {
        return if (isNeedReloadItems(model.categoryId)) {
            request(model)
                    .map {
                        clearDishesPages(model.categoryId)
                        switchStateDishesPagesCategory(model.categoryId)
                        mapPage(it, model.categoryId)
                    }
                    .onErrorResumeNext { _: Throwable -> Flowable.just(onErrorLoading(model)) }
        } else {
            Flowable.just(DishesPageResponse(TypeLoaded.SUCCESS_LOADING, getDishesPageFromDB(model.categoryId, model.index)))
        }
    }

    private fun flatMapLoadPage(request: (DishesPageModel) -> Flowable<DishesPage>,
                                model: DishesPageModel): Flowable<DishesPageResponse> {
        return if (isExists(model.categoryId, model.index)) {
            Flowable.just(DishesPageResponse(TypeLoaded.SUCCESS_LOADING, getDishesPageFromDB(model.categoryId, model.index)))
        } else {
            request(model)
                    .map { mapPage(it, model.categoryId) }
                    .onErrorResumeNext { _: Throwable -> Flowable.just(onErrorLoading(model)) }
        }
    }

    private fun mapPage(model: DishesPage, categoryItemId: String): DishesPageResponse {
        model.type = categoryItemId
        model.records.forEach { array -> array.details.sortBy { it.subOrder } }

        realmManager.saveDishesPage(model)
        return DishesPageResponse(TypeLoaded.SUCCESS_LOADING, model)
    }

    private fun onErrorLoading(model: DishesPageModel): DishesPageResponse {
        return DishesPageResponse(TypeLoaded.ERROR_LOADING, getDishesPageFromDB(model.categoryId, model.index))
    }

    private fun switchStateDishesPagesCategory(categoryId: String) {
        realmManager.switchStateDishesPagesCategory(categoryId)
    }

    private fun clearDishesPages(categoryId: String) =
            realmManager.clearDishesPages(categoryId)

    private fun getDishesPageFromDB(categoryId: String, page: Int): DishesPage =
            realmManager.getDishedPage(categoryId, page)

    private fun isNeedReloadItems(categoryId: String): Boolean =
            realmManager.isNeedReloadDishesPage(categoryId)

    private fun isExists(categoryId: String, page: Int): Boolean =
            realmManager.isExistsDishesPage(categoryId, page)

}