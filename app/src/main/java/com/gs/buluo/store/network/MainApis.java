package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.store.bean.StoreSetMealCreation;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainApis {

    @POST("stores/login")
    Observable<BaseResponse<UserBeanEntity>> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Observable<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);

    @POST("oss_authorization/picture")
    Observable<BaseResponse<UploadResponseBody>> getUploadUrl(@Query("me") String id, @Body UploadAccessBody body);

    @PUT("stores/{id}/authentication")
    Observable<BaseResponse<AuthenticationData>> doAuthentication(@Path("id") String id, @Body AuthenticationData request);

    @PUT("stores/{id}/phone")
    Observable<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);

    @GET("stores/{id}")
    Observable<BaseResponse<StoreMeta>> getStoreMeta(@Path("id")String uid,@Query("me")String id);

    @PUT("stores/{id}/{propNames}")
    Observable<BaseResponse<CodeResponse>> updateStoreProp(@Path("id") String id, @Path("propNames") String propNames, @Body StoreMeta bean);

    @PUT("stores/{id}")
    Observable<BaseResponse<CodeResponse>> updateStore(@Path("id") String id,@Body StoreMeta bean);

    @POST("stores/{id}")
    Observable<BaseResponse<StoreMeta>> createStore(@Path("id") String id,@Body StoreMeta bean);

    @GET("store_set_meals")
    Observable<BaseResponse<List<StoreSetMealCreation>>> getCreateSetMeal(@Query("me")String uid);

    @POST("store_set_meals")
    Observable<BaseResponse<CodeResponse>> createServe(@Query("me") String uid, @Body StoreSetMealCreation body);

    @PUT("store_set_meals/{id}")
    Observable<BaseResponse<CodeResponse>> updateMeal(@Path("id")String mealId,@Query("me")String uid,@Body StoreSetMealCreation mealCreation);

    @GET("stores/{id}/authentication")
    Observable<BaseResponse<AuthenticationData>> getAuth(@Path("id")String uid);
}
