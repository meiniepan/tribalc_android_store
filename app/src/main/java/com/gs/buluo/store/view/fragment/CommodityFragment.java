package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.ShoppingCarPresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.CreateGoodsVarietyActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import butterknife.Bind;

/**
 * Created by admin on 2016/11/1.
 */
public class CommodityFragment extends BaseFragment implements IOnSearchClickListener {
    @Bind(R.id.store_list)
    RefreshRecyclerView recyclerView;

    private Toolbar mToolbar;
    private SearchFragment searchFragment;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.order_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.order_find)
                    searchFragment.show(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
                return true;
            }
        });
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(this);

        if (TribeApplication.getInstance().getUserInfo() != null) {

        } else {
            recyclerView.showNoData("请先登录和创建店铺");
        }

        getActivity().findViewById(R.id.store_floating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateGoodsVarietyActivity.class));
            }
        });
//        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_menu, menu);
    }

    @Override
    public void OnSearchClick(String keyword) {
        ToastUtils.ToastMessage(getActivity(), keyword);
    }
}
