package com.gs.buluo.store.network;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.HomeMessageResponse;
import com.gs.buluo.store.bean.MessageToggleBean;
import com.gs.buluo.store.bean.RequestBodyBean.ReadMsgRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.MessageToggleResponse;
import com.gs.buluo.store.bean.UnReadMessageBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2017/8/24.
 */

public interface MessageApis {
    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessage(@Path("ownerId") String uid, @Query("limit") int limit);

    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessageMore(@Path("ownerId") String uid, @Query("limit") int limit, @Query("sinceTime") long sinceTime, @Query("isNew") boolean isNew);

    @PUT("members/{ownerId}/homeMessages/{messageId}/state")
    Observable<BaseResponse> deleteMessage(@Path("ownerId") String uid, @Path("messageId") String mid);

    @PUT("members/{ownerId}/homeMessageTypeShield/{messageType}")
    Observable<BaseResponse> ignoreMessage(@Path("ownerId") String uid, @Path("messageType") HomeMessageEnum type);

    @GET("members/{ownerId}/homeMessages/types/state?appType=STORE&isAgent=false")
    Observable<BaseResponse<MessageToggleResponse>> getMessageToggleList(@Path("ownerId") String uid);

    @POST("members/{ownerId}/homeMessages/types/{messageType}/state")
    Observable<BaseResponse> toggleMessageStatus(@Path("ownerId") String uid, @Path("messageType") HomeMessageEnum messageType, @Body ValueRequestBody body);

    @GET("members/{ownerId}/xgMessages/count")
    Observable<BaseResponse<UnReadMessageBean>> getUnReadMessage(@Path("ownerId") String uid);

    @POST("members/{ownerId}/xgMessages/read")
    Observable<BaseResponse> readMessage(@Path("ownerId") String uid,@Body ReadMsgRequest body);
}
