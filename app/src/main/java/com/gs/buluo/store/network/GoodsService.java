package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.RequestBodyBean.CreateGoodsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueBooleanRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.CreateGoodsResponse;
import com.gs.buluo.store.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.store.bean.StoreGoodsList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface GoodsService {
    @GET("goods")
    Call<BaseResponse<GoodList>> getGoodsList(
             @Query("limitSize") int limitSize
            , @Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );

    @GET("goods")
    Call<BaseResponse<GoodList>> getGoodsListFirst(
            @Query("limitSize") String limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Call<BaseResponse<ListGoodsDetail>> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Call<BaseResponse<GoodsStandard>> getGoodsStandard(@Path("id") String id);

    @GET("goods_standards?sort=createTime,desc/createTime,asc")
    Call<BaseResponse<GoodsStandardResponse>> getStandardList(@Query("me")String storeId,@Query("category")String category);

    @POST("goods")
    Call<CreateGoodsResponse> createGoods(@Query("me")String uid , @Body CreateGoodsRequestBody body);

    @PUT("goods/{goodsId}")
    Call<BaseResponse<CodeResponse>> updateGoods(@Path("goodsId") String goodsId,@Query("me")String uid , @Body GoodsMeta goodsMeta);

    @GET("goods?sort=saleQuantity,desc/salePrice,asc")
    Call<BaseResponse<StoreGoodsList>> getStoreGoodsListFirst(@Query("me")String uid,@Query("published")boolean published);

    @GET("goods?sort=saleQuantity,desc/salePrice,asc")
    Call<BaseResponse<StoreGoodsList>> getStoreGoodsListMore(@Query("me")String uid,@Query("published")boolean published,@Query("sortSkip")String sortSkip);

    @PUT("goods/{goodsId}/published")
    Call<BaseResponse<CodeResponse>> pullOffGoods(@Path("goodsId")String goodsId, @Query("me")String uid, @Body ValueBooleanRequest body);

    @DELETE("goods/{goodsId}")
    Call<BaseResponse<CodeResponse>> deleteGoods(@Path("goodsId")String goodsId,@Query("me")String uid);
}
