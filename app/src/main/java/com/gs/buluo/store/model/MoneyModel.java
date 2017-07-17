package com.gs.buluo.store.model;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.bean.OrderPayment;
import com.gs.buluo.store.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.WxPayResponse;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/18.
 */
public class MoneyModel {

    public void addBankCard(String uid, String vCode, BankCard card, Callback<BaseResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                addBankCard(uid, vCode, card).enqueue(callback);
    }

    public void deleteCard(String id, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(), id).enqueue(callback);
    }

    public void createPayment(List<String> ids, String payChannel, String type, Callback<BaseResponse<OrderPayment>> callback) {
        NewPaymentRequest request = new NewPaymentRequest();
        request.orderIds = ids;
        request.payChannel = payChannel;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(), type, request).enqueue(callback);
    }

    public void getPaymentStatus(String payId, Callback<BaseResponse<OrderPayment>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(), payId).enqueue(callback);
    }

    public void topUpInWx(String price, Callback<BaseResponse<WxPayResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                payInWx(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(price)).enqueue(callback);
    }

    public void getWXRechargeResult(String prepayId, Callback<BaseResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getTopUpResult(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(prepayId)).enqueue(callback);
    }
}
