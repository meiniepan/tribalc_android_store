package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.view.impl.ISelfView;


import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class SelfPresenter extends BasePresenter<ISelfView> {
    private MainModel mainModel;

    public SelfPresenter() {
        mainModel = new MainModel();
    }

    public void  updateUser(final String key, final String value,CreateStoreBean bean){
        mainModel.updateUser(TribeApplication.getInstance().getUserInfo().getId(), key, value, bean, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                if (isAttach())mView.updateSuccess(key,value);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }
}
