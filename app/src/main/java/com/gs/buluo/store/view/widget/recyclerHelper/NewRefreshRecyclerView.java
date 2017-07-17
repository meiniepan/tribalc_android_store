package com.gs.buluo.store.view.widget.recyclerHelper;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.R;


/**
 * Created by hjn on 2017/7/10.
 */

public class NewRefreshRecyclerView extends FrameLayout {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private View emptyView;
    private TextView emptyText;
    private ImageView emptyIcon;
    private View errorView;
    private TextView errorText;
    private ImageView errorIcon;

    private View progressView;

    public NewRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public NewRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = inflate(context, R.layout.view_refresh_recycler, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.my_swipe);
        mSwipeRefreshLayout.setEnabled(false);

        emptyView = view.findViewById(R.id.empty_view);
        emptyText = (TextView) view.findViewById(R.id.empty_view_text);
        emptyIcon = (ImageView) view.findViewById(R.id.empty_icon);
        errorView = view.findViewById(R.id.error_view);
        errorText = (TextView) view.findViewById(R.id.error_view_text);
        errorIcon = (ImageView) view.findViewById(R.id.error_view_icon);

        progressView = view.findViewById(R.id.progress_view);

        setSwipeRefreshColorsFromRes(R.color.main_tab, R.color.custom_color, R.color.custom_color_shallow);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setRefreshAction(final OnRefreshListener action) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action.onAction();
            }
        });
    }

    //刷新完成先调
    public void setRefreshFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.clearData();
    }

    public void showEmpty(int s) {
        progressView.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        emptyText.setText(s);
    }

    public void showEmpty(int s, int img) {
        progressView.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        emptyText.setText(s);
        emptyIcon.setImageResource(img);
    }

    public void showError(int s) {
        progressView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
        errorText.setText(s);
    }

    public void showError(int s, int img) {
        progressView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
        errorText.setText(s);
        errorIcon.setImageResource(img);
    }

    public void showProgress() {
        progressView.setVisibility(VISIBLE);
    }

    public void showContent() {
        progressView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }

}
