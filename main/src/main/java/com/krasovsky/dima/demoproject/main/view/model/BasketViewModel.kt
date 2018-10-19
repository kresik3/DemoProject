package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.main.list.datasource.model.TypeConnection
import com.krasovsky.dima.demoproject.main.util.ExecutorUtil
import com.krasovsky.dima.demoproject.repository.manager.MenuManager
import com.krasovsky.dima.demoproject.repository.model.TypeMenuItems
import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver


class BasketViewModel(application: Application) : BaseAndroidViewModel(application) {



}