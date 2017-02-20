package com.gs.buluo.store.model;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.AuthenticationData;
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
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.network.MainService;
import com.gs.buluo.store.network.TribeRetrofit;

import org.xutils.common.util.MD5;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainModel {             //登录数据同步,上传，验证码
    public void doLogin(Map<String, String> params, Callback<UserBeanResponse> callback) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(MainService.class).
                doLogin(bean).enqueue(callback);
    }

    public void doVerify(String phone, Callback<BaseResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                doVerify(new ValueRequestBody(phone)).enqueue(callback);
    }

    public void updateStore(String id, String key, String value, StoreMeta bean, Callback<BaseResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                updateUser(id, key, bean).enqueue(callback);
    }

    public void getAddressList(String uid, Callback<UserAddressListResponse> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getDetailAddressList(uid).enqueue(callback);
    }

    public void uploadFile(File file, String name, String type, Callback<UploadAccessResponse> callback) {
        UploadAccessBody body = new UploadAccessBody();
        body.key = name;
        body.contentType = type;
        try {
            body.contentMD5 = MD5.md5(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//            body.contentMD5 = "98d8826e6308556a4a0ed87e265e2573";
        TribeRetrofit.getInstance().createApi(MainService.class).
                getUploadUrl(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void doAuthentication(AuthenticationData data, Callback<BaseResponse<AuthenticationData>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), data).enqueue(callback);
    }

    public void updatePhone(String phone, String code, Callback<BaseResponse<CodeResponse>> callback) {
        PhoneUpdateBody body = new PhoneUpdateBody();
        body.phone = phone;
        body.verificationCode = code;
        TribeRetrofit.getInstance().createApi(MainService.class).
                updatePhone(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void getDetailStoreInfo(String uid , Callback<BaseResponse<StoreMeta>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getStoreMeta(uid).enqueue(callback);
    }

    public void getSetMeal(Callback<StoreSetMealResponse> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getCreateSetMeal(TribeApplication.getInstance().getUserInfo().getId()).enqueue(callback);
    }
}
