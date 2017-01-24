package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsCategory;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.utils.ToastUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.gs.buluo.store.R.id.goods_create_next;

/**
 * Created by hjn on 2017/1/23.
 */
public class AddGoodsWithStandardActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.goods_create_banner)
    Banner banner;
    @Bind(R.id.goods_create_title)
    EditText etTitle;
    @Bind(R.id.goods_create_desc)
    EditText etDesc;
    @Bind(R.id.goods_create_title_detail)
    EditText etTitleDetail;
    @Bind(R.id.goods_create_origin)
    EditText etOrigin;
    @Bind(R.id.goods_create_sale)
    EditText etSale;
    @Bind(R.id.goods_create_stock)
    EditText etStock;
    @Bind(R.id.create_goods_brand)
    EditText etBrand;
    @Bind(R.id.goods_create_source)
    EditText etSource;

    @Bind(R.id.goods_create_category)
    TextView tvCategory;
    @Bind(R.id.goods_create_title_left)
    TextView tvTitleWords;
    @Bind(R.id.goods_create_short_left)
    TextView tvDescWords;
    @Bind(R.id.goods_standard_key1)
    TextView tvKey1;
    @Bind(R.id.goods_standard_key2)
    TextView tvKey2;
    @Bind(R.id.goods_standard_value1)
    EditText tvValue1;
    @Bind(R.id.goods_standard_value2)
    EditText tvValue2;

    GoodsMeta meta;
    List<String> picList;
    private GoodsStandardDescriptions descriptions;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findView(R.id.goods_create_add_pic).setOnClickListener(this);
        findView(R.id.goods_create_del_pic).setOnClickListener(this);
        findView(goods_create_next).setOnClickListener(this);
        Intent intent = getIntent();
        GoodsStandardMeta standardMeta = intent.getParcelableExtra(Constant.ForIntent.GOODS_STANDARD);
        meta.standardId = standardMeta.id;
        String category = intent.getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
        tvCategory.setText(GoodsCategory.valueOf(category).toString());
        descriptions = standardMeta.descriptions;
        tvKey1.setText(descriptions.primary.label);

        if (descriptions.secondary != null)
            tvKey2.setText(descriptions.secondary.label);
        else
            findView(R.id.ll_second_standard).setVisibility(View.GONE);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_good_create_with_standard;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_create_next:
                if (etSale.length()==0||etStock.length()==0){
                    ToastUtils.ToastMessage(getCtx(),getString(R.string.goods_info_not_complete));
                    return;
                }
                meta.name =etTitle.getText().toString().trim();
                meta.title = etTitleDetail.getText().toString().trim();
                meta.pictures = picList;
                if (picList!=null&&picList.size()!=0) meta.mainPicture = picList.get(0);
                meta.brand =etBrand.getText().toString().trim();
                meta.originCounty = etSource.getText().toString().trim();
                meta.standardKeys =new ArrayList<>();
                meta.standardKeys.add(descriptions.primary.label);
                meta.standardKeys.add(descriptions.secondary.label);

                meta.priceAndRepertory  = new GoodsPriceAndRepertory();
                meta.priceAndRepertory.orginPrice = Float.parseFloat(etOrigin.getText().toString().trim());
                meta.priceAndRepertory.salePrice = Float.parseFloat(etSale.getText().toString().trim());
                meta.priceAndRepertory.repertory = Integer.parseInt(etStock.getText().toString().trim());

                Intent intent =new Intent(this,CreateGoodsFinalActivity.class);
                intent.putExtra(Constant.ForIntent.META,meta);
                startActivity(intent);
                break;
            case R.id.goods_create_add_pic:
                break;
            case R.id.goods_create_del_pic:
                break;
        }
    }
}
