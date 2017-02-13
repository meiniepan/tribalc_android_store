package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BannerPicture;
import com.gs.buluo.store.bean.GoodsCategory;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideBannerLoader;
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
    @Bind(R.id.goods_create_title_detail)
    EditText etTitleDetail;
    @Bind(R.id.goods_create_title)
    EditText etTitle;
    @Bind(R.id.goods_create_desc)
    EditText etDesc;
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
    @Bind(R.id.goods_create_main)
    CheckBox checkBox;

    ArrayList<BannerPicture> picList = new ArrayList<>();
    private int pos = 0;

    GoodsMeta meta;
    private GoodsStandardMeta standardMeta;
    private Bundle bundle;
    private View firstAddPic;
    private View delPic;
    private View addPic;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initView();
        String category = getIntent().getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
        meta = new GoodsMeta();
        meta.category = GoodsCategory.valueOf(category);
        tvCategory.setText(meta.category.toString());
    }

    private void initView() {
        banner.setImageLoader(new GlideBannerLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setOnPageChangeListener(this);
        banner.isAutoPlay(false);

        delPic = findViewById(R.id.goods_create_del_pic);
        delPic.setOnClickListener(this);
        addPic = findViewById(R.id.goods_create_add_pic);
        addPic.setOnClickListener(this);
        findViewById(R.id.goods_create_next).setOnClickListener(this);
        firstAddPic = findViewById(R.id.goods_create_add_first);
        firstAddPic.setOnClickListener(this);
        findViewById(R.id.ll_goods_create_standard).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        etTitleDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 60) {
                    etTitleDetail.setText(s.toString().substring(0, 60));
                    etTitleDetail.setSelection(60);
                    tvTitleWords.setText(60 + "");
                } else {
                    tvTitleWords.setText(s.length() + "");
                }
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
                if (s.length() > 20) {
                    etDesc.setText(s.toString().substring(0, 20));
                    etDesc.setSelection(20);
                    tvDescWords.setText(20 + "");
                } else {
                    tvDescWords.setText(s.length() + "");
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    picList.get(pos).isChecked = true;
                    for (BannerPicture picture : picList) {
                        if (picList.indexOf(picture) != pos) {
                            picture.isChecked = false;
                        }
                    }
                }
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
            case R.id.goods_create_add_first:
                showChoosePhoto();
                break;
            case R.id.goods_create_del_pic:
                if (picList.size() == 0 || pos > picList.size()) return;
                if (picList.size() == 1) {
                    picList.remove(0);
                    banner.update(picList);
                    banner.setVisibility(View.GONE);
                    firstAddPic.setVisibility(View.VISIBLE);
                    addPic.setVisibility(View.GONE);
                    delPic.setVisibility(View.GONE);
                    return;
                }
                picList.remove(pos);
                banner.setImages(picList);
                banner.start();
                pos = picList.size() - 1;
                break;
            case R.id.ll_goods_create_standard:
                Intent intent = new Intent(getCtx(), CreateStandardActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, 201);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.goods_create_next:
                goNext();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 201 && resultCode == RESULT_OK) {
            bundle = data.getExtras();
            SerializableHashMap<String, GoodsPriceAndRepertory> serMap = (SerializableHashMap<String, GoodsPriceAndRepertory>) bundle.getSerializable("map");
            if (serMap != null) {
                standardMeta = bundle.getParcelable("data");
                standardMeta.priceAndRepertoryMap = serMap.getMap();
                tvStandard.setText(standardMeta.title);
               findView(R.id.goods_price_area).setVisibility(View.GONE);
            }
        }
    }

    private void goNext() {
        meta.name = etTitle.getText().toString().trim();
        meta.title = etTitleDetail.getText().toString().trim();
        meta.pictures = new ArrayList<>();
        for (BannerPicture pic : picList) {
            if (pic.isChecked) {
                meta.mainPicture = pic.url;
            }
            meta.pictures.add(pic.url);
        }
        if (meta.mainPicture == null && picList.size() > 0)
            meta.mainPicture = picList.get(0).toString();

        meta.brand = etBrand.getText().toString().trim();
        meta.note = etDesc.getText().toString().trim();
        meta.originCountry = etSource.getText().toString().trim();
        if (standardMeta == null) {
            GoodsPriceAndRepertory goods = new GoodsPriceAndRepertory();
            if (etSale.length() == 0 || etStock.length() == 0) {
                ToastUtils.ToastMessage(this, R.string.goods_info_not_complete);
                return;
            }
            goods.originPrice = Float.parseFloat(etOrigin.getText().toString().trim());
            goods.salePrice = Float.parseFloat(etSale.getText().toString().trim());
            goods.repertory = Integer.parseInt(etStock.getText().toString().trim());
            meta.priceAndRepertory = goods;
        }

        Intent intent = new Intent(this, CreateGoodsFinalActivity.class);
        intent.putExtra(Constant.ForIntent.META, meta);
        if (bundle != null) {
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
                banner.setVisibility(View.VISIBLE);
                picList.add(0, new BannerPicture(data.objectKey));
                banner.setImages(picList);
                banner.start();
                pos = 0;
                if (picList.size() == 1) {
                    firstAddPic.setVisibility(View.GONE);
                    addPic.setVisibility(View.VISIBLE);
                    delPic.setVisibility(View.VISIBLE);
                }
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
        if (position > picList.size() || position == 0) return;
        this.pos = position - 1;
        checkBox.setChecked(picList.get(position - 1).isChecked);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
