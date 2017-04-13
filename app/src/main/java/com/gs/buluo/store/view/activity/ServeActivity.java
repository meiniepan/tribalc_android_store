package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.ServeListAdapter;
import com.gs.buluo.store.bean.CoordinateBean;
import com.gs.buluo.store.bean.ListStoreSetMeal;
import com.gs.buluo.store.bean.ResponseBody.ServeResponseBody;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.ServePresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IServeView;
import com.gs.buluo.store.view.widget.SortBoard;
import com.gs.buluo.store.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class ServeActivity extends BaseActivity implements View.OnClickListener, IServeView, SortBoard.OnSelectListener {
    @Bind(R.id.serve_list)
    RefreshRecyclerView refreshView;

    @Bind(R.id.serve_sort_mark)
    ImageView sortMark;
    @Bind(R.id.serve_filter_mark)
    ImageView filterMark;

    @Bind(R.id.serve_filter_title)
    TextView tvFilter;
    @Bind(R.id.serve_sort_title)
    TextView tvSort;
    @Bind(R.id.serve_title)
    TextView tvTitle;

    private SortBoard sortBoard;
    private ServeListAdapter adapter;
    private String type;

    private String sort = Constant.SORT_POPULAR;
    private List<ListStoreSetMeal> data;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        type = getIntent().getStringExtra(Constant.TYPE);
        initView(type);
        ((ServePresenter) mPresenter).getServeListFirst(type.toUpperCase(), sort);

        refreshView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServeListAdapter(this);
        refreshView.setAdapter(adapter);
        refreshView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((ServePresenter) mPresenter).getServeMore(type.toUpperCase(), sort);
            }
        });
    }

    private void initView(String type) {
        if (TextUtils.equals(type, Constant.REPAST)) {
            tvTitle.setText(R.string.repast);
        } else {
            tvTitle.setText(R.string.entertainment);
        }
        findViewById(R.id.serve_map).setOnClickListener(this);
        findViewById(R.id.serve_sort).setOnClickListener(this);
        findViewById(R.id.serve_filter).setOnClickListener(this);
        findViewById(R.id.serve_back).setOnClickListener(this);

        sortBoard = new SortBoard(this, this);
        sortBoard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideTopSort();
                hideTopFilter();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ServePresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serve_map:
                Intent intent = new Intent(ServeActivity.this, MapActivity.class);
                ArrayList<CoordinateBean> posList = new ArrayList<>();
                for (ListStoreSetMeal meal :data){
                    if (meal.store!=null&&meal.store.coordinate!=null)
                        posList.add(new CoordinateBean(meal.store.coordinate.get(0),meal.store.coordinate.get(1),meal.store,meal.id));
                }
                intent.putParcelableArrayListExtra(Constant.ForIntent.COORDINATE,posList);
                startActivity(intent);
                break;
            case R.id.serve_sort:
                showSortBoard();
                break;
            case R.id.serve_filter:
                showFilterBoard();
                break;
            case R.id.serve_back:
                finish();
                break;
        }
    }

    private void showSortBoard() {
        sortBoard.setSortVisible();
        sortBoard.showAsDropDown(findViewById(R.id.serve_parent), 0, 0);
        sortMark.setImageResource(R.mipmap.up_colored);
        tvSort.setTextColor(getResources().getColor(R.color.custom_color));
        hideTopFilter();
    }

    private void showFilterBoard() {
        sortBoard.showAsDropDown(findViewById(R.id.serve_parent), 0, 0);
        sortBoard.setFilterVisible();
        filterMark.setImageResource(R.mipmap.up_colored);
        tvFilter.setTextColor(getResources().getColor(R.color.custom_color));
        hideTopSort();
    }

    private void hideTopFilter() {
        filterMark.setImageResource(R.mipmap.down);
        tvFilter.setTextColor(0x90000000);
    }

    public void hideTopSort() {
        sortMark.setImageResource(R.mipmap.down);
        tvSort.setTextColor(0x90000000);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    public void getServerSuccess(ServeResponseBody body) {
        data = body.content;
        adapter.addAll(data);
        if (!body.hasMore) {
            refreshView.showNoMore();
        }
    }

    @Override
    public void onSelected(String sort) {
        adapter.clear();
        ((ServePresenter) mPresenter).getServeListFirst(type.toUpperCase(), sort);
        this.sort = sort;
    }
}
