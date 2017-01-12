package com.gs.buluo.store.model;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.bean.OrderPayment;
import com.gs.buluo.store.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BillResponse;
import com.gs.buluo.store.bean.ResponseBody.CardResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.bean.WxPayResponse;
import com.gs.buluo.store.network.MoneyService;
import com.gs.buluo.store.network.TribeRetrofit;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/18.
 */
public class MoneyModel {
    public void getWelletInfo(String uid, Callback<BaseResponse<WalletAccount>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getWallet(uid).enqueue(callback);
    }


    public void getBillList(String uid,String sortSkip, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getBillList(uid,"20",sortSkip).enqueue(callback);
    }

    public void getBillListFirst(String uid, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getBillListFirst(uid,"20").enqueue(callback);
    }

    public void getCardList(String uid, Callback<CardResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getCardList(uid).enqueue(callback);
    }

    public void addBankCard(String uid, String vCode, BankCard card, Callback<BaseResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                addBankCard(uid,vCode,card).enqueue(callback);
    }

    public void deleteCard(String id ,Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(),id).enqueue(callback);
    }

    public void createPayment(List<String> ids, String payChannel,String type, Callback<BaseResponse<OrderPayment>> callback) {
        NewPaymentRequest request=new NewPaymentRequest();
        request.orderIds=ids;
        request.payChannel=payChannel;
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(),type,request).enqueue(callback);
    }

    public void getPaymentStatus(String payId, Callback<BaseResponse<OrderPayment>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(),payId).enqueue(callback);
    }

    public void topUpInWx(String price, Callback<BaseResponse<WxPayResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                payInWx(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(price)).enqueue(callback);
    }

    public void getWXRechargeResult(String prepayId, Callback<BaseResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getTopUpResult(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(prepayId)).enqueue(callback);
    }
}
