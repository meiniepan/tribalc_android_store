package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.AppConfigInfo;
import com.gs.buluo.store.bean.ConfigInfo;
import com.gs.buluo.store.bean.PromotionInfo;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity {
//    public MyLocationListener myListener = new MyLocationListener();

    @Bind(R.id.version_name)
    TextView version;
    @Bind(R.id.app_start_bg)
    ImageView viewBg;
    @Bind(R.id.start_second)
    TextView tvSecond;
    @Bind(R.id.start_jump_area)
    View secondView;
//    private LocationClient mLocClient;
    private String versionName;

    private List<PromotionInfo> promotionInfos;
    private PromotionInfo current = null;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        mLocClient.start();
        File file = new File(Constant.DIR_PATH);
        if (!file.exists()) file.mkdirs();
        setupPromotion();
    }

    private void setupPromotion() {
        String json = SharePreferenceManager.getInstance(getApplicationContext()).getStringValue(Constant.APP_START);
        promotionInfos = JSON.parseArray(json, PromotionInfo.class);
        long currentTime = System.currentTimeMillis();
        if (promotionInfos != null && promotionInfos.size() != 0) {
            Iterator<PromotionInfo> iterator = promotionInfos.iterator();
            while (iterator.hasNext()) {
                PromotionInfo info = iterator.next();
                if (currentTime > info.endTime) {
                    iterator.remove();
                    continue;
                }
                if (currentTime >= info.startTime && currentTime <= info.endTime) {
                    current = info;
                }
            }
        }
        if (current != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.click_jump_area).setVisibility(View.VISIBLE);
                    setData(current);
                }
            }, 1500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    beginActivity();
                }
            }, 2000);
        }

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

    public void setData(PromotionInfo data) {
        if (data.canSkip) {
            secondView.setVisibility(View.VISIBLE);
            findViewById(R.id.click_jump_area).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beginActivity();
                }
            });
        }
        Glide.with(this).load(data.url).into(viewBg);
        startTime = data.skipSeconds;
        startCounter();
    }

    int startTime = 1;
    private Subscriber<Long> subscriber;

    private void startCounter() {
        tvSecond.setText(startTime + "");
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                beginActivity();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                tvSecond.setText(aLong + "");
            }
        };
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return startTime - time;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    private void saveData(ConfigInfo data) {
        if (promotionInfos != null && promotionInfos.size() != 0) {
            if (!TextUtils.equals(data.promotions.url, promotionInfos.get(promotionInfos.size() - 1).url)) {
                promotionInfos.add(data.promotions);
            }
        } else {
            promotionInfos = new ArrayList<>();
            promotionInfos.add(data.promotions);
        }
        SharePreferenceManager.getInstance(getApplicationContext()).setValue(Constant.APP_START, JSON.toJSONString(promotionInfos));

        TribeApplication.getInstance().setBf_recharge(data.switches.bf_recharge);
        TribeApplication.getInstance().setBf_withdraw(data.switches.bf_withdraw);

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
            startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
            finish();
        } else {
            if (checkUser(this)){
                startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                finish();
            }else {
                finish();
            }
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

//    public class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // map view 销毁后不在处理新接收的位置
//            if (location != null) {
//                LatLng myPos = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//
//                TribeApplication.getInstance().setPosition(myPos);
//            }
//        }
//
//        public void onReceivePoi(BDLocation poiLocation) {
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocClient.stop();
        if (subscriber != null) subscriber.unsubscribe();
    }
}
