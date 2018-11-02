package com.krasovsky.dima.demoproject.storage.model.page

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlin.properties.Delegates

@RealmClass
open class InfoObject : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var content: String = ""
    open var type: String = ""
    open var subOrder: Int = 0
    open var contentGroupId: String = ""
}