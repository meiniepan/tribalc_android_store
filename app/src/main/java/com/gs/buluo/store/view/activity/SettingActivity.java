package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.DataCleanManager;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.AppConfigInfo;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.ManagerSwitchEvent;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/7.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @Bind(R.id.mine_switch)
    Switch mSwitch;
    @Bind(R.id.setting_cache_size)
    TextView tvCache;
    @Bind(R.id.goods_switch)
    Switch sGoods;
    private StoreInfo info;
    private Context mCtx;
    private CustomAlertDialog customAlertDialog;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        StoreInfoDao dao = new StoreInfoDao();
        info = dao.findFirst();
        setSwitch();
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (info == null) {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    return true;
                }
                return false;
            }
        });
        sGoods.setChecked(SharePreferenceManager.getInstance(getApplicationContext()).getBooeanValue(Constant.GOODS_SWITCH, false));

        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_recall).setOnClickListener(this);
        findViewById(R.id.setting_update).setOnClickListener(this);
        findViewById(R.id.setting_pwd).setOnClickListener(this);
        findViewById(R.id.setting_info).setOnClickListener(this);
        findViewById(R.id.mine_message_manager).setOnClickListener(this);
        sGoods.setOnCheckedChangeListener(this);
        String cacheSize = null;
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cacheSize != null)
            tvCache.setText(cacheSize);
    }

    private void setSwitch() {
        if (null == info) {
            mSwitch.setChecked(false);
        } else {
//            if (info.isNotify()){
//                mSwitch.setChecked(true);
//            }else {
//                mSwitch.setChecked(false);
//            }
        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.goods_switch) {
            SharePreferenceManager.getInstance(getApplicationContext()).setValue(Constant.GOODS_SWITCH, isChecked);
            EventBus.getDefault().post(new ManagerSwitchEvent(isChecked));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_recall:
                intent.setClass(mCtx, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:
                new CustomAlertDialog.Builder(this).setTitle("提示").setMessage("确定清除所有缓存?").setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tvCache.setText("0K");
                    }
                }).setNegativeButton(mCtx.getString(R.string.cancel), null).create().show();
                break;
            case R.id.setting_update:
                checkUpdate();
                break;
            case R.id.exit:
                customAlertDialog = new CustomAlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("您确定要退出登录吗?")
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                customAlertDialog.dismiss();
                            }
                        }).create();
                customAlertDialog.show();
                break;
            case R.id.setting_pwd:
                String pwd = TribeApplication.getInstance().getPwd();
                if (pwd == null) {
                    intent.setClass(this, UpdateWalletPwdActivity.class);
                } else {
                    intent.setClass(this, ConfirmActivity.class);
                    intent.putExtra(Constant.WALLET_PWD, pwd);
                }
                startActivity(intent);
                break;
            case R.id.setting_info:
                intent.setClass(this, StoreInfoDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_message_manager:
                intent.setClass(this, MessageManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void logout() {
        Intent intent = new Intent();
        SharePreferenceManager.getInstance(getApplicationContext()).clearValue(Constant.WALLET_PWD);
        SharePreferenceManager.getInstance(getApplicationContext()).clearValue(Constant.GOODS_SWITCH);
        new StoreInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);
        intent.setClass(mCtx, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String versionName;

    private void checkUpdate() {
        showLoadingDialog();
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TribeRetrofit.getInstance().createApi(MainApis.class).getLastVersion(versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AppConfigInfo>>() {
                    @Override
                    public void onNext(BaseResponse<AppConfigInfo> config) {
                        String[] nows = versionName.split("\\.");
                        String[] minis = config.data.minVersion.split("\\.");
                        for (int i = 0; i < minis.length; i++) {
                            if (Integer.parseInt(nows[i]) < Integer.parseInt(minis[i])) {
                                EventBus.getDefault().postSticky(new UpdateEvent(false, config.data.lastVersion, config.data.releaseNote));
                                return;
                            }
                        }

                        if (!TextUtils.equals(config.data.lastVersion.substring(0, config.data.lastVersion.lastIndexOf(".")), versionName.substring(0, config.data.lastVersion.lastIndexOf(".")))) {
                            EventBus.getDefault().postSticky(new UpdateEvent(true, config.data.lastVersion, config.data.releaseNote));
                        } else {
                            ToastUtils.ToastMessage(getCtx(), R.string.current_newest);
                        }
                    }
                });
    }
}
