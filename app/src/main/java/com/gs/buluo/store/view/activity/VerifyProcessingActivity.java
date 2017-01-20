package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/20.
 */
public class VerifyProcessingActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify_processing;
    }
}
