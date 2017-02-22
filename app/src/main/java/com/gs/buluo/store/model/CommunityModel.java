package com.gs.buluo.store.model;

import com.gs.buluo.store.bean.CommunityDetail;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.network.CommunityApis;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityModel {

    public void getCommunityDetail(String uid, Callback<BaseResponse<CommunityDetail>> callback) {
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getCommunityDetail(uid).enqueue(callback);
    }

    public void getStoreDetail(String strorId, Callback<BaseResponse<StoreMeta>> callback) {
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getStoreDetail(strorId).enqueue(callback);
    }
}
