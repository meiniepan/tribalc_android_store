package com.gs.buluo.store.model;

import com.gs.buluo.store.bean.RequestBodyBean.CommonRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.UserAddressEntity;
import com.gs.buluo.store.network.AddressService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressModel {
    public void getAddress(String uid, String addId, Callback<BaseResponse<UserAddressEntity>> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                getAddress(uid, addId).enqueue(callback);
    }


    public void addAddress(String uid, UserAddressEntity entity, Callback<BaseResponse<UserAddressEntity>> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                addAddress(uid, entity).enqueue(callback);
    }

    public void updateAddress(String uid, String addId, UserAddressEntity entity, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                updateAddress(uid, addId, entity).enqueue(callback);
    }

    public void deleteAddress(String uid, String addrId, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                deleteAddress(uid, addrId).enqueue(callback);
    }


    public void updateDefaultAddress(String uid, String addId, TribeCallback callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                updateDefaultAddress(uid, new CommonRequestBody(addId)).enqueue(callback);
    }

}
