package com.androidhuman.example.simplegithub.extensions

import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

operator fun AutoClearedDisposable.plusAssign(disposable: Disposable)
        = this.add(disposable)

fun runOnIoScheduler(func: () -> Unit): Disposable
        = Completable.fromCallable(func).subscribeOn(Schedulers.io()).subscribe()
