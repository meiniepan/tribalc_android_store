package com.gs.buluo.store.kotlin.activity

import android.os.Bundle
import android.view.View
import com.gs.buluo.common.utils.AppManager
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.view.impl.ILoginView
import kotlinx.android.synthetic.main.activity_update_phone2.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class UpdatePhoneActivity2 : KotBaseActivity(), ILoginView {
    override fun showError(res: Int, message: String?) {
        ToastUtils.ToastMessage(ctx, message)
    }

    override fun actSuccess() {
        finish()
        AppManager.getAppManager().finishActivity(UpdatePhoneActivity::class.java)
        ToastUtils.ToastMessage(ctx, R.string.update_success)
    }

    override fun dealWithIdentify(res: Int) {
        when (res) {
            401 -> ToastUtils.ToastMessage(ctx, R.string.wrong_verify)
            409 -> ToastUtils.ToastMessage(ctx, R.string.phone_exist)
            else -> ToastUtils.ToastMessage(ctx, R.string.update_fail)
        }
    }

    var mPhone: String = ""
    override fun bindView(savedInstanceState: Bundle?) {
        mPresenter = KotLoginPresenter(this)
        mPhone = intent.getStringExtra(Constant.PHONE)
        verify_phone2.text = mPhone
        second_counts.isClickable= false
        second_counts.setOnClickListener {
            startCounter()
            (mPresenter as KotLoginPresenter).doVerify(mPhone)
        }
        subscriber = object : Subscriber<Long>() {
            override fun onNext(t: Long?) {
                second_counts.text = t.toString() + "秒后可重发"
            }

            override fun onCompleted() {
                second_counts.text = "重新发送"
                second_counts.isClickable= true
            }

            override fun onError(e: Throwable?) {
                second_counts.text = "重新发送"
                second_counts.isClickable= true
            }
        }

        startCounter()
    }

    private var subscriber: Subscriber<Long>? = null
    private fun startCounter() {
        var startTime = 60
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime)
                .map { time -> startTime - time }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    override val contentLayout: Int
        get() = R.layout.activity_update_phone2
}
