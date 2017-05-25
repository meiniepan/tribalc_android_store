package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.GoodsListAdapter;
import com.gs.buluo.store.bean.ListGoods;
import com.gs.buluo.store.bean.GoodList;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.GoodsPresenter;
import com.gs.buluo.store.view.impl.IGoodsView;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsListActivity extends BaseActivity implements IGoodsView {

    @Bind(R.id.goods_list)
    RefreshRecyclerView recyclerView;
    List<ListGoods> list;
    private boolean hasMore;
    private GoodsListAdapter adapter;
    @Bind(R.id.goods_list_wrap)
    StatusLayout statusLayout;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        adapter = new GoodsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, GridLayoutManager.HORIZONTAL, 16, getResources().getColor(R.color.tint_bg)));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, GridLayoutManager.VERTICAL, 12, getResources().getColor(R.color.tint_bg)));
        statusLayout.showProgressView();
        ((GoodsPresenter) mPresenter).getGoodsList();

        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((GoodsPresenter) mPresenter).loadMore();
            }
        });

        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoodsPresenter) mPresenter).getGoodsList();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods;
    }

    @Override
    public void getGoodsInfo(GoodList responseList) {
        list = responseList.content;
        adapter.addAll(list);
        hasMore = responseList.hasMore;
        if (!hasMore) {
            adapter.showNoMore();
            return;
        }

        if (list.size() == 0) {
            statusLayout.showErrorView(getString(R.string.no_goods));
        }
    }

    @Override
    public void showError(int res) {
        statusLayout.showErrorView(getString(res));
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsPresenter();
    }
}
