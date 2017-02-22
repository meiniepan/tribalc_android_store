package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.LogisticsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/24.
 */
public interface ShoppingApis {

    @GET("orders?type=store")
    Call<OrderResponse> getOrderFirst(@Query("me") String uid, @Query("limitSize") String limitSize,
                                      @Query("status") String status);

    @GET("orders?type=store")
    Call<OrderResponse> getOrder(@Query("me") String uid, @Query("limitSize") String limitSize
            , @Query("sortSkip") String sortSkip, @Query("status") String status);

    @PUT("orders/{orderId}/status?type=store")
    Call<BaseResponse<OrderBean>> updateOrderToSend(@Path("orderId") String orderId, @Query("me") String uid, @Body LogisticsRequestBody status);


    @POST("persons/{id}/shopping_cart")
    Call<BaseResponse> addCartItem(@Path("id") String uid, @Body NewOrderBean body);

}
