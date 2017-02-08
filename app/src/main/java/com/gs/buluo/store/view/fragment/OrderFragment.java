package com.gs.buluo.store.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.OrderListAdapter;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.eventbus.PaymentEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.OrderPresenter;
import com.gs.buluo.store.utils.ToastUtils;
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
        recyclerView.setNeedLoadMore(true);
        if (type == 0) {
            showLoadingDialog();
            ((OrderPresenter) mPresenter).getOrderListFirst(0);
        } else if (type == 1) {
            ((OrderPresenter) mPresenter).getOrderListFirst(1);
        } else if (type == 2) {
            showLoadingDialog();
            ((OrderPresenter) mPresenter).getOrderListFirst(2);
        } else if (type==3){
            ((OrderPresenter) mPresenter).getOrderListFirst(3);
        }   else {
            ((OrderPresenter) mPresenter).getOrderListFirst(4);
        }
        adapter.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((OrderPresenter) mPresenter).getOrderListMore();
            }
        });
        EventBus.getDefault().register(this);
    }

    //订单详情付款成功后，刷新订单列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderChanged(PaymentEvent event) {
        if (adapter != null) {
            if (type == 0) {
                showLoadingDialog();
                ((OrderPresenter) mPresenter).getOrderListFirst(0);
            } else if (type == 1) {
                ((OrderPresenter) mPresenter).getOrderListFirst(1);
            } else if (type == 2) {
                showLoadingDialog();
                ((OrderPresenter) mPresenter).getOrderListFirst(2);
            }  else if (type==3){
                ((OrderPresenter) mPresenter).getOrderListFirst(3);
            }   else {
                ((OrderPresenter) mPresenter).getOrderListFirst(4);
            }
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
    public void getOrderInfoSuccess(OrderResponse.OrderResponseBean data) {
        dismissDialog();
        list = data.content;
        if (list.size() == 0) {
            recyclerView.showNoData(R.string.no_order);
            return;
        }
        adapter.addAll(list);
        if (!data.haseMore) {
            adapter.showNoMore();
        }
    }

    @Override
    public void updateSuccess() {

    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(getActivity(), getString(res));
        dismissDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
