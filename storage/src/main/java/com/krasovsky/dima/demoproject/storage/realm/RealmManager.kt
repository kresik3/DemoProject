package com.krasovsky.dima.demoproject.storage.realm

import com.krasovsky.dima.demoproject.storage.model.*
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import com.krasovsky.dima.demoproject.storage.model.dish.StateDish
import com.krasovsky.dima.demoproject.storage.model.dish.StateDishModel
import com.krasovsky.dima.demoproject.storage.model.history.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject
import com.krasovsky.dima.demoproject.storage.model.page.BlockPage
import com.krasovsky.dima.demoproject.storage.model.page.InfoObject
import com.krasovsky.dima.demoproject.storage.model.page.InfoObjectsType
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
import io.reactivex.Flowable
import io.realm.Realm

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
                    copyToRealmOrUpdate(StateDishModel().apply {
                        categoryId = categoryItemId
                        state = StateDish.DOWNLOADED.name
                    })

                    copyToRealmOrUpdate(model)
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
                return if (page != null) {
                    Flowable.just(copyFromRealm(page))
                } else Flowable.just(BlockPage())
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
                    where(InfoObjectsType::class.java).equalTo("type", model.type).findFirst()?.deleteFromRealm()
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