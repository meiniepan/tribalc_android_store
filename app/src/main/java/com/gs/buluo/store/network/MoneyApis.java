package com.gs.buluo.store.network;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.bean.OrderPayment;
import com.gs.buluo.store.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.WithdrawRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BillResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.PrivilegeResponse;
import com.gs.buluo.store.bean.ResponseBody.WithdrawBillResponse;
import com.gs.buluo.store.bean.UpdatePwdBody;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.bean.WxPayResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface MoneyApis {
    @GET("wallets/{id}")
    Observable<BaseResponse<WalletAccount>> getWallet(
            @Path("id") String uid);

    @GET("wallets/{id}/bills")
    Observable<BaseResponse<BillResponse>> getBillList(
            @Path("id") String uid, @Query("limitSize") int limitSize, @Query("sortSkip") String sortSkip, @Query("face2face") boolean isFace);

    @GET("wallets/{id}/bills")
    Observable<BaseResponse<BillResponse>> getBillListFirst(
            @Path("id") String uid, @Query("limitSize") int limitSize, @Query("face2face") boolean isFace);


    @PUT("wallets/{id}/password")
    Call<BaseResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd);

    @PUT("wallets/{id}/password")
    Call<BaseResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd, @Query("vcode") String vCode);

    @GET("wallets/{id}/bank_cards")
    Observable<BaseResponse<List<BankCard>>> getCardList(
            @Path("id") String uid);

    @POST("wallets/{id}/bank_cards")
    Call<BaseResponse<CodeResponse>> addBankCard(
            @Path("id") String uid, @Query("vcode") String vCode, @Body BankCard card);

    @DELETE("wallets/{id}/bank_cards/{bankCardID}")
    Call<BaseResponse> deleteCard(@Path("id") String uid, @Path("bankCardID") String id);

    @GET("wallets/{id}/payments/{paymentId}")
    Call<BaseResponse<OrderPayment>> getPaymentStatus(@Path("id") String uid, @Path("paymentId") String paymentId);

    @POST("wallets/{id}/payments")
    Call<BaseResponse<OrderPayment>> createPayment(@Path("id") String uid, @Query("types") String type, @Body NewPaymentRequest request);

    @POST("recharge/wechat/unifiedorder")
    Call<BaseResponse<WxPayResponse>> payInWx(@Query("me") String uid, @Body ValueRequestBody body);

    @POST("recharge/wechat/orderquery")
    Call<BaseResponse<CodeResponse>> getTopUpResult(@Query("me") String uid, @Body ValueRequestBody body);

    /**
     * 准备添加银行卡信息
     *
     * @param uid
     * @param card
     */
    @POST("wallets/{id}/bank_cards")
    Observable<BaseResponse<BankCard>> prepareAddBankCard(
            @Path("id") String uid, @Body BankCard card);

    /**
     * 上传验证码，确认添加银行卡信息
     *
     * @param uid
     * @param cardId
     * @param verify
     */
    @PUT("wallets/{id}/bank_cards/{bankCardID}")
    Observable<BaseResponse<CodeResponse>> uploadVerify(
            @Path("id") String uid, @Path("bankCardID") String cardId, @Body ValueRequestBody verify);

    @POST("wallets/{id}/withdraws")
    Observable<BaseResponse<CodeResponse>> withdrawCash(@Path("id") String uid, @Body WithdrawRequestBody body);

    @GET("wallets/{id}/withdraws?sort=time,desc")
    Observable<BaseResponse<WithdrawBillResponse>> getWithdrawBill(@Path("id") String uid, @Query("accountType") String type);

    @GET("wallets/{id}/withdraws?sort=time,desc")
    Observable<BaseResponse<WithdrawBillResponse>> getWithdrawMoreBill(@Path("id") String uid,
                                                                       @Query("accountType") String type, @Query("sortSkip") String nextSkip);

    @GET("wallets/{id}/withdraws/{billId}")
    Observable<BaseResponse<WithdrawBill>> getWithdrawDetail(@Path("id") String uid, @Path("billId") String billId);

    @GET("stores/{storeId}/privilege")
    Observable<BaseResponse<PrivilegeResponse>> getAllPrivilege(@Path("storeId") String sid);


}
