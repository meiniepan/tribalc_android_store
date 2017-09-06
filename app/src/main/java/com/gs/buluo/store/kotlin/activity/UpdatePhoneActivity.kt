package com.gs.buluo.store.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.utils.CommonUtils
import com.gs.buluo.store.view.impl.ILoginView
import kotlinx.android.synthetic.main.activity_update_phone1.*

class UpdatePhoneActivity : KotBaseActivity(), ILoginView {
    override fun showError(res: Int, message: String?) {
        ToastUtils.ToastMessage(ctx, message)
    }

    override fun loginSuccess() {

    }

    override fun dealWithIdentify(res: Int) {
        when (res) {
            202 -> {
                val intent = Intent(ctx, UpdatePhoneActivity2::class.java)
                intent.putExtra(Constant.PHONE, mPhone)
                startActivity(intent)
            }
            400 -> {
                ToastUtils.ToastMessage(ctx, R.string.wrong_number)
            }
            504 ->{
                ToastUtils.ToastMessage(ctx, R.string.frequency_code)
            }
            else -> ToastUtils.ToastMessage(ctx, R.string.connect_fail)
        }
    }

    var mPhone: String = ""
    override fun bindView(savedInstanceState: Bundle?) {
        mPresenter = KotLoginPresenter(this)
    }

    override val contentLayout: Int
        get() = R.layout.activity_update_phone1


    fun changePhoneNext(v: View) {
        showLoadingDialog()
        mPhone = bind_edit_phone.text.toString()
        if (!CommonUtils.checkPhone("", mPhone, ctx)) return
        (mPresenter as KotLoginPresenter).doVerify(mPhone)
    }
}
