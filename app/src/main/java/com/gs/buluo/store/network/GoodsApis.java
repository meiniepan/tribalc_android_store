package com.gs.buluo.store.network;

import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.RequestBodyBean.CreateGoodsRequestBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueBooleanRequest;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.store.bean.StoreGoodsList;

import java.util.List;

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
public interface GoodsApis {
    @GET("goods")
    Observable<BaseResponse<GoodList>> getGoodsList(
             @Query("limitSize") int limitSize
            , @Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );

    @GET("goods")
    Observable<BaseResponse<GoodList>> getGoodsListFirst(
            @Query("limitSize") int limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Observable<BaseResponse<ListGoodsDetail>> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Observable<BaseResponse<GoodsStandard>> getGoodsStandard(@Path("id") String id);

    @GET("goods_standards?sort=createTime,desc/createTime,asc")
    Observable<BaseResponse<GoodsStandardResponse>> getStandardList(@Query("me")String storeId,@Query("category")String category);

    @POST("goods")
    Observable<BaseResponse<List<GoodsMeta>>> createGoods(@Query("me")String uid , @Body CreateGoodsRequestBody body);

    @PUT("goods/{goodsId}")
    Observable<BaseResponse<CodeResponse>> updateGoods(@Path("goodsId") String goodsId,@Query("me")String uid , @Body GoodsMeta goodsMeta);

    @GET("goods?sort=saleQuantity,desc/salePrice,asc")
    Observable<BaseResponse<StoreGoodsList>> getStoreGoodsListFirst(@Query("me")String uid,@Query("published")boolean published);

    @GET("goods?sort=saleQuantity,desc/salePrice,asc")
    Observable<BaseResponse<StoreGoodsList>> getStoreGoodsListMore(@Query("me")String uid,@Query("published")boolean published,@Query("sortSkip")String sortSkip);

    @PUT("goods/{goodsId}/published")
    Observable<BaseResponse<CodeResponse>> pullOffGoods(@Path("goodsId")String goodsId, @Query("me")String uid, @Body ValueBooleanRequest body);

    @DELETE("goods/{goodsId}")
    Observable<BaseResponse<CodeResponse>> deleteGoods(@Path("goodsId")String goodsId,@Query("me")String uid);
}
