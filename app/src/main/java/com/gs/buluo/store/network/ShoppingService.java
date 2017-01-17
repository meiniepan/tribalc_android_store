package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.CartItemUpdateResponse;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.NewOrderResponse;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/24.
 */
public interface ShoppingService {

    @GET("orders?type=store")
    Call<OrderResponse> getOrderFirst(@Query("store") String uid, @Query("limitSize") String limitSize,
                                      @Query("status") String status);

    @GET("orders?type=store")
    Call<OrderResponse> getOrder(@Query("store") String uid, @Query("limitSize") String limitSize
            , @Query("sortSkip") String sortSkip, @Query("status") String status);

    @PUT("orders/{orderId}/status?type=store")
    Call<BaseResponse> updateOrderToSend(@Path("orderId") String orderId, @Query("storeId") String uid,
                                         @Query("logisticsNum")String logisticsNum,
                                         @Query("logisticsCompany")String logisticsCompany,@Body ValueRequestBody status);
    @GET("persons/{id}/shopping_cart")
    Call<ShoppingCartResponse> getShoppingCarListFirst(@Path("id") String uid, @Query("limitSize") int limitSize);

    @HTTP(method = "DELETE", path = "persons/{id}/shopping_cart/{ids}")
    Call<BaseResponse> deleteCart(@Path("id") String uid, @Path("ids") String ids);

    @PUT("persons/{id}/shopping_cart")
    Call<CartItemUpdateResponse> updateCartItem(@Path("id") String uid, @Body ShoppingCartGoodsItem body);

    @POST("persons/{id}/shopping_cart")
    Call<BaseResponse> addCartItem(@Path("id") String uid, @Body NewOrderBean body);

}
