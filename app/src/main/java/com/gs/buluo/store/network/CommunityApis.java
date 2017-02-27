package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.StoreMeta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hjn on 2016/11/11.
 */
public interface CommunityApis {
    @GET("store_set_meals/store/{id}")
    Call<BaseResponse<StoreMeta>> getStoreDetail(
            @Path("id") String storeId);
}
