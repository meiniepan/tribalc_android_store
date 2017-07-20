package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.common.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.BillListAdapter;
import com.gs.buluo.store.adapter.WithdrawListAdapter;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.bean.ResponseBody.BillResponse;
import com.gs.buluo.store.bean.ResponseBody.WithdrawBillResponse;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.BillPresenter;
import com.gs.buluo.store.view.impl.IBillView;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillActivity extends BaseActivity implements IBillView, View.OnClickListener {
    @Bind(R.id.bill_list)
    RefreshRecyclerView recyclerView;
    @Bind(R.id.bill_status)
    StatusLayout statusLayout;

    BillListAdapter adapter;
    WithdrawListAdapter withdrawAdapter;
    List<BillEntity> list = new ArrayList<>();
    List<WithdrawBill> withdrawBills = new ArrayList<>();
    private boolean isFace;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        boolean isWithdraw = getIntent().getBooleanExtra(Constant.WITHDRAW_FLAG, false);
        isFace = getIntent().getBooleanExtra(Constant.FACE, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.tint_bg)));

        if (isWithdraw) {
            ((BillPresenter) mPresenter).getWithdrawBill();
        } else {
            ((BillPresenter) mPresenter).getBillListFirst(isFace);
        }
        statusLayout.showProgressView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BillPresenter();
    }

    @Override
    public void getBillSuccess(BillResponse response) {
        adapter = new BillListAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((BillPresenter) mPresenter).loadMoreBill(isFace);
            }
        });
        adapter.addAll(response.content);
        if (adapter.getData() == null || adapter.getData().size() == 0) {
            statusLayout.showEmptyView(getString(R.string.no_bill));
            return;
        }
        statusLayout.showContentView();
        if (!response.hasMore) {
            adapter.showNoMore();
        }
    }

    @Override
    public void getWithdrawBillSuccess(WithdrawBillResponse billList) {
        withdrawAdapter = new WithdrawListAdapter(this, withdrawBills);
        recyclerView.setAdapter(withdrawAdapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((BillPresenter) mPresenter).loadMoreWithdrawBill();
            }
        });
        withdrawAdapter.addAll(billList.content);
        if (withdrawAdapter.getData() == null || withdrawAdapter.getData().size() == 0) {
            statusLayout.showEmptyView(getString(R.string.no_bill));
            return;
        }
        statusLayout.showContentView();
        if (!billList.hasMore) {
            adapter.showNoMore();
        }
    }

    @Override
    public void showError(int res,String message) {
        statusLayout.showErrorView(getString(res));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        }
    }
}
