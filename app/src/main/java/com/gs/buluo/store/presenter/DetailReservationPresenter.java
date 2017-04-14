package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.DetailReservation;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.network.ReserveApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IDetailReserveView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/30.
 */
public class DetailReservationPresenter extends BasePresenter<IDetailReserveView> {
    public void getReserveDetail(String id) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).getReserveDetail(id, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<DetailReservation>>() {
                    @Override
                    public void onNext(BaseResponse<DetailReservation> response) {
                        if (isAttach()) mView.getDetailSuccess(response.data);
                    }
                });
    }

    public void updateReserve(String id, String key) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).updateReserve(id, TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (isAttach()) mView.updateSuccess();
                    }
                });
    }
}
