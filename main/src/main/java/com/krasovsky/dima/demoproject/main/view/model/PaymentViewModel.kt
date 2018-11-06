package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.constant.basketId
import com.krasovsky.dima.demoproject.main.util.validate.model.param.interfaces.IValidateParam
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.storage.realm.RealmManager
import com.krasovsky.dima.demoproject.storage.retrofit.ApiClient
import com.krasovsky.dima.demoproject.storage.retrofit.ApiManager
import com.krasovsky.dima.demoproject.repository.manager.BasketManager
import com.krasovsky.dima.demoproject.storage.model.basket.BasketModel
import kotlin.properties.Delegates
import com.krasovsky.dima.demoproject.main.util.validate.model.param.RequiredParam
import com.krasovsky.dima.demoproject.main.util.validate.model.param.TelephoneCodeParam
import com.krasovsky.dima.demoproject.main.util.validate.model.param.TelephoneParam
import com.krasovsky.dima.demoproject.main.util.wrapBySchedulers
import io.reactivex.observers.DisposableObserver


class PaymentViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: BasketManager by lazy { BasketManager(RealmManager(), ApiManager(ApiClient())) }

    var basket: BasketModel by Delegates.notNull()

    fun payment(name: String, telephone: String, address: String, comment: String) {
        compositeDisposable.add(manager.makeOrder(name, telephone, address, comment, basketId)
                .wrapBySchedulers()
                .doOnSubscribe { loadingLiveData.call() }
                .doOnTerminate {
                    loadingLiveData.clear()
                }
                .toObservable()
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: Boolean) {
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                }))
    }

    fun getNameValidateParams(): List<IValidateParam> {
        return listOf(RequiredParam(getApplication<Application>().getString(R.string.required_field)))
    }

    fun getTelephoneValidateParams(): List<IValidateParam> {
        return listOf(
                RequiredParam(getApplication<Application>().getString(R.string.required_field)),
                TelephoneParam(getApplication<Application>().getString(R.string.error_phone_field)))
    }


    fun getTelephoneCodeValidateParams(): List<IValidateParam> {
        return listOf(
                RequiredParam(getApplication<Application>().getString(R.string.required_code_field)),
                TelephoneCodeParam(getApplication<Application>().getString(R.string.error_phone_code_field)))
    }

    fun getAddressValidateParams(): List<IValidateParam> {
        return listOf(RequiredParam(getApplication<Application>().getString(R.string.required_field)))
    }
}