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

    @GET("orders?type=owner")
    Call<OrderResponse> getOrderFirst(@Query("me") String uid, @Query("limitSize") String limitSize,
                                      @Query("status") String status);

    @GET("orders?type=owner")
    Call<OrderResponse> getOrder(@Query("me") String uid, @Query("limitSize") String limitSize
            , @Query("sortSkip") String sortSkip, @Query("status") String status);

    @POST("orders?type=owner")
    Call<NewOrderResponse> createNewOrder(@Query("me") String uid, @Body NewOrderRequestBody requestBody);

    @PUT("orders/{orderId}/status?type=owner")
    Call<BaseResponse> updateOrderStatus(@Path("orderId") String orderId, @Query("me") String uid, @Body ValueRequestBody status);

    @GET("shopping_cart")
    Call<ShoppingCartResponse> getShoppingCarList(@Query("me") String uid, @Query("sortSkip") String sortSkip);

    @GET("persons/{id}/shopping_cart")
    Call<ShoppingCartResponse> getShoppingCarListFirst(@Path("id") String uid, @Query("limitSize") int limitSize);

    @HTTP(method = "DELETE", path = "persons/{id}/shopping_cart/{ids}")
    Call<BaseResponse> deleteCart(@Path("id") String uid, @Path("ids") String ids);

    @PUT("persons/{id}/shopping_cart")
    Call<CartItemUpdateResponse> updateCartItem(@Path("id") String uid, @Body ShoppingCartGoodsItem body);

    @POST("persons/{id}/shopping_cart")
    Call<BaseResponse> addCartItem(@Path("id") String uid, @Body NewOrderBean body);

}
