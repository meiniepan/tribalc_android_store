package com.gs.buluo.store.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.OrderListAdapter;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponseBean;
import com.gs.buluo.store.eventbus.PaymentEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.OrderPresenter;
import com.gs.buluo.store.view.impl.IOrderView;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragment extends BaseFragment implements IOrderView {
    private int type;

    @Bind(R.id.order_list)
    RefreshRecyclerView recyclerView;
    @Bind(R.id.order_layout)
    StatusLayout statusLayout;

    OrderListAdapter adapter;

    List<OrderBean> list;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new OrderListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        statusLayout.showProgressView();
        ((OrderPresenter) mPresenter).getOrderListFirst(type);
        adapter.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((OrderPresenter) mPresenter).getOrderListMore();
            }
        });
        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OrderPresenter) mPresenter).getOrderListFirst(type);
                statusLayout.showProgressView();
            }
        });
        EventBus.getDefault().register(this);
    }

    //订单详情付款成功后，刷新订单列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderChanged(PaymentEvent event) {
        statusLayout.showProgressView();
        if (adapter != null) {
            ((OrderPresenter) mPresenter).getOrderListFirst(type);
            adapter.clear();
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new OrderPresenter();
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getOrderInfoSuccess(OrderResponseBean data) {
        list = data.content;
        if (list.size() == 0) {
            statusLayout.showEmptyView(getString(R.string.no_order));
            return;
        }
        statusLayout.showContentView();
        adapter.addAll(list);
        if (!data.haseMore) {
            adapter.showNoMore();
        }
    }

    @Override
    public void updateSuccess(OrderBean data) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showError(int res,String message) {
        statusLayout.showErrorView(getString(res));
    }
}
