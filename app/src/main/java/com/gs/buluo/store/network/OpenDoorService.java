package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.RequestBodyBean.OpenDoorRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fs on 2016/12/21.
 */

public interface OpenDoorService {
    @POST("/tribalc/v1.0/persons/{id}/unlock_door")
    Call<BaseResponse> postOpenDoor(@Path("id") String id, @Body OpenDoorRequestBody openDoorRequestBody);
}
