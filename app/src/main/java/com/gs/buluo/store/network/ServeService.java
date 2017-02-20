package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.DetailStoreSetMeal;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.ServeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ServeService {
    @GET("store_set_meals")
    Call<ServeResponse> getServiceList(
            @Query("category") String category,
            @Query("limitSize") int limitSize,
            @Query("sortSkip") String sortSkip,@Query("sort") String sort,
            @Query("coordinate") String coordinate);

    @GET("store_set_meals")
    Call<ServeResponse> getServiceListFirst(
            @Query("category") String category,
            @Query("limitSize") int limitSize, @Query("sort") String sort,
            @Query("coordinate") String coordinate);


    @GET("store_set_meals/{id}")
    Call<BaseResponse<DetailStoreSetMeal>> getServeDetail(@Path("id")String serveId);

}

