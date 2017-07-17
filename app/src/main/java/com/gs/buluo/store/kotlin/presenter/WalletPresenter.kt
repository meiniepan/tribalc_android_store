package com.gs.buluo.store.kotlin.presenter

import com.gs.buluo.store.bean.WalletAccount
import com.gs.buluo.store.kotlin.activity.WalletActivity

/**
 * Created by hjn on 2016/11/18.
 */
open class WalletPresenter(var view: WalletActivity) : KotBasePresenter() {
    open fun getWalletInfo() {
        com.gs.buluo.store.network.TribeRetrofit.getInstance().createApi(com.gs.buluo.store.network.MoneyApis::class.java).getWallet(com.gs.buluo.store.TribeApplication.getInstance().userInfo.id)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(object : com.gs.buluo.common.network.BaseSubscriber<com.gs.buluo.common.network.BaseResponse<WalletAccount>>() {
                    override fun onNext(response: com.gs.buluo.common.network.BaseResponse<WalletAccount>?) {
                       view.getWalletInfoFinished(response!!.data)
                    }
                })
    }
}
