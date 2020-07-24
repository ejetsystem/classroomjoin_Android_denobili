package com.denobili.app.helper_utils

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject


open class RxEventBus {

    private val bus = SerializedSubject(PublishSubject.create<Any>())

    val busObservable: Observable<Any>
        get() = bus

    fun post(o: Any) {
        bus.onNext(o)
    }
}
