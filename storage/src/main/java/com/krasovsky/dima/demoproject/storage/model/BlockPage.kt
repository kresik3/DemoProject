package com.krasovsky.dima.demoproject.storage.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.RealmClass

@RealmClass
open class BlockPage : RealmObject() {

    open var currentPage: Int = 0
    open var pageSize: Int = 0
    open var totalPages: Int = 0
    open var type: String = ""

    open var records: RealmList<BlockInfoObject> = RealmList()
}