package com.gs.buluo.store.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.RequestBodyBean.ThirdLoginRequest;
import com.gs.buluo.store.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.activity.MainActivity;
import com.gs.buluo.store.view.activity.ThirdLoginActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/29.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.Base.WX_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e(TAG, "onReq: " + baseReq.toString());
        if (baseReq.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            doThirdLogin(baseResp);
        }
    }

    private void doThirdLogin(final BaseResp baseResp) {
        LoadingDialog.getInstance().show(this, "登录中", false);
        ThirdLoginRequest request = new ThirdLoginRequest();
        request.code = ((SendAuth.Resp) baseResp).code;
        request.memberType = "STORE";
        TribeRetrofit.getInstance().createApi(MainApis.class).doThirdLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserBeanEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserBeanEntity> response) {
                        if (response.code == 204) {
                            bindPhone(((SendAuth.Resp) baseResp).code);
                        } else {
                            StoreInfo entity = new StoreInfo();
                            String uid = response.data.getAssigned();
                            token = response.data.getToken();
                            entity.setId(uid);
                            entity.setToken(token);
                            TribeApplication.getInstance().setUserInfo(entity);
                            getUserInfo(uid);
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(WXEntryActivity.this, R.string.connect_fail);
                    }
                });
    }

    private void getUserInfo(String assigned) {
        TribeRetrofit.getInstance().createApi(MainApis.class).getStoreInfo(assigned, assigned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<BaseResponse<StoreInfo>>() {
                    @Override
                    public void call(BaseResponse<StoreInfo> response) {
                        saveUserInfo(response);
                    }
                })
                .subscribe(new BaseSubscriber<BaseResponse<StoreInfo>>() {
                    @Override
                    public void onNext(BaseResponse<StoreInfo> userInfoEntityBaseResponse) {
                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void bindPhone(String code) {
        Intent intent = new Intent(this, ThirdLoginActivity.class);
        intent.putExtra(Constant.WX_CODE, code);
        startActivity(intent);
        finish();
    }

    private void saveUserInfo(BaseResponse<StoreInfo> response) {
        StoreInfo entity = response.data;
        entity.setToken(token);
        TribeApplication.getInstance().setUserInfo(entity);
        StoreInfoDao dao = new StoreInfoDao();
        dao.saveBindingId(entity);
    }
}
