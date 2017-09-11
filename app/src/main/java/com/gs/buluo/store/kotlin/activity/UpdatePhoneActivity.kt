package com.gs.buluo.store.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gs.buluo.common.network.ApiException
import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.AuthStatus
import com.gs.buluo.store.bean.OnActionListener
import com.gs.buluo.store.bean.ResponseBody.CodeResponse
import com.gs.buluo.store.dao.StoreInfoDao
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.utils.CommonUtils
import com.gs.buluo.store.view.activity.IdentifyActivity
import com.gs.buluo.store.view.impl.ILoginView
import com.gs.buluo.store.view.widget.panel.ErrorFaceDialog
import kotlinx.android.synthetic.main.activity_update_phone1.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UpdatePhoneActivity : KotBaseActivity(), ILoginView {
    var mPhone: String = ""
    override fun bindView(savedInstanceState: Bundle?) {
        tv_old_phone.text = TribeApplication.getInstance().userInfo.phone
        mPresenter = KotLoginPresenter(this)
        tv_send_verify.setOnClickListener {
            mPhone = et_new_phone.text.toString()
            if (CommonUtils.checkPhone("", mPhone, ctx)) {
                tv_send_verify.isClickable = false
                (mPresenter as KotLoginPresenter).doVerify(mPhone)
                startCounter()
            }
        }
    }

    override fun showError(res: Int, message: String?) {
        ToastUtils.ToastMessage(ctx, message)
    }

    override fun actSuccess() {

    }

    override fun dealWithIdentify(res: Int) {
        when (res) {
            200 -> {
                ToastUtils.ToastMessage(ctx, R.string.update_success)
                val dao = StoreInfoDao()
                TribeApplication.getInstance().userInfo.setPhone(mPhone)
                dao.update(TribeApplication.getInstance().userInfo)
                finish()
            }
            202 -> {
                ToastUtils.ToastMessage(ctx, R.string.update_success)
                val dao = StoreInfoDao()
                TribeApplication.getInstance().userInfo.setPhone(mPhone)
                TribeApplication.getInstance().userInfo.authorizedStatus = AuthStatus.NOT_START.name
                TribeApplication.getInstance().userInfo.legalPersonName = ""
                TribeApplication.getInstance().userInfo.legalPersonIdNo = ""
                dao.update(TribeApplication.getInstance().userInfo)

                val intent = Intent(ctx, IdentifyActivity::class.java)
                intent.putExtra(Constant.UPDATE_PHONE_SIGN, true)
                startActivity(intent)
                finish()
            }
            400 -> {
                subscriber?.unsubscribe()
                ToastUtils.ToastMessage(ctx, R.string.wrong_number)
            }
            403 -> {
                ToastUtils.ToastMessage(ctx, R.string.phone_exist)
            }
            409 -> {
                alertBankCard()
            }
            504 -> {
                ToastUtils.ToastMessage(ctx, R.string.frequency_code)
            }
            else -> ToastUtils.ToastMessage(ctx, R.string.connect_fail)
        }
    }

    private var checkDialog: ErrorFaceDialog? = null
    private fun alertBankCard() {
        checkDialog = ErrorFaceDialog.Builder(this)
                .setTitle(getString(R.string.update_phone))
                .setPositiveButton(getString(R.string.move_on), OnActionListener {
                    checkDialog?.dismiss()
                    cleanBankCard()
                })
                .setContent("当前修改将清除全部个人银行卡绑定,是否继续?")
                .create()

    }

    private fun cleanBankCard() {
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).cleanCard(TribeApplication.getInstance().userInfo.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>() {
                    override fun onNext(response: BaseResponse<CodeResponse>?) {
                        (mPresenter as KotLoginPresenter).changePhone(mPhone, et_verify.text.toString())
                    }

                    override fun onFail(e: ApiException?) {
                        ToastUtils.ToastMessage(ctx, R.string.connect_fail)
                    }
                })
    }

    private var subscriber: Subscriber<Long>? = null
    private fun startCounter() {
        var startTime = 60
        subscriber = object : Subscriber<Long>() {
            override fun onNext(t: Long?) {
                tv_send_verify.text = t.toString() + "秒后可重发"
            }

            override fun onCompleted() {
                tv_send_verify.text = "重新发送"
                tv_send_verify.isClickable = true
                subscriber?.unsubscribe()
            }

            override fun onError(e: Throwable?) {
                tv_send_verify.text = "重新发送"
                tv_send_verify.isClickable = true
                subscriber?.unsubscribe()
            }
        }
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime)
                .map { time -> startTime - time }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    override val contentLayout: Int
        get() = R.layout.activity_update_phone1


    fun changePhone(v: View) {
        showLoadingDialog()
        mPhone = et_new_phone.text.toString()
        if (!CommonUtils.checkPhone("", mPhone, ctx)) return
        (mPresenter as KotLoginPresenter).changePhone(mPhone, et_verify.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriber?.unsubscribe()
    }
}