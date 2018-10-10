package com.krasovsky.dima.demoproject.main.util

import android.os.Handler
import android.os.Looper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class ExecutorUtil {

    internal class MainThreadExecutor : Executor {
        private val mHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    companion object {
        fun <T> wrapBySchedulers(request: Flowable<T>): Flowable<T> {
            return request
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}