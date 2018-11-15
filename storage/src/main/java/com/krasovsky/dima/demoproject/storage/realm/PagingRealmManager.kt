package com.krasovsky.dima.demoproject.storage.realm

import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.dish.StateDish
import com.krasovsky.dima.demoproject.storage.model.dish.StateDishModel
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.info.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.info.InfoObjectsType
import com.krasovsky.dima.demoproject.storage.model.paging.DishesPage
import io.reactivex.Flowable
import io.realm.Realm

class PagingRealmManager {

    fun isNeedReloadDishesPage(categoryId: String): Boolean {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                val dishState = where(StateDishModel::class.java)
                        .equalTo("categoryId", categoryId)
                        .findFirst()
                if (dishState != null) {
                    dishState.state == StateDish.LAUNCH.name
                } else true
            }
        }
    }

    fun clearDishesPages(categoryId: String) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    where(DishesPage::class.java)
                            .equalTo("categoryId", categoryId)
                            .findAll().deleteAllFromRealm()
                }
            }
        }
    }

    fun getDishedPage(categoryId: String, page: Int): DishesPage {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                val model = where(DishesPage::class.java)
                        .equalTo("categoryId", categoryId)
                        .equalTo("currentPage", page)
                        .findFirst()
                if (model != null) {
                    copyFromRealm(model)
                } else DishesPage()
            }
        }
    }

    fun saveDishesPage(model: DishesPage) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    deleteDishesPage(it, model.type, model.currentPage)
                    copyToRealm(model)
                }
            }
        }
    }

    fun switchStateDishesPagesCategory(type: String) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    copyToRealmOrUpdate(StateDishModel().apply {
                        categoryId = type
                        state = StateDish.DOWNLOADED.name
                    })
                }
            }
        }
    }

    private fun deleteDishesPage(db: Realm, categoryId: String, page: Int) {
        with(db) {
            where(DishesPage::class.java)
                    .equalTo("categoryId", categoryId)
                    .equalTo("currentPage", page)
                    .findAll().deleteAllFromRealm()
        }
    }

    fun isExistsDishesPage(categoryId: String, page: Int): Boolean {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                where(DishesPage::class.java)
                        .equalTo("categoryId", categoryId)
                        .equalTo("currentPage", page)
                        .findAll().size != 0
            }
        }
    }

}