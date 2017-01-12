package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.FacilityAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/10.
 */
public class StoreSettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.store_setting_facility)
    RecyclerView recyclerView;

    Context mCtx;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.store_setting_head_area).setOnClickListener(this);
        initFacility();
    }

    private void initFacility() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.string.wi_fi);
        list.add(R.string.bar);
        list.add(R.string.business_circle);
        list.add(R.string.business_dinner);
        list.add(R.string.facilities_for_disabled);
        list.add(R.string.organic_food);
        list.add(R.string.parking);
        list.add(R.string.pet_ok);
        list.add(R.string.room);
        list.add(R.string.restaurants_of_hotel);
        list.add(R.string.scene_seat);
        list.add(R.string.small_party);
        list.add(R.string.subway);
        list.add(R.string.valet_parking);
        list.add(R.string.vip_rights);
        list.add(R.string.weekend_brunch);

        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(new FacilityAdapter(this, list));
        recyclerView.stopScroll();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_setting;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.store_setting_head_area:
                intent.setClass(mCtx, PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivity(intent);
                break;
        }
    }
}
