package com.gs.buluo.store.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.view.fragment.OrderFragment;

import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragmentAdapter extends FragmentStatePagerAdapter {
    List<String> list;
    Context mCtx;
    public OrderFragmentAdapter(Context context ,FragmentManager supportFragmentManager, List<String> list) {
        super(supportFragmentManager);
        mCtx = context;
        this.list = list;
    }


    @Override
    public Fragment getItem(int position) {
        OrderFragment fragment = new OrderFragment();
        fragment.setType(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(mCtx).inflate(R.layout.item_tab_layout, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(list.get(position));
        return tabView;
    }

}
