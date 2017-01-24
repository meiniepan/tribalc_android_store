package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsCategory;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.FrescoImageLoader;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/20.
 */
public class NewGoodsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
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
    @Bind(R.id.goods_create_standard)
    TextView tvStandard;

    ArrayList<String> picList = new ArrayList<>();
    private int pos = 0;

    GoodsMeta meta;
    private GoodsStandardMeta standardMeta;
    private Bundle bundle;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String category = getIntent().getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
        meta=new GoodsMeta();
        meta.category = GoodsCategory.valueOf(category);
        banner.setImageLoader(new FrescoImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setOnPageChangeListener(this);
        banner.isAutoPlay(false);
        tvCategory.setText(GoodsCategory.valueOf(category).toString());

        findViewById(R.id.goods_create_del_pic).setOnClickListener(this);
        findViewById(R.id.goods_create_add_pic).setOnClickListener(this);
        findViewById(R.id.goods_create_next).setOnClickListener(this);
        findViewById(R.id.ll_goods_create_standard).setOnClickListener(this);
        etTitleDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTitleWords.setText(s.length()+"");
            }
        });
        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvDescWords.setText(s.length()+"");
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_goods;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_create_add_pic:
                showChoosePhoto();
                break;
            case R.id.goods_create_del_pic:
                if (picList.size()==0)return;
                picList.remove(picList.get(pos));
                banner.setImages(picList);
                banner.setOffscreenPageLimit(pos-1);
                banner.start();
                pos -=1;
                break;
            case R.id.ll_goods_create_standard:
                startActivityForResult(new Intent(getCtx(),CreateStandardActivity.class),201);
                break;
            case R.id.back:
                finish();
            case R.id.goods_create_next:
                goNext();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null&&requestCode==201&&resultCode==RESULT_OK){
            bundle = data.getExtras();
            SerializableHashMap<String, GoodsPriceAndRepertory> serMap = (SerializableHashMap<String, GoodsPriceAndRepertory>) bundle.getSerializable("map");
            standardMeta = bundle.getParcelable("data");
            standardMeta.priceAndRepertoryMap = serMap.getMap();
            tvStandard.setText(standardMeta.title);
//            standardMeta = data.getParcelableExtra(Constant.STANDARD_INFO);
            findView(R.id.ll_create_goods_orgin).setVisibility(View.GONE);
            findView(R.id.ll_create_goods_sale).setVisibility(View.GONE);
            findView(R.id.ll_create_goods_repertory).setVisibility(View.GONE);
        }
    }

    private void goNext() {
        meta.category=GoodsCategory.FOOD;
        meta.name =etTitle.getText().toString().trim();
        meta.title = etTitleDetail.getText().toString().trim();
        meta.pictures = picList;
        if (picList!=null&&picList.size()!=0) meta.mainPicture = picList.get(0);
        meta.brand =etBrand.getText().toString().trim();
        meta.originCounty = etSource.getText().toString().trim();
        if (standardMeta==null){
            GoodsPriceAndRepertory goods = new GoodsPriceAndRepertory();
            if (etSale.length()==0||etStock.length()==0){
                ToastUtils.ToastMessage(this,"请完善信息填写");
                return;
            }
            goods.orginPrice = Float.parseFloat(etOrigin.getText().toString().trim());
            goods.salePrice = Float.parseFloat(etSale.getText().toString().trim());
            goods.repertory = Integer.parseInt(etStock.getText().toString().trim());
            meta.priceAndRepertory  = goods;
        }

        Intent intent =new Intent(this,CreateGoodsFinalActivity.class);
        intent.putExtra(Constant.ForIntent.META,meta);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void showChoosePhoto() {
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                uploadPic(string);
            }
        });
        panel.show();
    }

    private void uploadPic(String path) {
        TribeUploader.getInstance().uploadFile("goods", "", new File(path), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                picList.add(data.objectKey);
                banner.setImages(picList);
                banner.start();
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(getCtx(), R.string.upload_fail);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.pos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
