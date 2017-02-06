package com.gs.buluo.store.model;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.CartItemUpdateResponse;
import com.gs.buluo.store.bean.RequestBodyBean.LogisticsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.NewOrderResponse;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.ShoppingService;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class ShoppingModel {

    public void getOrder(String uid, String status, String limitSize, String sortSkip, Callback<OrderResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                getOrder(uid, limitSize, sortSkip, status).enqueue(callback);
    }

    public void updateOrder(String uid, LogisticsRequestBody status, String orderId, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                updateOrderToSend(orderId, uid, status).enqueue(callback);
    }

    public void getOrderFirst(String uid, String status, String limitSize, Callback<OrderResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                getOrderFirst(uid, limitSize, status).enqueue(callback);
    }

    public void getShoppingListFirst(String uid, Callback<ShoppingCartResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                getShoppingCarListFirst(uid, 50).enqueue(callback);
    }

    public void deleteShoppingItem(String body, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                deleteCart(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void updateShoppingItem(ShoppingCartGoodsItem body, Callback<CartItemUpdateResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                updateCartItem(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void addShoppingCart(NewOrderBean body, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingService.class).
                addCartItem(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }
}
