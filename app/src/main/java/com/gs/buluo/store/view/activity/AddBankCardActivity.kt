package com.gs.buluo.store.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.NonNull
import android.text.TextUtils
import android.view.View
import com.gs.buluo.common.network.ApiException
import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.BankCard
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody
import com.gs.buluo.store.bean.ResponseBody.CodeResponse
import com.gs.buluo.store.kotlin.activity.KotBaseActivity
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit
import kotlinx.android.synthetic.main.activity_add_bank_card.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by hjn on 2016/11/23.
 */
class AddBankCardActivity : KotBaseActivity() {
    private var cardId: String? = null

    override fun bindView(savedInstanceState: Bundle?) {
        card_add_finish.setOnClickListener {
            if (bankType == BankCard.BankCardBindTypeEnum.WITHDRAW) {
                sendVerifyCode(card_add_phone!!.text.toString().trim { it <= ' ' })
            } else {
                doAddCard()
            }
        }
        findViewById(R.id.card_add_choose).setOnClickListener { startActivityForResult(Intent(ctx, BankPickActivity::class.java), Constant.ForIntent.REQUEST_CODE) }
        card_send_verify!!.setOnClickListener {
            sendVerifyCode(card_add_phone!!.text.toString().trim { it <= ' ' })
            card_add_verify.requestFocus()
        }
    }

    override val contentLayout: Int
        get() = R.layout.activity_add_bank_card

    private var bankType = BankCard.BankCardBindTypeEnum.NORMAL
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        if (resultCode == RESULT_OK && requestCode == Constant.ForIntent.REQUEST_CODE) {
            val card = data.getParcelableExtra<BankCard>(Constant.ForIntent.FLAG)
            card_add_bank_name!!.text = card.bankName
            bankType = card.bindType
            if (bankType == BankCard.BankCardBindTypeEnum.WITHDRAW) {
                findViewById(R.id.ll_verify_view).visibility = View.GONE
            } else {
                findViewById(R.id.ll_verify_view).visibility = View.VISIBLE
            }
        }
    }

    private fun sendVerifyCode(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(this, R.string.phone_not_empty)
            return
        }
        val card = BankCard()
        card.bankCardNum = card_add_bank_num!!.text.toString().trim { it <= ' ' }
        card.bankName = card_add_bank_name!!.text.toString().trim { it <= ' ' }
        card.userName = card_add_name!!.text.toString().trim { it <= ' ' }
        card.phone = card_add_phone!!.text.toString().trim { it <= ' ' }
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).prepareAddBankCard(TribeApplication.getInstance().userInfo.id, card).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<BankCard>>() {
                    override fun onNext(bankCardBaseResponse: BaseResponse<BankCard>?) {
                        dealWithIdentify(bankCardBaseResponse!!.code)
                        val data = bankCardBaseResponse.data
                        cardId = data.id
                    }

                    override fun onFail(e: ApiException) {
                        dealWithIdentify(e.code)
                    }
                })
    }


    private fun dealWithIdentify(code: Int) {
        when (code) {
            201 -> {
                card_send_verify!!.text = "60s"
                object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        card_send_verify!!.isClickable = false
                        card_send_verify!!.text = (millisUntilFinished / 1000).toString() + "秒"
                    }

                    override fun onFinish() {
                        card_send_verify!!.text = "获取验证码"
                        card_send_verify!!.isClickable = true
                    }
                }.start()
            }
            400 -> ToastUtils.ToastMessage(ctx, R.string.wrong_number)
            403 -> ToastUtils.ToastMessage(ctx, getString(R.string.bank_card_forbidden))
            409 -> ToastUtils.ToastMessage(ctx, getString(R.string.bank_card_binded))
            412 -> ToastUtils.ToastMessage(ctx, getString(R.string.not_auth))
            424 -> {
                ToastUtils.ToastMessage(ctx, getString(R.string.not_support_card))
                ToastUtils.ToastMessage(ctx, getString(R.string.connect_error) + code)
            }
            else -> ToastUtils.ToastMessage(ctx, getString(R.string.connect_error) + code)
        }
    }

    private fun doAddCard() {
        showLoadingDialog()
        val vCode = card_add_verify!!.text.toString().trim { it <= ' ' }
        val verifyBody = ValueRequestBody(vCode)
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).uploadVerify(TribeApplication.getInstance().userInfo.id, cardId, verifyBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>() {
                    override fun onNext(codeResponseBaseResponse: BaseResponse<CodeResponse>?) {
                        startActivity(Intent(ctx, BankCardActivity::class.java))
                    }

                    override fun onFail(e: ApiException) {
                        if (e.code == 401) ToastUtils.ToastMessage(ctx, R.string.wrong_verify)
                        else ToastUtils.ToastMessage(ctx, R.string.wrong_info)
                    }
                })
    }
}
