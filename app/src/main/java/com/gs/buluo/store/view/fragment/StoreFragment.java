package com.gs.buluo.store.view.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.CommunityListAdapter;
import com.gs.buluo.store.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.store.model.CommunityModel;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2016/11/1.
 */
public class StoreFragment extends BaseFragment implements Callback<CommunityResponse> {
    @Bind(R.id.community_list)
    RefreshRecyclerView recyclerView;
    @Bind(R.id.store_floating)
    FloatingActionButton actionButton;
    private CommunityListAdapter adapter;
    private CommunityModel model;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setUserVisibleHint(false);
        model = new CommunityModel();
        model.getCommunitiesList(this);
        adapter = new CommunityListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        recyclerView.setNeedLoadMore(false);
        recyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                loadData();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.ToastMessage(getActivity(),"click");
            }
        });
    }

    private void loadData() {
        model.getCommunitiesList(this);
    }

    @Override
    public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
        recyclerView.dismissSwipeRefresh();
        if (response.body()!=null&&response.body().code==200){
            if (mContext==null)return;
            adapter.clear();
            adapter.addAll(response.body().data);
        }
    }

    @Override
    public void onFailure(Call<CommunityResponse> call, Throwable t) {
        ToastUtils.ToastMessage(getActivity(),R.string.connect_fail);
    }
}
