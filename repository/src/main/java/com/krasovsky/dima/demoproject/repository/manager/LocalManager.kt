package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.storage.realm.PreparingRealmManager
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.baseUrl
import java.io.File

class LocalManager(private val source: PreparingRealmManager) {

    fun isDataChanged(): Boolean {
        return source.isDataChanged()
    }

    fun getAllImagesString(): List<String> {
        return (source.getMenuImagesPath() + source.getDishesImagesPath()
                + source.getInfoObjectImagesPath()).map { it.substringAfterLast("/") }
    }

    fun resetDishesState() {
        source.resetDishes()
    }
}