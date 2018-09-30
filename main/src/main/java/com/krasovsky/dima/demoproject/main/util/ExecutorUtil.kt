package com.krasovsky.dima.demoproject.main.util

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExecutorUtil {

    companion object {
        fun <T> wrapBySchedulers(request: Flowable<T>): Flowable<T> {
            return request
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}