package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IGoodsView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsPresenter extends BasePresenter<IGoodsView> {
    private String nextSkip;

    public void getGoodsList() {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getGoodsListFirst(20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                    @Override
                    public void onNext(BaseResponse<GoodList> goodListBaseResponse) {
                        super.onNext(goodListBaseResponse);
                        nextSkip = goodListBaseResponse.data.nextSkip;
                        if (isAttach()) mView.getGoodsInfo(goodListBaseResponse.data);
                    }
                });
    }

    public void loadMore() {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getGoodsList(20,nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                    @Override
                    public void onNext(BaseResponse<GoodList> goodListBaseResponse) {
                        super.onNext(goodListBaseResponse);
                        if (isAttach()) mView.getGoodsInfo(goodListBaseResponse.data);
                    }
                });
    }
}
