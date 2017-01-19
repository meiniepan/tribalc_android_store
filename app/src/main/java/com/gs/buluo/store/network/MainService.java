package com.gs.buluo.store.network;

import android.provider.CallLog;

import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.store.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreSetMeal;
import com.gs.buluo.store.bean.StoreSetMealCreation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainService {

    @GET("stores/{id}")
    Call<BaseResponse<StoreInfo>> getStoreInfo(
            @Path("id") String uid);

    @POST("stores/login")
    Call<UserBeanResponse> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Call<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);


    @GET("stores/{id}/addresses")
    Call<UserAddressListResponse> getDetailAddressList(
            @Path("id") String uid);


    @POST("oss_authorization/picture")
    Call<UploadAccessResponse> getUploadUrl(@Query("me") String id, @Body UploadAccessBody body);

    @POST("stores/{id}/authentication")
    Call<BaseResponse<StoreInfo>> doAuthentication(@Path("id") String id, @Body AuthorityRequest request);

    @PUT("stores/{id}/sensitive_info")
    Call<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);

    @GET("stores/{id}/store_detail")
    Call<BaseResponse<CreateStoreBean>> getCreateStore(@Path("id")String uid);

    @GET("store_set_meals/{id}/get_set_meal")
    Call<BaseResponse<StoreSetMeal>> getCreateSetMeal(@Path("id")String uid);

    @PUT("stores/{id}/{propNames}")
    Call<BaseResponse<CodeResponse>> updateUser(@Path("id") String id,@Path("propNames") String propNames,@Body CreateStoreBean bean);

    @POST("stores/{id}")
    Call<BaseResponse<StoreInfo>> createStore(@Path("id") String id,@Body CreateStoreBean bean);

    @POST("store_set_meals/store/{storeId}/creation")
    Call<BaseResponse<CodeResponse>> createServe(@Path("storeId")String uid, @Body StoreSetMealCreation body);
}
