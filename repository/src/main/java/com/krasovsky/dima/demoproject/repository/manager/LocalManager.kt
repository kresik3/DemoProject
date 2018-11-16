package com.krasovsky.dima.demoproject.repository.manager

import com.krasovsky.dima.demoproject.storage.realm.PreparingRealmManager

class LocalManager(private val source: PreparingRealmManager) {

    fun isDataChanged(): Boolean {
        return source.isDataChanged()
    }

    fun getAllImagesString(): List<String> {
        return (source.getMenuImagesPath() + source.getDishesImagesPath()
                + source.getInfoObjectImagesPath()).map { it.substringAfterLast("/") }
    }

    fun resetDishesState() {
        source.resetDishesPages()
    }
}