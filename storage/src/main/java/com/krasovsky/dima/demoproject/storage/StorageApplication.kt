package com.krasovsky.dima.demoproject.storage

import android.app.Application
import io.realm.Realm

class StorageApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}