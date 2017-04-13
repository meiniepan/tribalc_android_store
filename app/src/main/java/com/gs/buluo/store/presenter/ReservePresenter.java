package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.ReserveResponseBody;
import com.gs.buluo.store.network.ReserveApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IReserveView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReservePresenter extends BasePresenter<IReserveView> {
    private String nextSkip;

    public void getReserveListFirst(String category) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).getReservationListFirst(TribeApplication.getInstance().getUserInfo().getId(), 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ReserveResponseBody>>() {
                    @Override
                    public void onNext(BaseResponse<ReserveResponseBody> goodListBaseResponse) {
                        ReserveResponseBody data = goodListBaseResponse.data;
                        nextSkip = data.nextSkip;
                        if (isAttach()) mView.getReserveSuccess(data);
                    }
                });
    }

    public void getReserveMore(String category) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).getReservationList(TribeApplication.getInstance().getUserInfo().getId(), 20, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ReserveResponseBody>>() {
                    @Override
                    public void onNext(BaseResponse<ReserveResponseBody> goodListBaseResponse) {
                        if (isAttach()) mView.getReserveSuccess(goodListBaseResponse.data);
                    }
                });
    }
}
