package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.CreateStoreBean;

/**
 * Created by hjn on 2017/1/10.
 */
public class CreateDetailActivity extends BaseActivity{
    CreateStoreBean storeBean;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        storeBean = getIntent().getParcelableExtra(Constant.ForIntent.STORE_BEAN);
        findViewById(R.id.create_next_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateDetailActivity.this,CreateDetailActivitySecond.class));
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
        return R.layout.activity_create_detail;
    }
}
