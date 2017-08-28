package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.OrderFragmentAdapter;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.RequestBodyBean.ReadMsgRequest;
import com.gs.buluo.store.eventbus.MessageReadEvent;
import com.gs.buluo.store.network.MessageApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.widget.UnScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import butterknife.Bind;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderActivity extends BaseActivity {
    @Bind(R.id.order_pager)
    UnScrollViewPager pager;
    @Bind(R.id.order_tab)
    TabLayout tabLayout;
    private Toolbar mToolbar;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.order_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.wait_send), getString(R.string.wait_receive), getString(R.string.complete)};
        OrderFragmentAdapter adapter =
                new OrderFragmentAdapter(this, getSupportFragmentManager(), Arrays.asList(titles));
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        View tabView;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabView = adapter.getTabView(i);
            if (i == 2) {
                TextView tvRed = (TextView) tabView.findViewById(R.id.tv_tab_red);
                Integer count = TribeApplication.getInstance().getMessageMap().get(HomeMessageEnum.ORDER_SETTLE);
                if (count != null && count != 0) {
                    tvRed.setVisibility(View.VISIBLE);
                    tvRed.setText(count + "");
                }
            }
            tabLayout.getTabAt(i).setCustomView(tabView);
        }
        tabLayout.setOnTabSelectedListener(new MyTabSelectListener());
        pager.setCurrentItem(getIntent().getIntExtra(Constant.TYPE, 0), false);
    }

    private class MyTabSelectListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            pager.setCurrentItem(tab.getPosition());
            if (tab.getPosition() == 2) {
                readOrderMessage(HomeMessageEnum.ORDER_SETTLE);
            }
            View customView = tab.getCustomView();
            customView.findViewById(R.id.tv_tab_red).setVisibility(View.GONE);
            TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
            textView.setTextColor(getResources().getColor(R.color.common_dark));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            View customView = tab.getCustomView();
            TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
            textView.setTextColor(getResources().getColor(R.color.common_gray));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private void readOrderMessage(final HomeMessageEnum type) {
        TribeRetrofit.getInstance().createApi(MessageApis.class).readMessage(TribeApplication.getInstance().getUserInfo().getId(), new ReadMsgRequest(type))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        TribeApplication.getInstance().getMessageMap().put(type, 0);
                        EventBus.getDefault().post(new MessageReadEvent(type));
                    }
                });
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_order;
    }

}
