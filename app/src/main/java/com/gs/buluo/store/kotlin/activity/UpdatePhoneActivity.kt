package com.gs.buluo.store.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.gs.buluo.common.network.ApiException
import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.common.utils.DensityUtils
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.R
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.AuthStatus
import com.gs.buluo.store.bean.ResponseBody.CodeResponse
import com.gs.buluo.store.dao.StoreInfoDao
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.utils.CommonUtils
import com.gs.buluo.store.view.activity.IdentifyActivity
import com.gs.buluo.store.view.impl.ILoginView
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
             if (CommonUtils.checkPhone("",mPhone,ctx)){
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
                val dao: StoreInfoDao = StoreInfoDao()
                TribeApplication.getInstance().userInfo.setPhone(mPhone)
                dao.update(TribeApplication.getInstance().userInfo)
                finish()
            }
            202 -> {
                ToastUtils.ToastMessage(ctx, R.string.update_success)
                val dao: StoreInfoDao = StoreInfoDao()
                TribeApplication.getInstance().userInfo.setPhone(mPhone)
                TribeApplication.getInstance().userInfo.authorizedStatus = AuthStatus.NOT_START.name
                TribeApplication.getInstance().userInfo.legalPersonName = ""
                TribeApplication.getInstance().userInfo.legalPersonIdNo = ""
                dao.update(TribeApplication.getInstance().userInfo)

                val intent = Intent(ctx, IdentifyActivity::class.java)
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

    private var checkDialog: AlertDialog? = null
    private fun alertBankCard() {
        val builder = AlertDialog.Builder(this, R.style.myCorDialog)
        val view = View.inflate(this, R.layout.dialog_face_error, null)
        builder.setView(view)
        builder.setCancelable(true)
        val button = view.findViewById(R.id.btn_error_finish) as Button
        button.setOnClickListener {
            checkDialog!!.dismiss()
            cleanBankCard()
        }
        view.findViewById(R.id.ib_dismiss).setOnClickListener { checkDialog!!.dismiss() }
        (view.findViewById(R.id.error_dialog_content) as TextView).text = "提示：当前修改将清除全部已绑定的个人银行卡"
        (view.findViewById(R.id.error_dialog_title) as TextView).text = getString(R.string.update_phone)
        checkDialog = builder.create()
        val params = checkDialog!!.window?.attributes
        params?.width = DensityUtils.dip2px(this, 229f)
        params?.height = DensityUtils.dip2px(this, 223f)
        checkDialog!!.window?.attributes = params
        checkDialog!!.show()
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
