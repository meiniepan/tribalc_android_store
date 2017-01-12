package com.gs.buluo.store.presenter;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.UserAddressEntity;
import com.gs.buluo.store.dao.AddressInfoDao;
import com.gs.buluo.store.model.AddressModel;
import com.gs.buluo.store.view.impl.IAddAddressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/15.
 */
public class AddAddressPresenter extends BasePresenter<IAddAddressView> {

    AddressModel addressModel;

    public AddAddressPresenter() {
        addressModel = new AddressModel();
    }

    public void addAddress(String uid, final UserAddressEntity entity) {
        addressModel.addAddress(uid, entity, new Callback<BaseResponse<UserAddressEntity>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserAddressEntity>> call, Response<BaseResponse<UserAddressEntity>> response) {
                if (response.body().code == 201) {
                    UserAddressEntity addressEntity = response.body().data;
                    addressEntity.setArea(addressEntity.getProvice(), addressEntity.getCity(), addressEntity.getDistrict());
                    addressEntity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                    new AddressInfoDao().saveBindingId(addressEntity);
                    mView.addAddressSuccess(addressEntity);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserAddressEntity>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateAddress(String uid, String addId, final UserAddressEntity entity) {
        addressModel.updateAddress(uid, addId, entity, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().code == 200) {
                    entity.setArea(entity.getProvice(), entity.getCity(), entity.getDistrict());
                    new AddressInfoDao().update(entity);
                    mView.updateAddressSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
