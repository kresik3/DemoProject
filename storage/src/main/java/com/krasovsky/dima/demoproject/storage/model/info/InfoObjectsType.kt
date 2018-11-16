package com.krasovsky.dima.demoproject.storage.model.info

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class InfoObjectsType : RealmObject() {

    open var type: String = ""

    open var records: RealmList<BlockInfoObject> = RealmList()
}