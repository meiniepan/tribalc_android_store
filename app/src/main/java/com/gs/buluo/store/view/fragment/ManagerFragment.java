package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

import com.gs.buluo.store.view.activity.OrderActivity;
import com.gs.buluo.store.view.activity.ReserveActivity;

/**
 * Created by admin on 2016/11/1.
 */
public class ManagerFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_manager;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.usual_open_door).setOnClickListener(this);
        getActivity().findViewById(R.id.usual_property).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!checkUser(getActivity())) return;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.usual_property:
                intent.setClass(getActivity(), ReserveActivity.class);
                startActivity(intent);
                break;
            case R.id.usual_open_door:
                intent.setClass(getActivity(), OrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
