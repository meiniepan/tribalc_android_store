package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.DetailReservation;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.model.ReserveModel;
import com.gs.buluo.store.view.impl.IDetailReserveView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/30.
 */
public class DetailReservationPresenter extends BasePresenter<IDetailReserveView> {
    ReserveModel model;

    public DetailReservationPresenter() {
        model = new ReserveModel();
    }

    public void getReserveDetail(String id) {
        model.getServeDetail(id, TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseResponse<DetailReservation>>() {
            @Override
            public void onResponse(Call<BaseResponse<DetailReservation>> call, Response<BaseResponse<DetailReservation>> response) {
                if (mView == null) return;
                if (response.body() != null && response.body().code == 200) {
                    mView.getDetailSuccess(response.body().data);
                } else {
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<DetailReservation>> call, Throwable t) {
                if (mView == null) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateReserve(String id, String key) {
        model.updateReserve(id, TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(key), new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null && response.body().code == 200) {
                    mView.cancelSuccess();
                } else {
                    mView.cancelFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
