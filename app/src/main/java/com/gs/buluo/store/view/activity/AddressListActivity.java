package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.AddressAdapter;
import com.gs.buluo.store.bean.UserAddressEntity;
import com.gs.buluo.store.dao.AddressInfoDao;
import com.gs.buluo.store.presenter.AddressPresenter;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IAddressView;
import com.gs.buluo.store.view.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by hjn on 2016/11/2.
 */
public class AddressListActivity extends BaseActivity implements IAddressView {
    @Bind(R.id.address_list)
    RecyclerView mRecView;

    List<UserAddressEntity> mDatas = new ArrayList<>();
    private final int REQUEST_ADD = 200;
    private final int REQUEST_UPDATE = 201;

    private AddressAdapter mAdapter;
    private AddressInfoDao addressInfoDao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.address_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });
        findViewById(R.id.address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addressInfoDao = new AddressInfoDao();
        mDatas = addressInfoDao.findAll(TribeApplication.getInstance().getUserInfo().getId());
        if (null == mDatas || mDatas.size() == 0) {
            return;
        }
        boolean fromOrder = getIntent().getBooleanExtra(Constant.ForIntent.FROM_ORDER, false);

        mAdapter = new AddressAdapter(this, mDatas);

        mAdapter.setFromOrder(fromOrder);
        mRecView.setAdapter(mAdapter);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_detail_address;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            UserAddressEntity entity = (UserAddressEntity) data.getSerializableExtra(Constant.ADDRESS);
            if (mDatas != null) {
                mDatas.add(entity);
            }
            if (mAdapter == null) {
                mAdapter = new AddressAdapter(this, mDatas);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            mDatas = addressInfoDao.findAll(TribeApplication.getInstance().getUserInfo().getId());
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public AddressPresenter getAddressPresenter() {
        return ((AddressPresenter) mPresenter);
    }

    @Override
    public void deleteSuccessInfo(UserAddressEntity data) {
        LoadingDialog.getInstance().dismissDialog();
        mDatas.remove(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDefaultAddressSuccess(UserAddressEntity data) {
        LoadingDialog.getInstance().dismissDialog();
        mAdapter.setDefaultAddressID(data.getId());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(int res) {
        LoadingDialog.getInstance().dismissDialog();
        ToastUtils.ToastMessage(this, getString(res));
    }


}
