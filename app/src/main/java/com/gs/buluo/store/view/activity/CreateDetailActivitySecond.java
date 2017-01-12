package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/12.
 */
public class CreateDetailActivitySecond extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.create_next_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateDetailActivitySecond.this, QualificationActivity.class));
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_detail_second;
    }
}
