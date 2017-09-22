package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.store.bean.RequestBodyBean.ThirdLoginRequest;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.store.bean.StoreAccount;
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
                .flatMap(new Func1<BaseResponse<UserBeanEntity>, Observable<BaseResponse<StoreAccount>>>() {
                    @Override
                    public Observable<BaseResponse<StoreAccount>> call(BaseResponse<UserBeanEntity> response) {
                        String uid = response.data.getAssigned();
                        token = response.data.getToken();
                        StoreAccount entity = new StoreAccount();
                        entity.setId(uid);
                        entity.setToken(token);
                        TribeApplication.getInstance().setUserInfo(entity);
                        return TribeRetrofit.getInstance().createApi(MainApis.class).getStoreInfo(uid, uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<BaseResponse<StoreAccount>>() {
                    @Override
                    public void call(BaseResponse<StoreAccount> response) {
                        setStoreInfo(response.data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreAccount>>() {
                    @Override
                    public void onNext(BaseResponse<StoreAccount> response) {
                        if (isAttach()) {
                            mView.actSuccess();
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
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode());
                    }
                });
    }

    private void setStoreInfo(StoreAccount storeAccount) {
        StoreInfoDao dao = new StoreInfoDao();
        storeAccount.setToken(token);
        dao.saveBindingId(storeAccount);
    }

    public void doThirdLogin(String phone, String verify, final String wxCode) {
        LoginBody bean = new LoginBody();
        bean.phone = phone;
        bean.verificationCode = verify;
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<UserBeanEntity>, Observable<BaseResponse<StoreAccount>>>() {
                    @Override
                    public Observable<BaseResponse<StoreAccount>> call(BaseResponse<UserBeanEntity> response) {
                        UserBeanEntity data = response.data;
                        String uid = data.getAssigned();
                        token = data.getToken();

                        StoreAccount entity = new StoreAccount();
                        entity.setId(uid);
                        entity.setToken(token);
                        TribeApplication.getInstance().setUserInfo(entity);
                        bindThird(uid, wxCode);
                        return TribeRetrofit.getInstance().createApi(MainApis.class).
                                getStoreInfo(uid, uid);
                    }
                })
                .doOnNext(new Action1<BaseResponse<StoreAccount>>() {
                    @Override
                    public void call(BaseResponse<StoreAccount> response) {
                        setStoreInfo(response.data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreAccount>>() {
                    @Override
                    public void onNext(BaseResponse<StoreAccount> userBeanResponse) {
                        mView.actSuccess();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (e.getDisplayMessage()!=null){
                            mView.showError(e.getCode(),e.getDisplayMessage());
                        }else {
                            mView.dealWithIdentify(e.getCode());
                        }
                    }

                });
    }

    private void bindThird(String uid, String wxCode) {
        ThirdLoginRequest request = new ThirdLoginRequest();
        request.memberType = "STORE";
        request.memberId = uid;
        request.code = wxCode;
        TribeRetrofit.getInstance().createApi(MainApis.class).bindThirdLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getCode(), e.getDisplayMessage());
                    }
                });
    }

    public void changePhone(final String phone, String verify) {
        PhoneUpdateBody body = new PhoneUpdateBody();
        body.phone = phone;
        body.verificationCode = verify;
        TribeRetrofit.getInstance().createApi(MainApis.class).updatePhone(TribeApplication.getInstance().getUserInfo().getId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<BaseResponse<CodeResponse>>() {
                    @Override
                    public void call(BaseResponse<CodeResponse> codeResponseBaseResponse) {
                        StoreInfoDao dao = new StoreInfoDao();
                        StoreAccount entity = dao.findFirst();
                        entity.setPhone(phone);
                        dao.update(entity);
                    }
                })
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        mView.actSuccess();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (e.getDisplayMessage()!=null){
                            mView.showError(e.getCode(),e.getDisplayMessage());
                        }else {
                            mView.dealWithIdentify(e.getCode());
                        }
                    }
                });
    }
}
