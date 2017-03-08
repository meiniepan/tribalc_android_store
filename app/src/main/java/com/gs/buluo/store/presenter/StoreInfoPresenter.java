package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.StoreSetMealResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IInfoView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/2/9.
 */
public class StoreInfoPresenter extends BasePresenter<IInfoView> {

    public void updateStore(final StoreMeta storeBean) {
        TribeRetrofit.getInstance().createApi(MainApis.class).updateStore(TribeApplication.getInstance().getUserInfo().getId(), storeBean)
                .enqueue(new TribeCallback<CodeResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                        saveStore(storeBean);
                        if (isAttach()) mView.updateSuccess();
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                        if (isAttach()) mView.showError(R.string.connect_fail);
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
        TribeRetrofit.getInstance().createApi(MainApis.class).updateMeal(mealCreation.id,TribeApplication.getInstance().getUserInfo().getId(), mealCreation)
                .enqueue(new TribeCallback<CodeResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                        if (isAttach()) mView.showError(R.string.update_fail);
                    }
                });
    }

    public void getDetailStoreInfo() {
        new MainModel().getDetailStoreInfo(TribeApplication.getInstance().getUserInfo().getId(), new TribeCallback<StoreMeta>() {
            @Override
            public void onSuccess(Response<BaseResponse<StoreMeta>> response) {
                if (isAttach()) mView.setData(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<StoreMeta> body) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });

    }

    public void getSetMeal() {
        new MainModel().getSetMeal(new Callback<StoreSetMealResponse>() {
            @Override
            public void onResponse(Call<StoreSetMealResponse> call, Response<StoreSetMealResponse> response) {
                if (response==null || response.body()==null){
                    mView.showError(R.string.get_fail);
                    return;
                }
                List<StoreSetMealCreation> data = response.body().data;
                if ( response.body() != null && response.body().code == 200 &&data.size()>0 && isAttach()) {
                    mView.setMealData(data.get(0));
                } else if (response.body() != null && response.body().code == 200 && data.size() == 0 && isAttach()) {
                    mView.showError(R.string.get_fail);
                } else {
                    if (isAttach()) mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<StoreSetMealResponse> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }
}
