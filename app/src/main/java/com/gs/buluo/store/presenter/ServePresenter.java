package com.gs.buluo.store.presenter;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.ServeResponse;
import com.gs.buluo.store.model.ServeModel;
import com.gs.buluo.store.view.impl.IServeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServePresenter extends BasePresenter<IServeView> {
    private ServeModel model;
    private String nextSkip;

    public ServePresenter() {
        model = new ServeModel();
    }

    public void getServeListFirst(String category,String sort){
        String coordinate ="";
        if (sort.contains(Constant.SORT_COORDINATE_DESC)){
            coordinate =TribeApplication.getInstance().getPosition().longitude +","+ TribeApplication.getInstance().getPosition().latitude;
        }
        model.getServeListFirst(category, 20, sort,coordinate, new Callback<ServeResponse>() {
            @Override
            public void onResponse(Call<ServeResponse> call, Response<ServeResponse> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    ServeResponse.ServeResponseBody data = response.body().data;
                    nextSkip= data.nextSkip;
                    if (isAttach())mView.getServerSuccess(response.body().data);
                }else {
                    if (isAttach())mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<ServeResponse> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }
    public void getServeMore(String category,String sort){
        String coordinate ="";
        if (sort.contains(Constant.SORT_COORDINATE_DESC)){
            coordinate =TribeApplication.getInstance().getPosition().longitude +","+ TribeApplication.getInstance().getPosition().latitude;
        }
        model.getServeList(category, 20, sort,nextSkip, coordinate, new Callback<ServeResponse>() {
            @Override
            public void onResponse(Call<ServeResponse> call, Response<ServeResponse> response) {
                if (response.body().code==200&&response.body().data!=null){
                    if (isAttach())mView.getServerSuccess(response.body().data);
                }else {
                    if (isAttach())mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<ServeResponse> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }
}
