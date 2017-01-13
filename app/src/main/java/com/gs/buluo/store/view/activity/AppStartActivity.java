package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.SharePreferenceManager;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity {
    @Bind(R.id.version_name)
    TextView version;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        try {
            version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        beginActivity();
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
    protected void init() {
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
}
