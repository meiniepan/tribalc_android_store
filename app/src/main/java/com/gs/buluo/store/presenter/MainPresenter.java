package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.HomeMessage;
import com.gs.buluo.store.bean.HomeMessageResponse;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.MessageApis;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IMainView;

import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/19.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    private long lastTime = System.currentTimeMillis();
    private long firstTime = System.currentTimeMillis();

    private int limit = 10;

    public void getMessage() {
        TribeRetrofit.getInstance().createApi(MessageApis.class).getMessage(TribeApplication.getInstance().getUserInfo().getId(), limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> listBaseResponse) {
                        List<HomeMessage> data = listBaseResponse.data.content;
                        if (data != null && data.size() > 0) {
                            lastTime = data.get(data.size() - 1).createTime;
                            firstTime = data.get(0).createTime;
                        }
                        mView.getMessageSuccess(listBaseResponse.data, false);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(), e.getDisplayMessage());
                    }

                });
    }

    public void getMessageMore() {
        TribeRetrofit.getInstance().createApi(MessageApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(), limit, lastTime, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> listBaseResponse) {
                        List<HomeMessage> data = listBaseResponse.data.content;
                        lastTime = data.size() > 0 ? data.get(data.size() - 1).createTime : lastTime;
                        mView.getMessageSuccess(listBaseResponse.data, false);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(), e.getDisplayMessage());
                    }
                });
    }

    public void getMessageNewer() {
        TribeRetrofit.getInstance().createApi(MessageApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(), limit, firstTime, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> listBaseResponse) {
                        List<HomeMessage> data = listBaseResponse.data.content;
                        firstTime = data.size() > 0 ? data.get(0).createTime : firstTime;
                        mView.getMessageSuccess(listBaseResponse.data, true);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(), e.getDisplayMessage());
                    }
                });
    }

    public void getWalletInfo() {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getWallet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> walletAccountBaseResponse) {
                        if (isAttach()) mView.getWalletSuccess(walletAccountBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(500, null);
                    }
                });
    }

    public void deleteMessage(final HomeMessage message) {
        TribeRetrofit.getInstance().createApi(MessageApis.class).deleteMessage(TribeApplication.getInstance().getUserInfo().getId(), message.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.deleteSuccess(message);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(), e.getDisplayMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(500, null);
                        super.onError(e);
                    }
                });
    }

    public void ignoreMessage(final HomeMessage message) {
        TribeRetrofit.getInstance().createApi(MessageApis.class).ignoreMessage(TribeApplication.getInstance().getUserInfo().getId(), message.messageBody.homeMessageType.homeMessageTypeEnum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.ignoreSuccess(message);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(), e.getDisplayMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(500, null);
                        super.onError(e);
                    }
                });
    }
}
