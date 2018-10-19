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
import com.krasovsky.dima.demoproject.repository.model.response.DishItemsResponse
import com.krasovsky.dima.demoproject.repository.model.response.MenuItemsResponse
import com.krasovsky.dima.demoproject.storage.model.dish.DetailModel
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel
import io.reactivex.Flowable
import io.reactivex.observers.DisposableObserver
import kotlin.properties.Delegates


class DishItemViewModel(application: Application) : BaseAndroidViewModel(application) {

    val enableMinusLiveData = MutableLiveData<Boolean>()
    val countLiveData = MutableLiveData<Int>()
    val priceLiveData = MutableLiveData<Float>()
    val totalPriceLiveData = MutableLiveData<Float>()
    val quantityLiveData = MutableLiveData<String>()

    var count = 1
        set(value) {
            if (value == 1) {
                enableMinusLiveData.value = false
            } else if (value > 1) {
                if (enableMinusLiveData.value == false) enableMinusLiveData.value = true
            }
            field = value
            countLiveData.value = value
            notifyCountChanged()
        }

    var dish: DishModel? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    var targetDetail: DetailModel by Delegates.notNull()

    fun updateTargetDetail(detail: DetailModel) {
        targetDetail= detail
        priceLiveData.value = detail.price
        quantityLiveData.value = detail.quantity
        count = 1
    }

    private fun notifyCountChanged() {
        totalPriceLiveData.value = targetDetail.price * count
    }

}