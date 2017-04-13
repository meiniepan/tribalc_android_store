package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.DetailReservation;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.ReserveResponseBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ReserveApis {
    @GET("reservations?type=store")
    Observable<BaseResponse<ReserveResponseBody>> getReservationList(
//            @Query("status") String status,
            @Query("me") String myId,
            @Query("limitSize") int limitSize,
            @Query("sortSkip") String sortSkip);

    @GET("reservations?type=store")
    Observable<BaseResponse<ReserveResponseBody>> getReservationListFirst(
//            @Query("status") String status,
            @Query("me") String myId,
            @Query("limitSize") int limitSize);


    @GET("reservations/{id}?type=store")
    Observable<BaseResponse<DetailReservation>> getReserveDetail(@Path("id") String reserveId, @Query("me") String myId);

    @PUT("reservations/{id}/status?type=store")
    Observable<BaseResponse> updateReserve(@Path("id") String id, @Query("me") String myId, @Body ValueRequestBody body);
}

