package com.krasovsky.dima.demoproject.storage.realm

import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.dish.StateDish
import com.krasovsky.dima.demoproject.storage.model.dish.StateDishModel
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmResults

class RealmManager {

    fun resetDishes() {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    where(StateDishModel::class.java)
                            .findAll().forEach { item -> item.state = StateDish.LAUNCH.name }
                }
            }
        }
    }

    fun isNeedReloadItems(categoryId: String): Boolean {
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

    fun saveDishItems(categoryItemId: String, model: List<DishModel>) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    copyToRealmOrUpdate(StateDishModel()
                            .apply {
                                categoryId = categoryItemId
                                state = StateDish.DOWNLOADED.name
                            })
                    where(StateDishModel::class.java)
                            .equalTo("categoryId", categoryItemId)
                            .findAll().deleteAllFromRealm()
                    copyToRealm(model)
                }
            }
        }
    }

    fun getDishItems(categoryId: String): List<DishModel> {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                copyFromRealm(where(DishModel::class.java)
                        .equalTo("categoryId", categoryId)
                        .findAll())
            }
        }
    }

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
                    setDataChanged()
                }
                result
            }
        }
    }

    fun clearBlockPageByType(type: String) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    where(BlockPage::class.java).equalTo("type", type).findAll().deleteAllFromRealm()
                }
            }
        }
    }

    fun saveBlockPage(model: BlockPage) {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    copyToRealm(model)
                }
            }
        }
    }

    fun isExistBlockPage(model: BlockPageModel): Boolean {
        Realm.getDefaultInstance().use { db ->
            return with(db) {
                val page = where(BlockPage::class.java)
                        .equalTo("type", model.type)
                        .equalTo("currentPage", model.index)
                        .findFirst()
                page != null
            }
        }
    }

    fun getBlockPage(model: BlockPageModel): Flowable<BlockPage> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(BlockPage::class.java)
                        .equalTo("type", model.type)
                        .equalTo("currentPage", model.index)
                        .findFirst()
                if (page != null) {
                    return Flowable.just(copyFromRealm(page))
                } else return Flowable.just(BlockPage())
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

    fun getMenuItems(): Flowable<List<MenuItemModel>> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(MenuItemModel::class.java).findAll()
                return Flowable.just(copyFromRealm(page))
            }
        }
    }

    fun setDataChanged() {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                executeTransaction {
                    copyToRealmOrUpdate(LocalDataChanged())
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

    fun getInfoObjectImagesPath(): List<String> {
        Realm.getDefaultInstance().use { db ->
            with(db) {
                val page = where(InfoObject::class.java).findAll()
                return copyFromRealm(page.filter { it.type == "Image" }).map { it.content }
            }
        }
    }
}