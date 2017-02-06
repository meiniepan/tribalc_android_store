package com.gs.buluo.store.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.CustomAlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/20.
 */
public class GoodsStoreInfoActivity extends BaseActivity implements Callback<BaseResponse<StoreMeta>>, View.OnClickListener {
    @Bind(R.id.info_store_name)
    EditText tvName;
    @Bind(R.id.info_sub_store_name)
    EditText tvSubName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
    @Bind(R.id.info_send_address)
    TextView tvSend;
    @Bind(R.id.store_info_logo)
    TextView tvLogo;
    private StoreMeta storeBean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.ll_store_info_address).setOnClickListener(this);
        findViewById(R.id.info_store_introduction).setOnClickListener(this);
        new MainModel().getDetailStoreInfo(TribeApplication.getInstance().getUserInfo().getId(),this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_activity;
    }

    @Override
    public void onResponse(Call<BaseResponse<StoreMeta>> call, Response<BaseResponse<StoreMeta>> response) {
        if (response != null && response.body() != null && response.body().code == 200) {
            initData(response.body().data);
        }
    }

    private void initData(StoreMeta data) {
        storeBean = data;
        tvName.setText(data.name);
        tvSubName.setText(data.subbranchName);
        tvCategory.setText(data.category.toString());
        tvSend.setText(data.province + data.city + data.district + data.address);
    }

    @Override
    public void onFailure(Call<BaseResponse<StoreMeta>> call, Throwable t) {
        ToastUtils.ToastMessage(this, R.string.connect_fail);
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
            case R.id.info_store_introduction:
                intent.setClass(getCtx(), IntroductionActivity.class);
                intent.putExtra(Constant.ForIntent.INTRODUCTION,storeBean.desc);
                startActivityForResult(intent, 203);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void updateStoreInfo() {
        showLoadingDialog();
        storeBean.name = tvName.getText().toString().trim();
        storeBean.subbranchName = tvSubName.getText().toString().trim();
        String key = "logo,name,subbranchName,desc,province,city,district,address";
        new MainModel().updateStore(TribeApplication.getInstance().getUserInfo().getId(),key , "", storeBean, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                saveStore(storeBean);
                ToastUtils.ToastMessage(getCtx(), R.string.update_success);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
            }
        });
    }

    private void saveStore(StoreMeta data) {
        StoreInfoDao storeInfoDao = new StoreInfoDao();
        StoreInfo first = storeInfoDao.findFirst();
        data.setToken(first.token);
        storeInfoDao.update(data);
        TribeApplication.getInstance().setUserInfo(data);
        EventBus.getDefault().post(new SelfEvent());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean.logo = data.getStringExtra(Constant.LOGO);
            tvLogo.setText("1张");
        }else if (requestCode == 203 && resultCode == RESULT_OK) {
            storeBean.desc = data.getStringExtra(Constant.ForIntent.INTRODUCTION);
        }else if (data != null &&requestCode == 202 && resultCode == RESULT_OK) {
            String area = data.getStringExtra(Constant.AREA);
            String address = data.getStringExtra(Constant.ADDRESS);
            String[] arrs = area.split("-");
            storeBean.province = arrs[0];
            storeBean.city = arrs[1];
            storeBean.district = arrs[2];
            storeBean.address = address;
            tvSend.setText(storeBean.province + storeBean.city + storeBean.district + storeBean.address);
        }
    }

    @Override
    public void onBackPressed() {
        new CustomAlertDialog.Builder(this).setTitle("提示").setMessage("确定保存更改么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStoreInfo();
            }
        }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}
