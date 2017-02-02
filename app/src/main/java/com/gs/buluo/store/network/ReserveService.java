package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.DetailReservation;
import com.gs.buluo.store.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ReserveService {
    @GET("reservations?types=store")
    Call<ReserveResponse> getReservationList(
//            @Query("status") String status,
            @Query("limitSize") int limitSize,
            @Query("store") String myId,
            @Query("sortSkip") String sortSkip);

    @GET("reservations?types=store")
    Call<ReserveResponse> getReservationListFirst(
//            @Query("status") String status,
            @Query("store") String myId,
            @Query("limitSize") int limitSize);


    @GET("reservations/{id}?types=store")
    Call<BaseResponse<DetailReservation>> getReserveDetail(@Path("id") String reserveId, @Query("store") String myId);

    @PUT("reservations/{id}/status?types=store")
    Call<BaseResponse> updateReserve(@Path("id") String id, @Query("store") String myId,
                                     @Query("store")String storeId, @Body ValueRequestBody body);
}

