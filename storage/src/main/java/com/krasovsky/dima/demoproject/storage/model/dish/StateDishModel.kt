package com.krasovsky.dima.demoproject.storage.model.dish

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class StateDishModel : RealmObject() {

    @PrimaryKey
    open var categoryId: String = ""

    open var state: String = ""

}