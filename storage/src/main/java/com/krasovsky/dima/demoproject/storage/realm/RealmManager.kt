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

class RealmManager {

    fun checkHistory(model: HistoryModel): Boolean {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                val history = where(HistoryModel::class.java).equalTo("type", model.type).findFirst()
                val result = if (history != null) {
                    history.timeOfChange != model.timeOfChange
                } else {
                    true
                }
                if (result) {
                    executeTransaction {
                        copyToRealmOrUpdate(model)
                    }
                }
                result
            }
        }
    }

    fun saveMenuItems(model: ArrayList<MenuItemModel>) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    delete(MenuItemModel::class.java)
                    copyToRealm(model)
                }
            }
        }
    }

    fun saveInfoObjects(model: InfoObjectsType) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    where(InfoObjectsType::class.java).equalTo("type", model.type)
                            .findFirst()?.let {
                                it.records.deleteAllFromRealm()
                                it.deleteFromRealm()
                            }
                    copyToRealm(model)
                }
            }
        }
    }

    fun getMenuItems(): Flowable<List<MenuItemModel>> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(MenuItemModel::class.java).findAll()
                return Flowable.just(copyFromRealm(page))
            }
        }
    }

    fun getInfoObjects(type: String): Flowable<List<BlockInfoObject>> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val model = where(InfoObjectsType::class.java).equalTo("type", type).findFirst()
                if (model != null) {
                    return Flowable.just(copyFromRealm(model.records))
                } else return Flowable.just(arrayListOf())
            }
        }
    }


}