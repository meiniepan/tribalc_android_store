package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.StoreGoodsList;
import com.gs.buluo.store.model.GoodsModel;
import com.gs.buluo.store.network.GoodsService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IStoreGoodsView;

import retrofit2.Response;

/**
 * Created by hjn on 2017/1/24.
 */
public class StoreGoodsPresenter extends BasePresenter<IStoreGoodsView> {
    private GoodsModel model;
    private String sortSkip;

    public StoreGoodsPresenter() {
        model = new GoodsModel();
    }

    public void getGoodsListFirst(final boolean published) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).getStoreGoodsListFirst(TribeApplication.getInstance().getUserInfo().getId(), published).
                enqueue(new TribeCallback<StoreGoodsList>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<StoreGoodsList>> response) {
                        StoreGoodsList data = response.body().data;
                        sortSkip = data.nextSkip;
                        mView.getGoodsSuccess(data,published);
                        if (!data.hasMore) {
                            if (isAttach()) mView.showNoMore(published);
                        }
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<StoreGoodsList> body) {
                        if (isAttach())mView.showError(R.string.connect_fail);
                    }
                });
    }

    public void getMore(final boolean published) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).getStoreGoodsListMore(TribeApplication.getInstance().getUserInfo().getId(), published, sortSkip)
                .enqueue(new TribeCallback<StoreGoodsList>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<StoreGoodsList>> response) {
                        StoreGoodsList data = response.body().data;
                        mView.getGoodsSuccess(data,published);
                        sortSkip = data.nextSkip;
                        if (!data.hasMore) {
                            if (isAttach())mView.showNoMore(published);
                        }
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<StoreGoodsList> body) {
                        if (isAttach())mView.showError(R.string.connect_fail);
                    }
                });
    }
}
