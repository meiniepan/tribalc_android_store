package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;
import com.gs.buluo.store.bean.UserAddressEntity;
import com.gs.buluo.store.bean.UserSensitiveEntity;
import com.gs.buluo.store.dao.AddressInfoDao;
import com.gs.buluo.store.dao.UserSensitiveDao;
import com.gs.buluo.store.model.AddressModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.view.impl.IAddressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressPresenter extends BasePresenter<IAddressView> {
    AddressModel addressModel;

    public AddressPresenter() {
        addressModel = new AddressModel();
    }


    public void getAddress(String uid, String addId) {
        addressModel.getAddress(uid, addId, new Callback<BaseCodeResponse<UserAddressEntity>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<UserAddressEntity>> call, Response<BaseCodeResponse<UserAddressEntity>> response) {
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<UserAddressEntity>> call, Throwable t) {
            }
        });
    }

    public void deleteAddress(String uid, final UserAddressEntity entity) {
        addressModel.deleteAddress(uid, entity.getId(), new Callback<BaseCodeResponse>() {
            @Override
            public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                if (response.body().code == 204) {
                    new AddressInfoDao().deleteAddress(entity);
                    mView.deleteSuccessInfo(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }


    public void updateDefaultAddress(final UserAddressEntity entity) {
        addressModel.updateDefaultAddress(TribeApplication.getInstance().getUserInfo().getId(), entity.getId(), new TribeCallback<IBaseResponse>() {
            @Override
            public void onSuccess(Response<BaseCodeResponse<IBaseResponse>> response) {
                UserSensitiveDao userSensitiveDao = new UserSensitiveDao();
                UserSensitiveEntity first = userSensitiveDao.findFirst();
                first.setAddressID(entity.getId());
                userSensitiveDao.update(first);
                mView.updateDefaultAddressSuccess(entity);
            }

            @Override
            public void onFail(int responseCode, BaseCodeResponse<IBaseResponse> body) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
