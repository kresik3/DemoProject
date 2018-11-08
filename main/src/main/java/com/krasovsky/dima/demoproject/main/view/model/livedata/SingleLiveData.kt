package com.krasovsky.dima.demoproject.main.view.model.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import android.support.annotation.NonNull

import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<T>) {
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun postValue(t: T?) {
        pending.set(true)
        super.postValue(t)
    }

    @MainThread
    fun call() {
        postValue(null)
    }

    @MainThread
    fun call(param: T) {
        postValue(param)
    }

}
