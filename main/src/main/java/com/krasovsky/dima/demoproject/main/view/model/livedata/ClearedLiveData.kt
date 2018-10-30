package com.krasovsky.dima.demoproject.main.view.model.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import java.util.concurrent.atomic.AtomicBoolean


class ClearedLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private var listener: (() -> Unit)? = null


    @MainThread
    override fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<T>) {
        super.observe(owner, Observer { t ->
            if (pending.get()) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<T>, body: (() -> Unit)?) {
        super.observe(owner, Observer { t ->
            if (pending.get()) {
                observer.onChanged(t)
            }
        })
        this.listener = body
    }

    @MainThread
    override fun postValue(@Nullable t: T?) {
        pending.set(true)
        super.postValue(t)
    }

    @MainThread
    fun clear() {
        pending.set(false)
        listener?.invoke()
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