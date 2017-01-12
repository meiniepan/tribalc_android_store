package com.gs.buluo.store.model;

import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.GoodsService;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsModel {
    public void getGoodsListFirst(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsListFirst(20 + "").enqueue(callback);
    }

    public void getGoodsList(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsList(category, 20, sortSkip).enqueue(callback);
    }

    public void getGoodsDetail(String id, Callback<BaseResponse<ListGoodsDetail>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsDetail(id).enqueue(callback);
    }

    public void getGoodsStandard(String id, Callback<BaseResponse<GoodsStandard>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsStandard(id).enqueue(callback);
    }
}