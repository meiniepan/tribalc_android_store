package com.gs.buluo.store.kotlin.presenter

import rx.subscriptions.CompositeSubscription

/**
 * Created by hjn on 2016/11/3.
 */
abstract class KotBasePresenter {
    val mSubscription: CompositeSubscription by lazy {
        CompositeSubscription()
    }

    open fun unSubscriber() {
        if (mSubscription.hasSubscriptions()) {
            mSubscription.unsubscribe()
        }
    }
//    fun attach(mView: T) {
//        this.mView = mView
//    }
//
//    fun detachView() {
//        mView ?: null
//    }
//
//    val isAttach: Boolean
//        get() = mView != null
}