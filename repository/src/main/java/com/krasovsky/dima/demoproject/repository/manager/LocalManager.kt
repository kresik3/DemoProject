package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.baseUrl
import java.io.File

class LocalManager(private val source: RealmManager) {

    fun isDataChanged(): Boolean {
        return source.isDataChanged()
    }

    fun getAllImagesString(): List<String> {
        return (source.getMenuImagesPath() + source.getInfoObjectImagesPath()).map { it.substringAfterLast("/") }
    }

    fun resetDishesState() {
        source.resetDishes()
    }
}