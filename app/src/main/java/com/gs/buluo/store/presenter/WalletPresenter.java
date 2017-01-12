package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.model.MoneyModel;
import com.gs.buluo.store.view.impl.IWalletView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletPresenter extends BasePresenter<IWalletView> {

    MoneyModel moneyModel;

    public WalletPresenter() {
        moneyModel = new MoneyModel();
    }

    public void getWalletInfo() {
        moneyModel.getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseResponse<WalletAccount>>() {
            @Override
            public void onResponse(Call<BaseResponse<WalletAccount>> call, Response<BaseResponse<WalletAccount>> response) {
                if (response.body() != null && response.body().code == 200 && response.body().data != null) {
                    mView.getWalletInfoFinished(response.body().data);
                } else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<WalletAccount>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
