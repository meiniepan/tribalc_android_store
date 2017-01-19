package com.gs.buluo.store.presenter;

import android.util.Log;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.view.impl.ILoginView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final MainModel mainModel;
    private String token;

    public LoginPresenter() {
        mainModel = new MainModel();
    }

    public void doLogin(Map<String, String> params) {
        mainModel.doLogin(params, new Callback<UserBeanResponse>() {
            @Override
            public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {
                UserBeanResponse user = response.body();
                if (null != user && user.getCode() == 200 || null != user && user.getCode() == 201) {
                    Log.e("Login Result: userId ", "Retrofit Response: " + response.body().getData().getAssigned());
                    String uid = user.getData().getAssigned();
                    token = response.body().getData().getToken();
                    getStoreInfo(uid);
                } else if (user != null && user.getCode() == 401) {
                    if (isAttach()) mView.showError(R.string.wrong_verify);
                }
            }

            @Override
            public void onFailure(Call<UserBeanResponse> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }


    public void doVerify(String phone) {
        mainModel.doVerify(phone, new Callback<BaseResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CodeResponse>> call, Response<BaseResponse<CodeResponse>> response) {
                if (response.body() != null) {
                    BaseResponse res = response.body();
                    if (res.code == 202) {
                        mView.dealWithIdentify(202);
                    } else {
                        mView.dealWithIdentify(400);
                    }
                } else {
                    if (null == mView) return;
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CodeResponse>> call, Throwable t) {
                if (null == mView) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getStoreInfo(String uid) {
        mainModel.getStoreInfo(uid, new TribeCallback<StoreInfo>() {
            @Override
            public void onSuccess(Response<BaseResponse<StoreInfo>> response) {
                StoreInfo entity = response.body().data;
                entity.setToken(token);

                TribeApplication.getInstance().setUserInfo(entity);
                StoreInfoDao dao = new StoreInfoDao();
                dao.saveBindingId(entity);
                EventBus.getDefault().post(new SelfEvent());
                if (isAttach()) {
                    mView.loginSuccess();
                }
            }

            @Override
            public void onFail(int responseCode, BaseResponse<StoreInfo> body) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
