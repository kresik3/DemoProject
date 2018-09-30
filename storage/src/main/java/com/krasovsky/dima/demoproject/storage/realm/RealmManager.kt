package com.krasovsky.dima.demoproject.storage.realm

import com.krasovsky.dima.demoproject.storage.model.BlockPage
import com.krasovsky.dima.demoproject.storage.model.HistoryModel
import com.krasovsky.dima.demoproject.storage.model.InfoObject
import com.krasovsky.dima.demoproject.storage.retrofit.model.request.BlockPageModel
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
                    copyToRealmOrUpdate(model)
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

}