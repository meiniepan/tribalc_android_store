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
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.model.GoodsModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.FrescoImageLoader;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;
import com.youth.banner.Banner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import retrofit2.Response;

import static com.gs.buluo.store.R.id.goods_create_next;

/**
 * Created by hjn on 2017/1/23.
 */
public class AddGoodsWithStandardActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
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
    private int pos = 0;
    private View llPrimay;
    private View llSecond;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initView();
        picList = new ArrayList<>();
        Intent intent = getIntent();
        meta = intent.getParcelableExtra(Constant.ForIntent.GOODS_BEAN);
        if (meta != null) {
            if (meta.standardId != null)
                getStandard();
            else
                setStandard(null);

            setData();
        } else {
            GoodsStandardMeta standardMeta = intent.getParcelableExtra(Constant.ForIntent.GOODS_STANDARD);
            meta = new GoodsMeta();
            meta.standardId = standardMeta.id;
            String category = intent.getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
            meta.category = GoodsCategory.valueOf(category);
            tvCategory.setText(GoodsCategory.valueOf(category).toString());
            setStandard(standardMeta.descriptions);
        }
    }

    private void initView() {
        findView(R.id.goods_create_add_pic).setOnClickListener(this);
        findView(R.id.goods_create_del_pic).setOnClickListener(this);
        findView(R.id.back).setOnClickListener(this);
        llPrimay = findView(R.id.ll_primary_standard);
        llSecond = findView(R.id.ll_second_standard);
        findView(goods_create_next).setOnClickListener(this);
        banner.setOnPageChangeListener(this);
        banner.setImageLoader(new FrescoImageLoader());
        banner.isAutoPlay(false);
        etTitleDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTitleWords.setText(s.length() + "");
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
                tvDescWords.setText(s.length() + "");
            }
        });
    }

    private void setData() {
        meta.isEdit = true;
        picList = meta.pictures;
        banner.setImages(meta.pictures);
        banner.start();
        etTitleDetail.setText(meta.title);
        etTitle.setText(meta.name);
        etDesc.setText(meta.note);
        etBrand.setText(meta.brand);
        tvCategory.setText(meta.category.toString());
        etSource.setText(meta.originCountry);
        etSale.setText(meta.priceAndRepertory.salePrice + "");
        etOrigin.setText(meta.priceAndRepertory.originPrice + "");
        etStock.setText(meta.priceAndRepertory.repertory + "");
        if (meta.standardKeys != null) {
            tvValue1.setText(meta.standardKeys.get(0));
            if (meta.standardKeys.size() > 1) tvValue2.setText(meta.standardKeys.get(1));
        }
    }

    private void setStandard(GoodsStandardDescriptions descriptions) {
        if (descriptions == null) {        //没规格
            llPrimay.setVisibility(View.GONE);
            llSecond.setVisibility(View.GONE);
        } else {
            this.descriptions = descriptions;
            tvKey1.setText(descriptions.primary.label);
            if (descriptions.secondary != null)
                tvKey2.setText(descriptions.secondary.label);
            else
                llSecond.setVisibility(View.GONE);  //一个规格
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_good_create_with_standard;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_create_next:
                if (etSale.length() == 0 || etStock.length() == 0 ) {
                    ToastUtils.ToastMessage(getCtx(), getString(R.string.goods_info_not_complete));
                    return;
                }
                meta.name = etTitle.getText().toString().trim();
                meta.title = etTitleDetail.getText().toString().trim();
                meta.pictures = picList;
                if (picList != null && picList.size() != 0) meta.mainPicture = picList.get(0);
                meta.brand = etBrand.getText().toString().trim();
                meta.originCountry = etSource.getText().toString().trim();
                meta.note = etDesc.getText().toString().trim();
                if (descriptions != null) {
                    meta.standardKeys = new ArrayList<>();
                    meta.standardKeys.add(tvValue1.getText().toString().trim());
                    meta.standardKeys.add(tvValue2.getText().toString().trim());
                }
                meta.priceAndRepertory = new GoodsPriceAndRepertory();
                meta.priceAndRepertory.originPrice = Float.parseFloat(etOrigin.getText().toString().trim());
                meta.priceAndRepertory.salePrice = Float.parseFloat(etSale.getText().toString().trim());
                meta.priceAndRepertory.repertory = Integer.parseInt(etStock.getText().toString().trim());

                Intent intent = new Intent(this, CreateGoodsFinalActivity.class);
                intent.putExtra(Constant.ForIntent.META, meta);
                startActivity(intent);
                break;
            case R.id.goods_create_add_pic:
                showChoosePhoto();
                break;
            case R.id.goods_create_del_pic:
                if (picList.size() == 0 || pos > picList.size()) return;
                if (picList.size()==1){
                    picList.remove(0);
                    banner.update(picList);
                    banner.setVisibility(View.GONE);
                    return;
                }
                picList.remove(pos-1);
                banner.update(picList);
                pos = picList.size()-1;
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void uploadPic(String path) {
        TribeUploader.getInstance().uploadFile("goods", "", new File(path), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                banner.setVisibility(View.VISIBLE);
                picList.add(data.objectKey);
                Collections.reverse(picList);
                banner.setImages(picList);
                banner.start();
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(getCtx(), R.string.upload_fail);
            }
        });
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position>picList.size())return;
        pos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void getStandard() {
        new GoodsModel().getGoodsStandard(meta.standardId, new TribeCallback<GoodsStandard>() {
            @Override
            public void onSuccess(Response<BaseResponse<GoodsStandard>> response) {
                setStandard(response.body().data.descriptions);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<GoodsStandard> body) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
            }
        });

    }
}
