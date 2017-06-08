package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BillResponse;
import com.gs.buluo.store.model.MoneyModel;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IBillView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/18.
 */
public class BillPresenter extends BasePresenter<IBillView> {

    private final MoneyModel moneyModel;
    private String nextSkip;

    public BillPresenter() {
        moneyModel = new MoneyModel();
    }

    public void getBillListFirst(boolean isFace) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getBillListFirst(TribeApplication.getInstance().getUserInfo().getId(), 20, isFace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BillResponse>() {
                    @Override
                    public void onNext(BillResponse billResponse) {
                        mView.getBillSuccess(billResponse.data);
                        nextSkip = billResponse.data.nextSkip;
                    }
                });
    }

    public void loadMoreBill(boolean isFace) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getBillList(TribeApplication.getInstance().getUserInfo().getId(), 20, nextSkip, isFace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BillResponse>() {
                    @Override
                    public void onNext(BillResponse billResponse) {
                        mView.getBillSuccess(billResponse.data);
                        nextSkip = billResponse.data.nextSkip;
                    }
                });
    }
}
