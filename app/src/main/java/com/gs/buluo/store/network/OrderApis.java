package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.LogisticsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponseBean;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/24.
 */
public interface OrderApis {

    @GET("orders?type=store")
    Observable<BaseResponse<OrderResponseBean>> getOrderFirst(@Query("me") String uid, @Query("limitSize") int limitSize,
                                                @Query("status") String status);

    @GET("orders?type=store")
    Observable<BaseResponse<OrderResponseBean>> getOrder(@Query("me") String uid, @Query("limitSize") int limitSize
            , @Query("sortSkip") String sortSkip, @Query("status") String status);

    @PUT("orders/{orderId}/status?type=store")
    Observable<BaseResponse<OrderBean>> updateOrderToSend(@Path("orderId") String orderId, @Query("me") String uid, @Body LogisticsRequestBody status);


    @POST("persons/{id}/shopping_cart")
    Observable<BaseResponse> addCartItem(@Path("id") String uid, @Body NewOrderBean body);

}
