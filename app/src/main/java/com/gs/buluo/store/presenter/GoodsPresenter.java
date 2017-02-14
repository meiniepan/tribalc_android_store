package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.model.GoodsModel;
import com.gs.buluo.store.view.impl.IGoodsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsPresenter extends BasePresenter<IGoodsView> {
    GoodsModel model;
    private String nextSkip;

    public GoodsPresenter() {
        model = new GoodsModel();
    }

    public void getGoodsList() {
        model.getGoodsListFirst("", "", "", "", new Callback<BaseResponse<GoodList>>() {
            @Override
            public void onResponse(Call<BaseResponse<GoodList>> call, Response<BaseResponse<GoodList>> response) {
                if (response.body() != null && response.body().data != null && response.body().code == 200) {
                    nextSkip = response.body().data.nextSkip;
                    if (isAttach()) mView.getGoodsInfo(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GoodList>> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }

    public void loadMore() {
        model.getGoodsList("", "", nextSkip, "saleQuantity,desc", new Callback<BaseResponse<GoodList>>() {
            @Override
            public void onResponse(Call<BaseResponse<GoodList>> call, Response<BaseResponse<GoodList>> response) {
                if (response.code() == 200) {
                    if (isAttach())mView.getGoodsInfo(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GoodList>> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }
}
