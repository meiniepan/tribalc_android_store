package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.OrderFragmentAdapter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.UnScrollViewPager;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.Arrays;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderActivity extends BaseActivity implements IOnSearchClickListener {
    @Bind(R.id.order_pager)
    UnScrollViewPager pager;
    @Bind(R.id.order_tab)
    TabLayout tabLayout;
    private Toolbar mToolbar;
    private SearchFragment searchFragment;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.order_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        mToolbar.setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
                return true;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.wait_send), getString(R.string.wait_receive),getString(R.string.complete)};
        OrderFragmentAdapter adapter =
                new OrderFragmentAdapter(getSupportFragmentManager(), Arrays.asList(titles));
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabsFromPagerAdapter(adapter);
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(this);
        pager.setCurrentItem(getIntent().getIntExtra(Constant.TYPE, 0), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.order_menu,menu);
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnSearchClick(String keyword) {
//        ToastUtils.ToastMessage(this, keyword);
    }
}
