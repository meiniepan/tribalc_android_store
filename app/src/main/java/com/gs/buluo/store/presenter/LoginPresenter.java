package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.ILoginView;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private String token;

    public void doLogin(Map<String, String> params) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);

        TribeRetrofit.getInstance().createApi(MainApis.class).doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<UserBeanEntity>, Observable<BaseResponse<StoreInfo>>>() {
                    @Override
                    public Observable<BaseResponse<StoreInfo>> call(BaseResponse<UserBeanEntity> response) {
                        String uid = response.data.getAssigned();
                        token = response.data.getToken();
                        StoreInfo entity = new StoreInfo();
                        entity.setId(uid);
                        entity.setToken(token);
                        TribeApplication.getInstance().setUserInfo(entity);
                        return TribeRetrofit.getInstance().createApi(MainApis.class).getStoreInfo(uid,uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<BaseResponse<StoreInfo>>() {
                    @Override
                    public void call(BaseResponse<StoreInfo> response) {
                        setStoreInfo(response.data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreInfo>>() {
                    @Override
                    public void onNext(BaseResponse<StoreInfo> response) {
                        if (isAttach()) {
                            mView.loginSuccess();
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode());
                    }
                });
    }

    public void doVerify(String phone) {
        TribeRetrofit.getInstance().createApi(MainApis.class).doVerify(new ValueRequestBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>(false) {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        mView.dealWithIdentify(202);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode());
                    }
                });
    }

    private void setStoreInfo(StoreInfo storeInfo) {
        StoreInfoDao dao = new StoreInfoDao();
        storeInfo.setToken(token);
        dao.saveBindingId(storeInfo);
    }
}
