package com.androidhuman.example.simplegithub.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable

class AutoActivatedDisposable(
        private val lifecycleOwner: LifecycleOwner,
        private val func: () -> Disposable)
    : LifecycleObserver {

    private var disposable: Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activate() {
        disposable = func.invoke()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun deactivate() {
        disposable?.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}
