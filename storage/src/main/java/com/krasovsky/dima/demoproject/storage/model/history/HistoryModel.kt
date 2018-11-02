package com.krasovsky.dima.demoproject.storage.model.history

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlin.properties.Delegates

@RealmClass
open class HistoryModel : RealmObject() {

    @PrimaryKey
    open var type: String = ""
    open var timeOfChange: String = ""
}