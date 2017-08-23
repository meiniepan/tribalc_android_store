package com.gs.buluo.store.network;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.AppConfigInfo;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ConfigInfo;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.HomeMessageResponse;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.store.bean.RequestBodyBean.ThirdLoginRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreMeta;
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
    @GET("configs/init?os=android&edition=store")
    Observable<BaseResponse<ConfigInfo>> getConfig(
            @Query("selfId") String uid, @Query("version") String version);

    @GET("configs/version?os=android&edition=store")
    Observable<BaseResponse<AppConfigInfo>> getLastVersion(@Query("version") String version);

    @GET("stores/{storeId}?type=store")
    Observable<BaseResponse<StoreInfo>> getStoreInfo(@Path("storeId") String uid, @Query("me") String id);

    @GET("stores/{storeId}/store_detail?type=store")
    Observable<BaseResponse<StoreMeta>> getStoreDetailInfo(@Path("storeId") String uid, @Query("me") String id);

    @POST("stores/login")
    Observable<BaseResponse<UserBeanEntity>> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Observable<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);

    @POST("wechat/login")
    Observable<BaseResponse<UserBeanEntity>> doThirdLogin(@Body ThirdLoginRequest request);

    @POST("wechat/bind")
    Observable<BaseResponse> bindThirdLogin(@Body ThirdLoginRequest request);


    @POST("oss_authorization/picture")
    Observable<BaseResponse<UploadResponseBody>> getUploadUrl(@Query("me") String id, @Body UploadAccessBody body);


    @PUT("stores/{id}/authentication")
    Observable<BaseResponse<AuthenticationData>> doAuthentication(@Path("id") String id, @Body AuthenticationData request);

    @PUT("stores/{id}/phone")
    Observable<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);


    @PUT("stores/{id}/{propNames}")
    Observable<BaseResponse<CodeResponse>> updateStoreProp(@Path("id") String id, @Path("propNames") String propNames, @Body StoreMeta bean);

    @PUT("stores/{id}")
    Observable<BaseResponse<CodeResponse>> updateStore(@Path("id") String id, @Body StoreMeta bean);

    @GET("store_set_meals")
    Observable<BaseResponse<List<StoreSetMealCreation>>> getCreateSetMeal(@Query("me") String uid);


    @PUT("store_set_meals/{id}")
    Observable<BaseResponse<CodeResponse>> updateMeal(@Path("id") String mealId, @Query("me") String uid, @Body StoreSetMealCreation mealCreation);

    @GET("stores/{id}/authentication")
    Observable<BaseResponse<AuthenticationData>> getAuth(@Path("id") String uid);

    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessage(@Path("ownerId") String uid, @Query("limit") int limit);

    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessageMore(@Path("ownerId") String uid, @Query("limit") int limit, @Query("sinceTime") long sinceTime, @Query("isNew") boolean isNew);

    @PUT("members/{ownerId}/homeMessages/{messageId}/state")
    Observable<BaseResponse> deleteMessage(@Path("ownerId") String uid, @Path("messageId") String mid);

    @PUT("members/{ownerId}/homeMessageTypeShield/{messageType}")
    Observable<BaseResponse> ignoreMessage(@Path("ownerId") String uid, @Path("messageType") HomeMessageEnum type);
}
