package com.gs.buluo.store.kotlin.activity

import com.gs.buluo.store.kotlin.presenter.WalletPresenter
import kotlinx.android.synthetic.main.activity_wallet.*

/**
 * Created by hjn on 2016/11/17.
 */
class WalletActivity: KotBaseActivity(), android.view.View.OnClickListener, com.gs.buluo.store.view.impl.IWalletView, android.content.DialogInterface.OnDismissListener {
    override fun bindView(savedInstanceState: android.os.Bundle?) {
        wallet_bill.setOnClickListener(this)
        wallet_card.setOnClickListener(this)
        wallet_financial.setOnClickListener(this)
        wallet_pwd.setOnClickListener(this)
        wallet_cash.setOnClickListener(this)
        wallet_receive.setOnClickListener(this)
        presenter  = WalletPresenter(this)
    }

    override val contentLayout: Int
        get() = com.gs.buluo.store.R.layout.activity_wallet


    private var pwd: String = ""
    private var balance = -1f
    private var withdrawCharge: Float = 0.toFloat()

    override fun onResume() {
        super.onResume()
        showLoadingDialog()
        getData()
    }

    private fun getData() {
        (presenter as WalletPresenter).getWalletInfo()
    }

    override fun onClick(v: android.view.View) {
        if (balance == -1f) {
            com.gs.buluo.common.utils.ToastUtils.ToastMessage(this, com.gs.buluo.store.R.string.connect_fail)
            return
        }
        val intent = android.content.Intent()
        when (v.id) {
            com.gs.buluo.store.R.id.wallet_bill -> {
                intent.setClass(this, com.gs.buluo.store.view.activity.BillActivity::class.java)
                startActivity(intent)
            }
            com.gs.buluo.store.R.id.wallet_card -> {
                intent.setClass(this, com.gs.buluo.store.view.activity.BankCardActivity::class.java)
                startActivity(intent)
            }
            com.gs.buluo.store.R.id.wallet_receive -> {
                intent.setClass(this, com.gs.buluo.store.view.activity.PayCodeActivity::class.java)
                startActivity(intent)
            }
            com.gs.buluo.store.R.id.wallet_financial -> com.gs.buluo.common.utils.ToastUtils.ToastMessage(this, com.gs.buluo.store.R.string.no_function)
            com.gs.buluo.store.R.id.wallet_pwd -> {
                if (android.text.TextUtils.isEmpty(pwd)) {
                    intent.setClass(this, com.gs.buluo.store.view.activity.UpdateWalletPwdActivity::class.java)
                } else {
                    intent.putExtra(com.gs.buluo.store.Constant.WALLET_PWD, pwd)
                    intent.setClass(this, com.gs.buluo.store.view.activity.ConfirmActivity::class.java)
                }
                startActivity(intent)
            }
            com.gs.buluo.store.R.id.wallet_cash -> {
                if (!com.gs.buluo.store.TribeApplication.getInstance().isBf_withdraw) {
                    com.gs.buluo.common.utils.ToastUtils.ToastMessage(this, com.gs.buluo.store.R.string.no_function)
                    return
                }
                intent.putExtra(com.gs.buluo.store.Constant.WALLET_AMOUNT, balance)
                intent.putExtra(com.gs.buluo.store.Constant.WALLET_PWD, pwd)
                intent.putExtra(com.gs.buluo.store.Constant.POUNDAGE, withdrawCharge)
                intent.setClass(this, com.gs.buluo.store.view.activity.CashActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun getWalletInfoFinished(account: com.gs.buluo.store.bean.WalletAccount) {
        pwd = account.password
        balance = account.balance
        withdrawCharge = account.withdrawCharge
        setData(balance.toString() + "")

//        var (account ,id ) = account
    }

    fun setData(price: String?) {
        if (price == null) return
        val arrs = price.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (arrs.size > 1) {
            wallet_integer.text = arrs[0]
            wallet_float.text = arrs[1]
        } else {
            wallet_integer.text = price
            wallet_float.text = "00"
        }
    }

    override fun showError(res: Int) {
        com.gs.buluo.common.utils.ToastUtils.ToastMessage(this, getString(res))
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        (presenter as WalletPresenter).getWalletInfo()
    }
}
