package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import com.krasovsky.dima.demoproject.base.dialog.alert.model.DialogData
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
import com.krasovsky.dima.demoproject.main.view.model.livedata.ClearedLiveData
import com.krasovsky.dima.demoproject.main.view.model.livedata.SingleLiveData
import io.reactivex.observers.DisposableObserver


class PaymentViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val manager: BasketManager by lazy { BasketManager(RealmManager(), ApiManager(ApiClient())) }

    var basket: BasketModel by Delegates.notNull()

    val error = ClearedLiveData<DialogData>()
    var success: SingleLiveData<DialogData> = SingleLiveData()

    fun payment(name: String, telephone: String, address: String, comment: String) {
        compositeDisposable.add(manager.makeOrder(name, telephone, address, comment, basketId)
                .wrapBySchedulers()
                .doOnSubscribe {
                    error.clear()
                    loadingLiveData.call()
                }
                .doOnTerminate {
                    loadingLiveData.clear()
                }
                .toObservable()
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        basketId = ""
                        success.call(geSuccessDialogData())
                    }

                    override fun onNext(response: Boolean) {
                    }

                    override fun onError(e: Throwable) {
                        error.call(getErrorDialogData())
                    }

                }))
    }

    private fun getErrorDialogData(): DialogData {
        return with(getApplication<Application>()) {
            DialogData(
                    getString(R.string.title_error),
                    getString(R.string.error_message_try_again),
                    getString(R.string.btn_close),
                    null
            )
        }
    }

    private fun geSuccessDialogData(): DialogData {
        return with(getApplication<Application>()) {
            DialogData(
                    getString(R.string.title_error),
                    getString(R.string.message_success),
                    getString(R.string.btn_ok),
                    null
            )
        }
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