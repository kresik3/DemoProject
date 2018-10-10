package com.krasovsky.dima.demoproject.storage.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlin.properties.Delegates

@RealmClass
open class BlockInfoObject : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var title: String? = null
    open var order: Int = 0
    open var items: RealmList<InfoObject> = RealmList()

}