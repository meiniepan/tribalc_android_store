package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.BillListAdapter;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.ShoppingCarPresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.CreateGoodsVarietyActivity;
import com.gs.buluo.store.view.activity.CreateStoreVarietyActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import butterknife.Bind;

/**
 * Created by admin on 2016/11/1.
 */
public class StoreFragment extends BaseFragment {
    @Bind(R.id.store_list)
    RefreshRecyclerView recyclerView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (TribeApplication.getInstance().getUserInfo() != null) {

        } else {
            recyclerView.showNoData("请创建店铺");
        }

        getActivity().findViewById(R.id.store_floating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateGoodsVarietyActivity.class));
            }
        });
        getActivity().findViewById(R.id.store_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        recyclerView.setAdapter(adapter);

    }

    @Override
    protected BasePresenter getPresenter() {
        return new ShoppingCarPresenter();
    }
}
