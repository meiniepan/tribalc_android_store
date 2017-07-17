package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.MainListAdapter;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.kotlin.activity.StrategyActivity;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.widget.MoneyTextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private long mkeyTime;
    @Bind(R.id.main_recycler)
    XRecyclerView recyclerView;
    @Bind(R.id.store_name)
    TextView tvName;

    MainListAdapter adapter;
    private MoneyTextView tvBalance;
    private View withDraw;
    private View bankCard;
    private View topView;
    private ImageView ivIcon;
    private float balance;
    private float poundage;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (new StoreInfoDao().findFirst() != null) {
            initData();
        }
    }

    private void initData() {
        StoreInfo userInfo = initUser();
        tvName.setText(userInfo.getName());
        tvBalance.setMoneyText(userInfo.getBalance());
        if (userInfo.getType() == StoreInfo.StoreType.CARD) {
            withDraw.setVisibility(View.GONE);
            bankCard.setVisibility(View.GONE);
            topView.findViewById(R.id.main_top).setBackgroundResource(R.mipmap.withdraw_bg);
        }
        GlideUtils.loadImage(this, TribeApplication.getInstance().getUserInfo().getLogo(), ivIcon, true);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.main_setting).setOnClickListener(this);
        topView = LayoutInflater.from(this).inflate(R.layout.main_item, (ViewGroup) getRootView(), false);
        tvBalance = (MoneyTextView) topView.findViewById(R.id.store_balance);
        ivIcon = (ImageView) topView.findViewById(R.id.store_icon);
        withDraw = topView.findViewById(R.id.main_withdraw);
        bankCard = topView.findViewById(R.id.main_bank_card);
        bankCard.setOnClickListener(this);
        withDraw.setOnClickListener(this);
        topView.findViewById(R.id.main_bill).setOnClickListener(this);
        topView.findViewById(R.id.main_receive).setOnClickListener(this);
        topView.findViewById(R.id.main_strategy).setOnClickListener(this);

        initData();
        setMessageList(topView);
        EventBus.getDefault().register(this);
    }

    private void setMessageList(View view) {
        ArrayList list = new ArrayList<>();
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        list.add("jhahhahahaha");
        adapter = new MainListAdapter(list, this);
        recyclerView.addHeaderView(view);
        recyclerView.setRefreshPosition(1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private StoreInfo initUser() {
        StoreInfo first = new StoreInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
        return first;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
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
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_receive:
                intent.setClass(this, PayCodeActivity.class);
                this.startActivity(intent);
                break;
            case R.id.main_withdraw:
                if (poundage==0&&balance==0){
                    ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
                    return;
                }
                intent.setClass(this, CashActivity.class);
                intent.putExtra(Constant.POUNDAGE,poundage);
                intent.putExtra(Constant.WALLET_AMOUNT,balance);
                startActivity(intent);
                break;
            case R.id.main_bank_card:
                intent.setClass(this, BankCardActivity.class);
                startActivity(intent);
                break;
            case R.id.main_bill:
                intent.setClass(this, BillActivity.class);
                startActivity(intent);
                break;
            case R.id.main_strategy:
                intent.setClass(this, StrategyActivity.class);
                startActivity(intent);
                break;
            case R.id.main_setting:
                intent.setClass(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getWallet(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> walletAccountBaseResponse) {
                        TribeApplication.getInstance().setPwd(walletAccountBaseResponse.data.getPassword());
                        balance = walletAccountBaseResponse.data.getBalance();
                        poundage = walletAccountBaseResponse.data.getWithdrawCharge();
                        tvBalance.setMoneyText(balance +"");
                    }
                });
    }
}
