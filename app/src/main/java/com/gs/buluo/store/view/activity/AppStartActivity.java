package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.store.bean.AppConfigInfo;
import com.gs.buluo.store.bean.ConfigInfo;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.versionName;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity {
    public MyLocationListener myListener = new MyLocationListener();

    @Bind(R.id.version_name)
    TextView version;
    private LocationClient mLocClient;
    private String versionName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
        File file =new File(Constant.DIR_PATH);
        if (!file.exists())file.mkdirs();
        checkUpdate();
        beginActivity();
    }

    private void checkUpdate() {
        String uid = TribeApplication.getInstance().getUserInfo() == null ? null : TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MainApis.class).getConfig(uid, versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConfigInfo>>(false) {
                    @Override
                    public void onNext(BaseResponse<ConfigInfo> response) {
                        saveData(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getCtx(), "当前网络状况不佳");
                    }
                });
    }

    private void saveData(ConfigInfo data) {
        final AppConfigInfo app = data.app;
        String[] nows = versionName.split("\\.");
        String[] minis = app.minVersion.split("\\.");
        for (int i = 0; i < minis.length; i++) {
            if (Integer.parseInt(nows[i]) < Integer.parseInt(minis[i])) {
                EventBus.getDefault().postSticky(new UpdateEvent(false, app.lastVersion, app.releaseNote));
                return;
            }
        }
        if (TextUtils.equals(app.lastVersion, SharePreferenceManager.getInstance(getCtx()).getStringValue(Constant.CANCEL_UPDATE_VERSION))) {
            return;
        }

        if (!TextUtils.equals(app.lastVersion.substring(0, app.lastVersion.lastIndexOf(".")), versionName.substring(0, app.lastVersion.lastIndexOf(".")))) {
            EventBus.getDefault().postSticky(new UpdateEvent(true, app.lastVersion, app.releaseNote));
        }
    }

    private void beginActivity() {
        if (SharePreferenceManager.getInstance(this).getBooeanValue("guide" + getVersionCode())) {
            SharePreferenceManager.getInstance(this).setValue("guide" + getVersionCode(), false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
                    finish();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }


    public int getVersionCode() {
        PackageManager manager;

        PackageInfo info = null;

        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location != null) {
                LatLng myPos = new LatLng(location.getLatitude(),
                        location.getLongitude());

                TribeApplication.getInstance().setPosition(myPos);
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
    }
}
