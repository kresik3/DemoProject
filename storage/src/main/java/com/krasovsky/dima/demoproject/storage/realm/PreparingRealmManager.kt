package com.krasovsky.dima.demoproject.storage.realm

import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.dish.StateDish
import com.krasovsky.dima.demoproject.storage.model.dish.StateDishModel
import com.krasovsky.dima.demoproject.storage.model.info.InfoObject
import io.realm.Realm

class PreparingRealmManager {

    fun resetDishesPages() {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    where(StateDishModel::class.java)
                            .findAll().forEach { item -> item.state = StateDish.LAUNCH.name }
                }
            }
        }
    }

    fun isDataChanged(): Boolean {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val data = where(LocalDataChanged::class.java).findFirst()
                return if (data == null) {
                    false
                } else {
                    val result = copyFromRealm(data).isDataChanged
                    executeTransaction {
                        data.isDataChanged = false
                    }
                    result
                }
            }
        }
    }

    fun getMenuImagesPath(): List<String> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(MenuItemModel::class.java).findAll()
                return copyFromRealm(page).map { it.iconPath }
            }
        }
    }

    fun getDishesImagesPath(): List<String> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(DishModel::class.java).findAll()
                return copyFromRealm(page).map { it.imagePath }
            }
        }
    }

    fun getInfoObjectImagesPath(): List<String> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(InfoObject::class.java).findAll()
                return copyFromRealm(page.filter { it.type == "Image" }).map { it.content }
            }
        }
    }
}