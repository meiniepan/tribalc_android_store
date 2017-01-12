package com.gs.buluo.store.model;

import com.gs.buluo.store.bean.CommunityDetail;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.store.bean.StoreDetail;
import com.gs.buluo.store.network.CommunityService;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityModel {
    public void getCommunitiesList(Callback<CommunityResponse> callback) {
        TribeRetrofit.getInstance().createApi(CommunityService.class).
                getCommunitiesList().enqueue(callback);
    }

    public void getCommunityDetail(String uid, Callback<BaseResponse<CommunityDetail>> callback) {
        TribeRetrofit.getInstance().createApi(CommunityService.class).
                getCommunityDetail(uid).enqueue(callback);
    }

    public void getStoreDetail(String strorId, Callback<BaseResponse<StoreDetail>> callback) {
        TribeRetrofit.getInstance().createApi(CommunityService.class).
                getStoreDetail(strorId).enqueue(callback);
    }
}
