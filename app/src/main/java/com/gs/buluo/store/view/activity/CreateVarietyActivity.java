package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/10.
 */
public class CreateVarietyActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.store_food).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(CreateVarietyActivity.this,CreateDetailActivity.class);
        switch (v.getId()){
            case R.id.store_food:
                break;
        }
    }
}
