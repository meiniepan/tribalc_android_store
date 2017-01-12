package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.CompanyDetail;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fs on 2016/12/9.
 */
public interface CompanyService {
    @POST("persons/{id}/company_bind_request")
    Call<BaseResponse<CodeResponse>> bindCompany(
            @Path("id") String id, @Body ValueRequestBody requestBody);

    @GET("persons/{id}/company_bind_request")
    Call<BaseResponse<CompanyDetail>> queryCompany(
            @Path("id") String id);
}
