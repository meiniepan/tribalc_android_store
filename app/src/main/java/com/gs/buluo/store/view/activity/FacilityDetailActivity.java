package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.FacilityDetailAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/2/21.
 */
public class FacilityDetailActivity extends BaseActivity {
    @Bind(R.id.facility_detail_list)
    GridView gridView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<String> list = getIntent().getStringArrayListExtra(Constant.ForIntent.FANCILITY);
        gridView.setAdapter(new FacilityDetailAdapter(this,list));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_facility;
    }
}
