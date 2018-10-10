package com.krasovsky.dima.demoproject.storage.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class LocalDataChanged : RealmObject() {

    @PrimaryKey
    open var id = "always"

    open var isDataChanged = true
}