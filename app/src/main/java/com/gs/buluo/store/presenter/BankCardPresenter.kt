package com.gs.buluo.store.presenter

import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.store.bean.BankCard
import com.gs.buluo.store.kotlin.presenter.KotBasePresenter
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.view.impl.ICardView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by hjn on 2016/11/23.
 */
class BankCardPresenter(var mView: ICardView) : KotBasePresenter() {
    fun getCardList(uid: String) {
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).getCardList(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    override fun onNext(response: BaseResponse<List<BankCard>>) {
                        mView.getCardInfoSuccess(response.data)
                    }
                })
    }
}

