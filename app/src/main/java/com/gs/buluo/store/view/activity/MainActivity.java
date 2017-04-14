package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.MainPagerAdapter;
import com.gs.buluo.store.bean.ResponseBody.AppUpdateResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.utils.SharePreferenceManager;
import com.gs.buluo.store.view.fragment.BaseFragment;
import com.gs.buluo.store.view.fragment.CommodityFragment;
import com.gs.buluo.store.view.fragment.MainFragment;
import com.gs.buluo.store.view.fragment.MineFragment;
import com.gs.buluo.store.view.fragment.ManagerFragment;
import com.gs.buluo.store.view.widget.LoadingDialog;
import com.gs.buluo.store.view.widget.panel.UpdatePanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.main_pager)
    ViewPager mPager;
    @Bind(R.id.main_found_text)
    TextView mFound;
    @Bind(R.id.main_mine_text)
    TextView mMine;
    @Bind(R.id.main_usual_text)
    TextView mUsual;
    @Bind(R.id.main_home_text)
    TextView mHome;
    @Bind(R.id.main_found)
    ImageView mFoundImage;
    @Bind(R.id.main_mine)
    ImageView mMineImage;
    @Bind(R.id.main_usual)
    ImageView mUsualImage;
    @Bind(R.id.main_home)
    ImageView mHomeImage;

    private ArrayList<BaseFragment> list;
    private ArrayList<TextView> tabs = new ArrayList<>(4);
    private List<Integer> imageRids = new ArrayList<>(4);
    private List<Integer> imageSelectedRids = new ArrayList<>(4);
    private List<ImageView> tabIcons = new ArrayList<>(4);
    private MineFragment mineFragment;
    private long mkeyTime = 0;
    private CommodityFragment commodityFragment;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String flag = intent.getStringExtra(Constant.ForIntent.FLAG);
        if (TextUtils.equals(flag,Constant.GOODS)){
            commodityFragment.refreshList();
            return;
        }
        if (new StoreInfoDao().findFirst() == null) {
            mineFragment.setLoginState(false);
            commodityFragment.showLogin();
        } else {
            mineFragment.setLoginState(true);
        }
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        list = new ArrayList<>();
        list.add(new MainFragment());
        commodityFragment = new CommodityFragment();
        list.add(commodityFragment);
        list.add(new ManagerFragment());
        mineFragment = new MineFragment();
        list.add(mineFragment);
        findViewById(R.id.main_home_layout).setOnClickListener(new MainOnClickListener(0));
        findViewById(R.id.main_found_layout).setOnClickListener(new MainOnClickListener(1));
        findViewById(R.id.main_usual_layout).setOnClickListener(new MainOnClickListener(2));
        findViewById(R.id.main_mine_layout).setOnClickListener(new MainOnClickListener(3));
        initBar();
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);
        setCurrentTab(0);
//        checkUpdate();
        initUser();

        EventBus.getDefault().register(this);
    }

    private void initUser() {
        StoreInfo first = new StoreInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
    }

    private void initBar() {
        tabs.add(mHome);
        tabs.add(mFound);
        tabs.add(mUsual);
        tabs.add(mMine);
        imageRids.add(R.mipmap.tabbar_home_normal);
        imageRids.add(R.mipmap.tabbar_goods_normal);
        imageRids.add(R.mipmap.tabbar_order_normal);
        imageRids.add(R.mipmap.tabbar_profile_normal);
        imageSelectedRids.add(R.mipmap.tabbar_home_selected);
        imageSelectedRids.add(R.mipmap.tabbar_goods_selected);
        imageSelectedRids.add(R.mipmap.tabbar_order_selected);
        imageSelectedRids.add(R.mipmap.tabbar_profile_selected);
        tabIcons.add(mHomeImage);
        tabIcons.add(mFoundImage);
        tabIcons.add(mUsualImage);
        tabIcons.add(mMineImage);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }


    private void changeFragment(int i) {
        mPager.setCurrentItem(i, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        changeFragment(position);
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class MainOnClickListener implements View.OnClickListener {
        private int mIndex;

        public MainOnClickListener(int index) {
            mIndex = index;
        }

        @Override
        public void onClick(View v) {
            changeFragment(mIndex);
            setCurrentTab(mIndex);
        }
    }

    public void setCurrentTab(int currentTab) {
        for (int i = 0; i < tabs.size(); i++) {
            TextView textView = tabs.get(i);
            ImageView img = tabIcons.get(i);
            if (i == 2) {
//                setBarColor(R.color.black);
            }
            if (i == currentTab) {
                textView.setTextColor(getResources().getColor(R.color.black));
                img.setBackgroundResource(imageSelectedRids.get(i));
            } else {
                textView.setTextColor(getResources().getColor(R.color.main_tab));
                img.setBackgroundResource(imageRids.get(i));
            }
        }
    }

    private void checkUpdate() {
        RequestParams entity = new RequestParams(Constant.Base.BASE + "tribalc/versions/android.json");
        entity.addParameter("t",System.currentTimeMillis());
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                AppUpdateResponse response = JSON.parseObject(result,AppUpdateResponse.class);
                if (checkNeedUpdate(response.v)){
                    new UpdatePanel(getCtx()).show();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    private boolean checkNeedUpdate(String v) {
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            if (!TextUtils.equals(v,version)){
                long lastDenyUpdateTime = SharePreferenceManager.getInstance(getCtx()).getLongValue(Constant.UPDATE_TIME);      //如果用户取消更新，一周问一次
                if (System.currentTimeMillis() - lastDenyUpdateTime>= 7*24*3600*1000){
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                Toast.makeText(this, R.string.exit_app, Toast.LENGTH_LONG).show();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tokenExpired(TokenEvent event) {
        SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).clearValue(Constant.WALLET_PWD);
        new StoreInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);

        Intent intent = new Intent(getCtx(), LoginActivity.class);
        intent.putExtra(Constant.RE_LOGIN, true);
        startActivity(intent);
        if (mineFragment != null) {
            mineFragment.setLoginState(false);
        }
    }
}
