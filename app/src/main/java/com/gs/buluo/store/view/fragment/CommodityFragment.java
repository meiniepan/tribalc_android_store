package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.SaleGoodsListAdapter;
import com.gs.buluo.store.adapter.StoreGoodsListAdapter;
import com.gs.buluo.store.bean.StoreGoodsList;
import com.gs.buluo.store.eventbus.AuthSuccessEvent;
import com.gs.buluo.store.eventbus.GoodsChangedEvent;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.StoreGoodsPresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.Authentication1Activity;
import com.gs.buluo.store.view.activity.CreateGoodsVarietyActivity;
import com.gs.buluo.store.view.activity.LoginActivity;
import com.gs.buluo.store.view.impl.IStoreGoodsView;
import com.gs.buluo.store.view.widget.RecycleViewDivider;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.no_auth_view)
    View authView;
    @Bind(R.id.no_login_view)
    View loginView;
    @Bind(R.id.store_floating)
    View floatButton;

    private Toolbar mToolbar;
    private SearchFragment searchFragment;
    private StoreGoodsListAdapter goodsListAdapter;
    private SaleGoodsListAdapter saleListAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
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
        getActivity().findViewById(R.id.commodity_auth).setOnClickListener(this);
        getActivity().findViewById(R.id.commodity_login_button).setOnClickListener(this);

        floatButton.setOnClickListener(this);
        if (TribeApplication.getInstance().getUserInfo() != null) {
            String authenticationStatus = TribeApplication.getInstance().getUserInfo().getAuthenticationStatus();
//            if (!TextUtils.equals(authenticationStatus, Constant.SUCCEED)) {
//                authView.setVisibility(View.VISIBLE);
//                loginView.setVisibility(View.GONE);
//                floatButton.setVisibility(View.GONE);
//            } else {
                initSaleList();
                initStoreList();
//            }
        } else {
            floatButton.setVisibility(View.GONE);
            loginView.setVisibility(View.VISIBLE);
        }
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
    }

    @Override
    public void getGoodsSuccess(StoreGoodsList data, boolean published) {
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
            recyclerViewSale.showNoData(R.string.no_goods_add);
        } else {
            recyclerViewStore.showNoData(R.string.no_goods_add);
        }
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
                startActivity(new Intent(getActivity(), CreateGoodsVarietyActivity.class));
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
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.commodity_auth:
                startActivity(new Intent(getContext(),Authentication1Activity.class));
                break;
        }
    }

    public void refreshList() {
        saleListAdapter.clear();
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(true);
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
    }

    public void showLogin() {
        if (saleListAdapter!=null&&goodsListAdapter!=null){
            saleListAdapter.clear();
            goodsListAdapter.clear();
        }

        loginView.setVisibility(View.VISIBLE);
        floatButton.setVisibility(View.GONE);
        authView.setVisibility(View.GONE);
        recyclerViewSale.setVisibility(View.GONE);
        recyclerViewStore.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthSuccess(AuthSuccessEvent event) {
        floatButton.setVisibility(View.VISIBLE);
        authView.setVisibility(View.GONE);
        initSaleList();
        initStoreList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(SelfEvent event) {
        recyclerViewSale.setVisibility(View.VISIBLE);
        recyclerViewStore.setVisibility(View.VISIBLE);
        recyclerViewSale.dismissSwipeRefresh();
        recyclerViewStore.dismissSwipeRefresh();
        loginView.setVisibility(View.GONE);
        if (!TextUtils.equals(TribeApplication.getInstance().getUserInfo().authenticationStatus, Constant.SUCCEED)) {
            authView.setVisibility(View.VISIBLE);
            return;
        } else {
            floatButton.setVisibility(View.VISIBLE);
            authView.setVisibility(View.GONE);
        }
        initSaleList();
        initStoreList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodsChanged(GoodsChangedEvent event) {
        goodsListAdapter.clear();
        ((StoreGoodsPresenter) mPresenter).getGoodsListFirst(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
