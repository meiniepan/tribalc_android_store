package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.OrderApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IGoodDetialView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/22.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodDetialView> {

    public void getGoodsDetail(String id) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getGoodsDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListGoodsDetail>>() {
                    @Override
                    public void onNext(BaseResponse<ListGoodsDetail> listGoodsDetailBaseResponse) {
                        if (isAttach()) mView.getDetailSuccess(listGoodsDetailBaseResponse.data);
                    }
                });
    }

    public void getGoodsStandard(String id) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getGoodsStandard(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodsStandard>>() {
                    @Override
                    public void onNext(BaseResponse<GoodsStandard> listGoodsDetailBaseResponse) {
                        if (isAttach())mView.getStandardSuccess(listGoodsDetailBaseResponse.data);
                    }
                });
    }

    public void addCartItem(String id, int num) {
        NewOrderBean item = new NewOrderBean();
        item.goodsId = id;
        item.amount = num;
        TribeRetrofit.getInstance().createApi(OrderApis.class).addCartItem(id,item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (isAttach())mView.addSuccess();
                    }
                });
    }
}
