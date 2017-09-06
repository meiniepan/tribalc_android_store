package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.SelfPresenter;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.impl.ISelfView;

import butterknife.Bind;


/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener,ISelfView {
    @Bind(R.id.tv_nickname)
    TextView storeName;
    @Bind(R.id.self_name)
    TextView linkName;
    @Bind(R.id.tv_number)
    TextView mPhone;

    Context mCtx;
    private StoreInfo userInfo;
    private StoreMeta bean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.ll_number).setOnClickListener(this);
        findViewById(R.id.ll_store_name).setOnClickListener(this);
        findViewById(R.id.self_back).setOnClickListener(this);
        findViewById(R.id.ll_name).setOnClickListener(this);
        mCtx = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = TribeApplication.getInstance().getUserInfo();
        initData();
    }

    private void initData() {
        if (null != userInfo) {
            storeName.setText(userInfo.getName());
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_self;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mCtx, ModifyInfoActivity.class);
        switch (view.getId()) {
            case R.id.ll_name:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.LINKMAN);
                startActivityForResult(intent,200);
                break;
            case R.id.ll_store_name:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.ForIntent.STORE_NAME);
                startActivityForResult(intent, 201);
                break;
            case R.id.ll_number:
//                intent.setClass(mCtx, PhoneVerifyActivity.class);
//                startActivity(intent);
                break;
            case R.id.self_back:
                finish();
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 201:
                    linkName.setText(data.getStringExtra(Constant.ForIntent.STORE_NAME));
                    break;
                case 200:
                    storeName.setText(data.getStringExtra(Constant.LINKMAN));
                    break;
            }
        }
    }

    @Override
    public void updateSuccess(String key, String value) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.update_success);
    }

    @Override
    public void showError(int res,String message) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }
}
