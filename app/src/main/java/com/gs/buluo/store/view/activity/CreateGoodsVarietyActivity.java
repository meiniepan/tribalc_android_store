package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsCategory;

/**
 * Created by hjn on 2017/1/11.
 */
public class CreateGoodsVarietyActivity extends BaseActivity implements View.OnClickListener {
    private GoodsCategory goodsCategory;

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
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_create;
    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent(this, GoodsStandardActivity.class);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.store_food:
                goodsCategory = GoodsCategory.FOOD;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_gift:
                goodsCategory= GoodsCategory.GIFT;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_life:
                goodsCategory= GoodsCategory.LIVING;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_furniture:
                goodsCategory= GoodsCategory.HOUSE;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_office:
                goodsCategory= GoodsCategory.OFFICE;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_dress:
                goodsCategory=GoodsCategory.MAKEUP;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
            case R.id.store_baby:
                goodsCategory= GoodsCategory.PENETRATION;
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY, goodsCategory.name());
                startActivity(intent);
                break;
        }
    }
}
