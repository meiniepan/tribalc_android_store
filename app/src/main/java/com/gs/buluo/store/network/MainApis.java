package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.StoreSetMealResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.store.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.store.bean.StoreInfo;
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
public interface MainApis {

    @POST("stores/login")
    Call<UserBeanResponse> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Call<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);

    @GET("stores/{id}/addresses")
    Call<UserAddressListResponse> getDetailAddressList(
            @Path("id") String uid);

    @POST("oss_authorization/picture")
    Call<UploadAccessResponse> getUploadUrl(@Query("me") String id, @Body UploadAccessBody body);

    @PUT("stores/{id}/authentication")
    Call<BaseResponse<AuthenticationData>> doAuthentication(@Path("id") String id, @Body AuthenticationData request);

    @PUT("stores/{id}/phone")
    Call<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);

    @GET("stores/{id}")
    Call<BaseResponse<StoreMeta>> getStoreMeta(@Path("id")String uid);

    @PUT("stores/{id}/{propNames}")
    Call<BaseResponse<CodeResponse>> updateUser(@Path("id") String id,@Path("propNames") String propNames,@Body StoreMeta bean);

    @PUT("stores/{id}")
    Call<BaseResponse<CodeResponse>> updateStore(@Path("id") String id,@Body StoreMeta bean);

    @POST("stores/{id}")
    Call<BaseResponse<StoreMeta>> createStore(@Path("id") String id,@Body StoreMeta bean);

    @GET("store_set_meals")
    Call<StoreSetMealResponse> getCreateSetMeal(@Query("me")String uid);

    @POST("store_set_meals")
    Call<BaseResponse<CodeResponse>> createServe(@Query("me") String uid, @Body StoreSetMealCreation body);

    @PUT("store_set_meals/{id}")
    Call<BaseResponse<CodeResponse>> updateMeal(@Path("id")String mealId,@Query("me")String uid,@Body StoreSetMealCreation mealCreation);

    @GET("stores/{id}/authentication")
    Call<BaseResponse<AuthenticationData>> getAuth(@Path("id")String uid);
}
