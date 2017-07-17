package com.gs.buluo.store.view.widget.recyclerHelper.loadmore;


import com.gs.buluo.store.R;

/**
 * Created by BlingBling on 2016/10/11.
 */

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_status_last;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.error_view;
    }

    @Override protected int getLoadEndViewId() {
        return R.id.no_more_view;
    }
}
