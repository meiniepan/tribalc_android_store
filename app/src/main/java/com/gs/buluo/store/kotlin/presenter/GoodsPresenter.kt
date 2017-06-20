package com.gs.buluo.store.kotlin.presenter

import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.store.bean.GoodList

/**
 * Created by hjn on 2016/11/16.
 */
class GoodsPresenter(var mView: com.gs.buluo.store.view.impl.IGoodsView) : KotBasePresenter() {
    private var nextSkip: String? = null

    fun getGoodsList() {
        com.gs.buluo.store.network.TribeRetrofit.getInstance().createApi(com.gs.buluo.store.network.GoodsApis::class.java).getGoodsListFirst(20)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(object : com.gs.buluo.common.network.BaseSubscriber<BaseResponse<GoodList>>() {
                    override fun onNext(goodListBaseResponse: com.gs.buluo.common.network.BaseResponse<GoodList>?) {
                        super.onNext(goodListBaseResponse)
                        nextSkip = goodListBaseResponse!!.data.nextSkip
                        mView.getGoodsInfo(goodListBaseResponse.data)
                    }
                })
    }

    fun loadMore() {
        com.gs.buluo.store.network.TribeRetrofit.getInstance().createApi(com.gs.buluo.store.network.GoodsApis::class.java).getGoodsList(20, nextSkip)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(object : com.gs.buluo.common.network.BaseSubscriber<BaseResponse<GoodList>>() {
                    override fun onNext(goodListBaseResponse: com.gs.buluo.common.network.BaseResponse<GoodList>?) {
                        super.onNext(goodListBaseResponse)
                        mView.getGoodsInfo(goodListBaseResponse!!.data)
                    }
                })
    }
}
