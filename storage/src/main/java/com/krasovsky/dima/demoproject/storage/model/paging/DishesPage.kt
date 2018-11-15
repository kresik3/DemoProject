package com.krasovsky.dima.demoproject.storage.model.paging

import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class DishesPage : RealmObject() {

    open var currentPage: Int = 0
    open var pageSize: Int = 0
    open var totalPages: Int = 0
    open var type: String = ""

    open var records: RealmList<DishModel> = RealmList()
}