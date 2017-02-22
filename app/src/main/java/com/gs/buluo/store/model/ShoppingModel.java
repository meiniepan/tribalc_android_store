package com.gs.buluo.store.model;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.LogisticsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.ShoppingApis;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class ShoppingModel {

    public void getOrder(String uid, String status, String limitSize, String sortSkip, Callback<OrderResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrder(uid, limitSize, sortSkip, status).enqueue(callback);
    }

    public void updateOrder(String uid, LogisticsRequestBody status, String orderId, Callback<BaseResponse<OrderBean>> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                updateOrderToSend(orderId, uid, status).enqueue(callback);
    }

    public void getOrderFirst(String uid, String status, String limitSize, Callback<OrderResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrderFirst(uid, limitSize, status).enqueue(callback);
    }


    public void addShoppingCart(NewOrderBean body, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                addCartItem(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }
}
