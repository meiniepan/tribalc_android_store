package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.StoreMeta;

/**
 * Created by hjn on 2017/1/10.
 */
public class CreateStoreVarietyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.store_food).setOnClickListener(this);
        findViewById(R.id.store_gift).setOnClickListener(this);
        findViewById(R.id.store_office).setOnClickListener(this);
        findViewById(R.id.store_life).setOnClickListener(this);
        findViewById(R.id.store_furniture).setOnClickListener(this);
        findViewById(R.id.store_dress).setOnClickListener(this);
        findViewById(R.id.store_baby).setOnClickListener(this);
        findViewById(R.id.serve_repast).setOnClickListener(this);
        findViewById(R.id.serve_beauty).setOnClickListener(this);
        findViewById(R.id.serve_fitness).setOnClickListener(this);
        findViewById(R.id.serve_fun).setOnClickListener(this);
        findViewById(R.id.serve_regimen).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create;
    }

    @Override
    public void onClick(View v) {
        StoreMeta bean = new StoreMeta();
        Intent intent = new Intent(getCtx(), CreateDetailActivity.class);
        switch (v.getId()) {
            case R.id.store_food:
                bean.category = StoreMeta.StoreCategory.FOOD;
                bean.storeType = Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_gift:
                bean.category = StoreMeta.StoreCategory.GIFT;
                bean.storeType =Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_life:
                bean.category = StoreMeta.StoreCategory.LIVING;
                bean.storeType =Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_furniture:
                bean.category = StoreMeta.StoreCategory.HOUSE;
                bean.storeType = Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_office:
                bean.category = StoreMeta.StoreCategory.OFFICE;
                bean.storeType = Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_dress:
                bean.category = StoreMeta.StoreCategory.MAKEUP;
                bean.storeType = Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.store_baby:
                bean.category = StoreMeta.StoreCategory.PENETRATION;
                bean.storeType = Constant.GOODS;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.serve_repast:
                bean.category = StoreMeta.StoreCategory.REPAST;
                bean.storeType = Constant.SET_MEAL;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.serve_beauty:
                bean.category = StoreMeta.StoreCategory.HAIRDRESSING;
                bean.storeType =  Constant.SET_MEAL;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.serve_fitness:
                bean.category = StoreMeta.StoreCategory.FITNESS;
                bean.storeType =  Constant.SET_MEAL;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.serve_fun:
                bean.category = StoreMeta.StoreCategory.ENTERTAINMENT;
                bean.storeType =  Constant.SET_MEAL;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
            case R.id.serve_regimen:
                bean.category = StoreMeta.StoreCategory.KEEPHEALTHY;
                bean.storeType =  Constant.SET_MEAL;
                intent.putExtra(Constant.ForIntent.STORE_BEAN,bean);
                startActivity(intent);
                break;
        }
    }
}
