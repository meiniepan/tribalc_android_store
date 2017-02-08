package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.SaleGoodsListAdapter;
import com.gs.buluo.store.adapter.StoreGoodsListAdapter;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.eventbus.GoodsChangedEvent;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.StoreGoodsPresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.CreateGoodsVarietyActivity;
import com.gs.buluo.store.view.impl.IStoreGoodsView;
import com.gs.buluo.store.view.widget.RecycleViewDivider;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2016/11/1.
 */
public class CommodityFragment extends BaseFragment implements IOnSearchClickListener, IStoreGoodsView, View.OnClickListener {
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

    private Toolbar mToolbar;
    private SearchFragment searchFragment;
    private StoreGoodsListAdapter goodsListAdapter;
    private SaleGoodsListAdapter saleListAdapter;
    private boolean published = true;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.goods_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.order_find)
                    searchFragment.show(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
                return true;
            }
        });
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(this);

        EventBus.getDefault().register(this);
        getActivity().findViewById(R.id.ll_goods_sale).setOnClickListener(this);
        getActivity().findViewById(R.id.ll_goods_store).setOnClickListener(this);
        getActivity().findViewById(R.id.store_floating).setOnClickListener(this);
        if (TribeApplication.getInstance().getUserInfo() != null) {
            initSaleList();
            initStoreList();
        } else {
            recyclerViewSale.showNoData("请先登录和创建店铺");
            recyclerViewStore.showNoData("请先登录和创建店铺");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(SelfEvent event) {
        initSaleList();
        initStoreList();
    }

    private void initStoreList() {
        goodsListAdapter = new StoreGoodsListAdapter(getActivity());
        recyclerViewStore.setAdapter(goodsListAdapter);
        recyclerViewStore.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, 12, getResources().getColor(R.color.tint_bg)));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodsChanged(GoodsChangedEvent event) {
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
    }

    private void initSaleList() {
        saleListAdapter = new SaleGoodsListAdapter(getActivity());
        recyclerViewSale.setAdapter(saleListAdapter);
        recyclerViewSale.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, 12, getResources().getColor(R.color.tint_bg)));
        recyclerViewSale.setSwipeRefreshColorsFromRes(R.color.common_gray, R.color.custom_color_shallow, R.color.custom_yellow);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_menu, menu);
    }

    @Override
    public void OnSearchClick(String keyword) {
        ToastUtils.ToastMessage(getActivity(), keyword);
    }

    @Override
    public void getGoodsSuccess(List<GoodsMeta> list, boolean published) {
        if (published) {
            recyclerViewSale.dismissSwipeRefresh();
            saleListAdapter.addAll(list);
        } else {
            recyclerViewStore.dismissSwipeRefresh();
            goodsListAdapter.addAll(list);
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
    public void showError(int res) {
        recyclerViewSale.dismissSwipeRefresh();
        ToastUtils.ToastMessage(getActivity(), res);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_floating:
                if (TribeApplication.getInstance().getUserInfo() == null){
                    ToastUtils.ToastMessage(getContext(),"请先登录才能创建商品");
                    return;
                }
                startActivity(new Intent(getActivity(), CreateGoodsVarietyActivity.class));
                break;
            case R.id.ll_goods_store:
                published = false;
                tvStore.setTextColor(getResources().getColor(R.color.custom_yellow));
                tvSale.setTextColor(0xff2a2a2a);
                recyclerViewStore.setVisibility(View.VISIBLE);
                recyclerViewSale.setVisibility(View.GONE);
                break;
            case R.id.ll_goods_sale:
                tvSale.setTextColor(getResources().getColor(R.color.custom_yellow));
                tvStore.setTextColor(0xff2a2a2a);
                published = true;
                recyclerViewStore.setVisibility(View.GONE);
                recyclerViewSale.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void refreshList() {
        saleListAdapter.clear();
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(true);
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showLogin() {
        saleListAdapter.clear();
        goodsListAdapter.clear();
        recyclerViewSale.showNoData("请先登录和创建店铺");
        recyclerViewStore.showNoData("请先登录和创建店铺");
    }
}
