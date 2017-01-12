package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/11.
 */
public class CreateGoodsVarietyActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_create;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
