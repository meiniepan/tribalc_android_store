package com.gs.buluo.store.model;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.RequestBodyBean.CreateGoodsRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.CreateGoodsResponse;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsModel {
    public void getGoodsListFirst(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsListFirst(20 + "").enqueue(callback);
    }

    public void getGoodsList(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsList(20, sortSkip).enqueue(callback);
    }

    public void getGoodsDetail(String id, Callback<BaseResponse<ListGoodsDetail>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsDetail(id).enqueue(callback);
    }

    public void getGoodsStandard(String id, Callback<BaseResponse<GoodsStandard>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsStandard(id).enqueue(callback);
    }

    public void createGoods(CreateGoodsRequestBody body, Callback<CreateGoodsResponse> callback){
        TribeRetrofit.getInstance().createApi(GoodsApis.class).createGoods(TribeApplication.getInstance().getUserInfo().getId(),body)
        .enqueue(callback);
    }

    public void updateGoods(String goodsId, GoodsMeta meta, Callback<BaseResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(GoodsApis.class).updateGoods(goodsId,TribeApplication.getInstance().getUserInfo().getId(),meta)
                .enqueue(callback);
    }

}
