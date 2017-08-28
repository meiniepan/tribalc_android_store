package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.MainListAdapter;
import com.gs.buluo.store.bean.HomeMessage;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.HomeMessageResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.UnReadMessageBean;
import com.gs.buluo.store.bean.WalletAccount;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.ManagerSwitchEvent;
import com.gs.buluo.store.eventbus.MessageReadEvent;
import com.gs.buluo.store.eventbus.NewMessageEvent;
import com.gs.buluo.store.kotlin.activity.StrategyActivity;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.MainPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.impl.IMainView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;

import static com.tencent.android.tpush.XGPush4Msdk.registerPush;


public class MainActivity extends BaseActivity implements View.OnClickListener, IMainView {
    private long mKeyTime;
    @Bind(R.id.main_recycler)
    XRecyclerView recyclerView;
    @Bind(R.id.store_name)
    TextView tvName;

    MainListAdapter adapter;
    private TextView tvBalance;
    private View withDraw;
    private View bankCard;
    private View topView;
    private ImageView ivIcon;
    private float balance;
    private float poundage;
    private TextView tvType;
    private View titleView;

    private TextView tvRedHor;
    private TextView tvRedVer;

    ArrayList<HomeMessage> list = new ArrayList<>();
    private View goodHor;
    private View orderHor;
    private View secondArea;
    private String TAG = "MainActivity";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (new StoreInfoDao().findFirst() != null) {
            ((MainPresenter) mPresenter).getMessageNewer();
        }
    }

    private void initData() {
        StoreInfo userInfo = initUser();
        tvName.setText(userInfo.getName());
        if (userInfo.getType() == StoreInfo.StoreType.CARD) {
            if (SharePreferenceManager.getInstance(getApplicationContext()).getBooeanValue(Constant.GOODS_SWITCH, false)) {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.VISIBLE);
                orderHor.setVisibility(View.VISIBLE);
            } else {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            }
            withDraw.setVisibility(View.GONE);
            bankCard.setVisibility(View.GONE);
            topView.findViewById(R.id.main_top).setBackgroundResource(R.mipmap.withdraw_bg);
            tvType.setText(R.string.account_balance);
        } else {
            if (SharePreferenceManager.getInstance(getApplicationContext()).getBooeanValue(Constant.GOODS_SWITCH, false)) {
                secondArea.setVisibility(View.VISIBLE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            } else {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            }
            withDraw.setVisibility(View.VISIBLE);
            bankCard.setVisibility(View.VISIBLE);
            topView.findViewById(R.id.main_top).setBackgroundResource(R.mipmap.recharge_bg);
            tvType.setText(R.string.account_withdraw);
        }
        GlideUtils.loadImage(this, TribeApplication.getInstance().getUserInfo().getLogo(), ivIcon, true);
        ((MainPresenter) mPresenter).getMessage();
        ((MainPresenter) mPresenter).getUnReadMessage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onManagerSwitchChanged(ManagerSwitchEvent event) {
        if (TribeApplication.getInstance().getUserInfo().getType() == StoreInfo.StoreType.CARD) {
            if (event.isChecked()) {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.VISIBLE);
                orderHor.setVisibility(View.VISIBLE);
            } else {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            }
        } else {
            if (event.isChecked()) {
                secondArea.setVisibility(View.VISIBLE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            } else {
                secondArea.setVisibility(View.GONE);
                goodHor.setVisibility(View.GONE);
                orderHor.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        registerPush(getApplicationContext(), TribeApplication.getInstance().getUserInfo().getId(), null);
        initView();
        initData();
        setMessageList(topView);
        recyclerView.setScrollAlphaChangeListener(new XRecyclerView.ScrollAlphaChangeListener() {
            @Override
            public void onAlphaChange(int alpha) {
                float gra = (alpha + 0.0f) / 255;
                titleView.setAlpha(gra);
                if (TribeApplication.getInstance().getUserInfo().getType() == StoreInfo.StoreType.PROTOCOL)
                    setBarColorAgain(MainActivity.this, CommonUtils.getGradient(gra, 0xffe4b278, 0xffffffff));
                else
                    setBarColorAgain(MainActivity.this, CommonUtils.getGradient(gra, 0xff7891e5, 0xffffffff));
            }

            @Override
            public int setLimitHeight() {
                return DensityUtils.dip2px(getCtx(), 50);
            }
        });
    }

    private void initView() {
        findViewById(R.id.main_setting).setOnClickListener(this);
        topView = LayoutInflater.from(this).inflate(R.layout.main_item, (ViewGroup) getRootView(), false);
        tvBalance = (TextView) topView.findViewById(R.id.store_balance);
        ivIcon = (ImageView) topView.findViewById(R.id.store_icon);
        withDraw = topView.findViewById(R.id.main_withdraw);
        bankCard = topView.findViewById(R.id.main_bank_card);
        tvType = (TextView) topView.findViewById(R.id.main_type);
        goodHor = topView.findViewById(R.id.main_goods_h);
        orderHor = topView.findViewById(R.id.main_order_h);
        secondArea = topView.findViewById(R.id.goods_hor_area);
        tvRedHor = (TextView) topView.findViewById(R.id.order_un_read_count_hor);
        tvRedVer = (TextView) topView.findViewById(R.id.order_un_read_count_ver);
        titleView = findViewById(R.id.title_view);
        bankCard.setOnClickListener(this);
        withDraw.setOnClickListener(this);
        topView.findViewById(R.id.main_goods).setOnClickListener(this);
        topView.findViewById(R.id.main_order).setOnClickListener(this);
        goodHor.setOnClickListener(this);
        orderHor.setOnClickListener(this);
        topView.findViewById(R.id.main_bill).setOnClickListener(this);
        topView.findViewById(R.id.main_receive).setOnClickListener(this);
        topView.findViewById(R.id.main_strategy).setOnClickListener(this);
    }


    private void setMessageList(View view) {
        adapter = new MainListAdapter(list, this);
        recyclerView.addHeaderView(view);
        recyclerView.setRefreshPosition(1);
        recyclerView.setAdapter(adapter);
        adapter.setPresenter((MainPresenter) mPresenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                ((MainPresenter) mPresenter).getMessageNewer();
            }

            @Override
            public void onLoadMore() {
                ((MainPresenter) mPresenter).getMessageMore();
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainPresenter();
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
            if ((System.currentTimeMillis() - mKeyTime) > 2000) {
                mKeyTime = System.currentTimeMillis();
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
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRead(MessageReadEvent event) {
        tvRedHor.setVisibility(View.GONE);
        tvRedVer.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessage(NewMessageEvent event) {
        setRedCount(TribeApplication.getInstance().getMessageMap());
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
                if (poundage == 0 && balance == 0) {
                    ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    return;
                }
                intent.setClass(this, CashActivity.class);
                intent.putExtra(Constant.POUNDAGE, poundage);
                intent.putExtra(Constant.WALLET_AMOUNT, balance);
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
            case R.id.main_goods:
                intent.setClass(this, CommodityActivity.class);
                startActivity(intent);
                break;
            case R.id.main_order:
                intent.setClass(this, OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.main_goods_h:
                intent.setClass(this, CommodityActivity.class);
                startActivity(intent);
                break;
            case R.id.main_order_h:
                intent.setClass(this, OrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MainPresenter) mPresenter).getWalletInfo();
    }

    @Override
    public void getWalletSuccess(WalletAccount account) {
        TribeApplication.getInstance().setPwd(account.getPassword());
        balance = account.getBalance();
        poundage = account.getWithdrawCharge();
        tvBalance.setText(balance + "");
    }

    @Override
    public void getMessageSuccess(HomeMessageResponse data, boolean isNewer) {
        if (isNewer) { //新消息
            list.addAll(0, data.content);
            adapter.notifyItemRangeInserted(2, data.content.size());
            recyclerView.refreshComplete();
        } else { //老消息
            recyclerView.loadMoreComplete();
            list.addAll(data.content);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), data.content.size());
            if ((!data.hasMore) && list.size() > 2) {
                recyclerView.setNoMore(true);
            }
        }
    }

    @Override
    public void deleteSuccess(HomeMessage message) {
        int pos = list.indexOf(message);
        list.remove(message);
        adapter.notifyItemRemoved(pos + 2);
    }

    @Override
    public void ignoreSuccess(HomeMessage message) {
        ((MainPresenter) mPresenter).getMessage();
        list.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getUnReadMessageSuccess(UnReadMessageBean response) {
        HashMap<HomeMessageEnum, Integer> messageMap = TribeApplication.getInstance().getMessageMap();
        if (messageMap == null || messageMap.isEmpty()) {
            return;
        }
        setRedCount(messageMap);
    }

    private void setRedCount(HashMap<HomeMessageEnum, Integer> messageMap) {
        Integer count = messageMap.get(HomeMessageEnum.ORDER_SETTLE); //need to calculate the amount here
        if (count != null && count != 0) {
            tvRedHor.setVisibility(View.VISIBLE);
            tvRedVer.setVisibility(View.VISIBLE);
            tvRedHor.setText(count + "");
            tvRedVer.setText(count + "");
        } else {
            tvRedHor.setVisibility(View.GONE);
            tvRedVer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int res, String message) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        if (!TextUtils.isEmpty(message)) {
            ToastUtils.ToastMessage(getCtx(), message);
        } else {
            ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
