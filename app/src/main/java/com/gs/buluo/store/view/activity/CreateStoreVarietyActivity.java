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
public class CreateStoreVarietyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.store_food).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create;
    }

    @Override
    public void onClick(View v) {
        CreateStoreBean bean = new CreateStoreBean();
        Intent intent = new Intent(CreateStoreVarietyActivity.this, CreateDetailActivity.class);
        switch (v.getId()) {
            case R.id.store_food:
                bean.category = CreateStoreBean.StoreCategory.FOOD;
                intent.putExtra(Constant.ForIntent.STORE_BEAN, bean);
                startActivity(intent);
                break;
            case R.id.store_gift:
                bean.category = CreateStoreBean.StoreCategory.GIFT;
                intent.putExtra(Constant.ForIntent.STORE_BEAN, bean);
                startActivity(intent);
                break;
            case R.id.store_office:
                bean.category = CreateStoreBean.StoreCategory.OFFICE;
                intent.putExtra(Constant.ForIntent.STORE_BEAN, bean);
                startActivity(intent);
                break;
        }
    }
}
