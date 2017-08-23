package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.SaleGoodsListAdapter;
import com.gs.buluo.store.adapter.StoreGoodsListAdapter;
import com.gs.buluo.store.bean.StoreGoodsList;
import com.gs.buluo.store.eventbus.GoodsChangedEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.StoreGoodsPresenter;
import com.gs.buluo.store.view.impl.IStoreGoodsView;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by admin on 2016/11/1.
 */
public class CommodityActivity extends BaseActivity implements IStoreGoodsView, View.OnClickListener {
    @Bind(R.id.store_list_sale)
    RefreshRecyclerView recyclerViewSale;
    @Bind(R.id.store_list_store)
    RefreshRecyclerView recyclerViewStore;
    @Bind(R.id.goods_sale)
    TextView tvSale;
    @Bind(R.id.goods_store)
    TextView tvStore;
    @Bind(R.id.goods_sale_number)
    TextView tvSaleNum;
    @Bind(R.id.goods_store_number)
    TextView tvStoreNum;
    @Bind(R.id.no_auth_view)
    View authView;
    @Bind(R.id.no_login_view)
    View loginView;
    @Bind(R.id.store_floating)
    FloatingActionButton floatButton;

    private Toolbar mToolbar;
    private StoreGoodsListAdapter goodsListAdapter;
    private SaleGoodsListAdapter saleListAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.goods_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ll_goods_sale).setOnClickListener(this);
        findViewById(R.id.ll_goods_store).setOnClickListener(this);
        findViewById(R.id.commodity_auth).setOnClickListener(this);
        findViewById(R.id.commodity_login_button).setOnClickListener(this);

        floatButton.setOnClickListener(this);
        if (TribeApplication.getInstance().getUserInfo() != null) {
            setStatusView();
        } else {
            floatButton.setVisibility(View.GONE);
            loginView.setVisibility(View.VISIBLE);
        }
    }

    private void initStoreList() {
        goodsListAdapter = new StoreGoodsListAdapter(this);
        recyclerViewStore.setAdapter(goodsListAdapter);
        recyclerViewStore.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 12, getResources().getColor(R.color.tint_bg)));
        recyclerViewStore.setSwipeRefreshColorsFromRes(R.color.common_gray, R.color.custom_color_shallow, R.color.custom_yellow);
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
        recyclerViewStore.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                goodsListAdapter.clear();
                ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
            }
        });
        recyclerViewStore.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((StoreGoodsPresenter) mPresenter).getMore(false);
            }
        });
    }

    private void initSaleList() {
        saleListAdapter = new SaleGoodsListAdapter(this);
        recyclerViewSale.setAdapter(saleListAdapter);
        recyclerViewSale.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 12, getResources().getColor(R.color.tint_bg)));
        recyclerViewSale.setSwipeRefreshColorsFromRes(R.color.common_gray, R.color.custom_color_shallow, R.color.custom_yellow);
        recyclerViewSale.showProgress();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(true);
        recyclerViewSale.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                saleListAdapter.clear();
                ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(true);
            }
        });
        recyclerViewSale.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((StoreGoodsPresenter) mPresenter).getMore(true);
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new StoreGoodsPresenter();
    }

    @Override
    public void getGoodsSuccess(StoreGoodsList data, boolean published) {
        recyclerViewSale.dismissProgress();
        if (published) {
            tvSaleNum.setText(data.publishedAmount);
            recyclerViewSale.dismissSwipeRefresh();
            saleListAdapter.addAll(data.content);
        } else {
            tvStoreNum.setText(data.unpublishedAmount);
            recyclerViewStore.dismissSwipeRefresh();
            goodsListAdapter.addAll(data.content);
        }

    }

    @Override
    public void showNoMore(boolean published) {
        if (published)
            recyclerViewSale.showNoMore();
        else
            recyclerViewStore.showNoMore();
    }

    @Override
    public void showNoData(boolean published) {
        if (published) {
            if (saleListAdapter.getData() != null && saleListAdapter.getData().size() == 0)
                recyclerViewSale.showNoData(getString(R.string.no_goods_add));
        } else {
            if (saleListAdapter.getData() != null && saleListAdapter.getData().size() == 0)
                recyclerViewStore.showNoData(getString(R.string.no_goods_add));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_floating:
                startActivity(new Intent(this, CreateGoodsVarietyActivity.class));
                break;
            case R.id.ll_goods_store:
                tvStore.setTextColor(getResources().getColor(R.color.custom_yellow));
                tvSale.setTextColor(0xff2a2a2a);
                recyclerViewStore.setVisibility(View.VISIBLE);
                recyclerViewSale.setVisibility(View.GONE);
                break;
            case R.id.ll_goods_sale:
                tvSale.setTextColor(getResources().getColor(R.color.custom_yellow));
                tvStore.setTextColor(0xff2a2a2a);
                recyclerViewStore.setVisibility(View.GONE);
                recyclerViewSale.setVisibility(View.VISIBLE);
                break;
            case R.id.commodity_login_button:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    public void refreshList() {
        saleListAdapter.clear();
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(true);
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
        recyclerViewStore.setVisibility(View.GONE);
    }

    private void setStatusView() {
        initSaleList();
        initStoreList();
        floatButton.setVisibility(View.VISIBLE);
        recyclerViewStore.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodsChanged(GoodsChangedEvent event) {       //商品下架
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
    }

    @Override
    public void showError(int res, String message) {
        ToastUtils.ToastMessage(this, message);
    }
}
