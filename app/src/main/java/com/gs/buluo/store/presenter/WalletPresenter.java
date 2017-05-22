package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.model.MoneyModel;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IWalletView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletPresenter extends BasePresenter<IWalletView> {
    public void getWalletInfo() {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getWallet(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        if (isAttach()) mView.getWalletInfoFinished(response.data);
                    }
                });
    }
}
