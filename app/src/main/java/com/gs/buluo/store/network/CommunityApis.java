package com.gs.buluo.store.network;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.StoreMeta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface CommunityApis {
    @GET("store_set_meals/store/{id}")
    Observable<BaseResponse<StoreMeta>> getStoreDetail(
            @Path("id") String storeId);
}
