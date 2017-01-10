package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/29.
 */
public class CommunityVisitActivity extends BaseActivity {
    @Bind(R.id.community_visit_name)
    EditText tvName;
    @Bind(R.id.community_visit_time)
    EditText tvTime;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvName.setText( getIntent().getStringExtra(Constant.COMMUNITY_NAME));
        tvTime.requestFocus();
        findViewById(R.id.community_visit_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.community_visit_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_community_visit;
    }
}
