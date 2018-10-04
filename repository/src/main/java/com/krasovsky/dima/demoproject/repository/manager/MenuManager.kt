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

    fun getMenuItems(): Flowable<MenuItemsResponse> {
        return apiManager.getMenuItems()
                .map(this::mapMenuItems)
                .map { MenuItemsResponse(successfulLoading, it) }
                .onErrorResumeNext { t: Throwable -> getMenuItemsFromDB() }
    }

    private fun mapMenuItems(it: ArrayList<MenuItemModel>): ArrayList<MenuItemModel> {
        realmManager.saveMenuItems(it)
        return it
    }

    private fun getMenuItemsFromDB(): Flowable<MenuItemsResponse> {
        return realmManager.getMenuItems().map { MenuItemsResponse(errorLoading, it) }
    }

}