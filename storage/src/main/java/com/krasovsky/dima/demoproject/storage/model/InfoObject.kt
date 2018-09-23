package com.krasovsky.dima.demoproject.storage.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlin.properties.Delegates

class InfoObject : RealmObject() {

    @PrimaryKey
    var id: Int = 0

    var content: String by Delegates.notNull<String>()
    var type: Int by Delegates.notNull<Int>()

}