package com.gs.buluo.store.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.StoreInfoPresenter;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IInfoView;
import com.gs.buluo.common.widget.CustomAlertDialog;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/20.
 */
public class GoodsStoreInfoActivity extends BaseActivity implements View.OnClickListener,IInfoView {
    @Bind(R.id.info_store_name)
    EditText tvName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
    @Bind(R.id.info_send_address)
    TextView tvSend;
    @Bind(R.id.store_info_logo)
    TextView tvLogo;
    @Bind(R.id.info_store_introduction)
    EditText etDesc;
    private StoreMeta storeBean;
    @Bind(R.id.info_store_auth)
    Button auth;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.ll_store_info_address).setOnClickListener(this);
        auth.setOnClickListener(this);
        ((StoreInfoPresenter)mPresenter).getDetailStoreInfo();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_activity;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.info_store_logo:
                intent.setClass(getCtx(), PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivityForResult(intent, 200);
                break;
            case R.id.info_store_save:
                updateStoreInfo();
                break;
            case R.id.ll_store_info_address:
                intent.setClass(getCtx(), CreateStoreAddressActivity.class);
                startActivityForResult(intent, 202);
                break;
            case R.id.info_store_auth:
                intent.setClass(getCtx(), Authentication1Activity.class);
                startActivity(intent);
                break;
            case R.id.back:
                back();
                break;
        }
    }

    private void updateStoreInfo() {
        showLoadingDialog();
        storeBean.name = tvName.getText().toString().trim();
        storeBean.desc = etDesc.getText().toString().trim();
        ((StoreInfoPresenter)mPresenter).updateStore(storeBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean.logo = data.getStringExtra(Constant.LOGO);
            tvLogo.setText("1张");
        }else if (data != null &&requestCode == 202 && resultCode == RESULT_OK) {
            String area = data.getStringExtra(Constant.AREA);
            String address = data.getStringExtra(Constant.ADDRESS);
            String[] arrs = area.split("-");
            storeBean.province = arrs[0];
            storeBean.city = arrs[1];
            storeBean.district = arrs[2];
            storeBean.address = address;
            double lan = data.getDoubleExtra(Constant.LATITUDE, 0);
            double lon = data.getDoubleExtra(Constant.LONGITUDE,0);
            storeBean.coordinate = new double[]{lon,lan};
            tvSend.setText(storeBean.province + storeBean.city + storeBean.district + storeBean.address);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        new CustomAlertDialog.Builder(this).setTitle(getString(R.string.reminder)).setMessage(getString(R.string.save_update)).
                setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStoreInfo();
                    }
                }).setNegativeButton(getString(R.string.not_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }

    @Override
    public void updateSuccess() {
        ToastUtils.ToastMessage(this,R.string.update_success);
        finish();
    }

    @Override
    public void setData(StoreMeta data) {
        if (TextUtils.equals(data.authenticationStatus,"NOT_START")){
            auth.setVisibility(View.VISIBLE);
        }
        storeBean = data;
        tvName.setText(data.name);
        if (data.category!=null)tvCategory.setText(data.category.toString());
        tvSend.setText(data.province + data.city + data.district + data.address);
        etDesc.setText(data.desc);
    }

    @Override
    public void setMealData(StoreSetMealCreation mealCreation) {
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,res);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new StoreInfoPresenter();
    }
}
