package com.gs.buluo.store.kotlin.presenter

import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.store.bean.WalletAccount
import com.gs.buluo.store.kotlin.activity.WalletActivity
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit

/**
 * Created by hjn on 2016/11/18.
 */
open class WalletPresenter(var view: WalletActivity) : KotBasePresenter() {
    open fun getWalletInfo() {
        val id = com.gs.buluo.store.TribeApplication.getInstance().userInfo.id
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).getWallet(id)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<WalletAccount>>() {
                    override fun onNext(response: BaseResponse<WalletAccount>?) {
                        view.getWalletInfoFinished(response!!.data)
                    }
                })
    }
}
