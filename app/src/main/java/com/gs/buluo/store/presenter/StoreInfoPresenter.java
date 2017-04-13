package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IInfoView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/2/9.
 */
public class StoreInfoPresenter extends BasePresenter<IInfoView> {

    public void updateStore(final StoreMeta storeBean) {
        TribeRetrofit.getInstance().createApi(MainApis.class).updateStore(TribeApplication.getInstance().getUserInfo().getId(), storeBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        saveStore(storeBean);
                        if (isAttach()) mView.updateSuccess();
                    }
                });
    }

    private void saveStore(StoreMeta data) {
        StoreInfoDao storeInfoDao = new StoreInfoDao();
        StoreInfo first = storeInfoDao.findFirst();
        first.setLogo(data.getLogo());
        first.setName(data.getName());
        storeInfoDao.update(first);
        EventBus.getDefault().post(new SelfEvent());
    }

    public void updateMeal(StoreSetMealCreation mealCreation) {
        TribeRetrofit.getInstance().createApi(MainApis.class).updateMeal(mealCreation.id, TribeApplication.getInstance().getUserInfo().getId(), mealCreation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                    }
                });
    }

    public void getDetailStoreInfo() {
        TribeRetrofit.getInstance().createApi(MainApis.class).getStoreMeta(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreMeta>>() {
                    @Override
                    public void onNext(BaseResponse<StoreMeta> response) {
                        if (isAttach()) mView.setData(response.data);
                    }
                });
    }

    public void getSetMeal() {
        TribeRetrofit.getInstance().createApi(MainApis.class).getCreateSetMeal(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<StoreSetMealCreation>>>() {
                    @Override
                    public void onNext(BaseResponse<List<StoreSetMealCreation>> response) {
                        List<StoreSetMealCreation> data = response.data;
                        mView.setMealData(data.get(0));
                    }
                });
    }
}
