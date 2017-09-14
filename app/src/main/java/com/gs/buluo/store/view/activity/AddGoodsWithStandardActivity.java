package com.gs.buluo.store.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BannerPicture;
import com.gs.buluo.store.bean.GoodsCategory;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideBannerLoader;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.gs.buluo.store.R.id.goods_create_next;
import static com.gs.buluo.store.R.id.ll_standard;

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
    @Bind(R.id.goods_create_profit)
    EditText etProfit;
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
    @Bind(R.id.goods_create_main)
    CheckBox checkBox;
    @Bind(R.id.choose_item_check)
    RadioButton mainButton;

    GoodsMeta meta;
    List<BannerPicture> picList;
    private GoodsStandardDescriptions descriptions;
    private int pos = 0;
    private View llSecond;

    private View firstAddPic;
    private View addPic;
    private View delPic;
    private View addFirst;
    private boolean isMain;
    private GoodsStandardMeta standardMeta;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        picList = new ArrayList<>();
        initView();
        initData();
        if (meta.pictures == null || meta.pictures.size() == 0) {
            addFirst.setVisibility(View.VISIBLE);
            addPic.setVisibility(View.GONE);
            delPic.setVisibility(View.GONE);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        meta = intent.getParcelableExtra(Constant.ForIntent.GOODS_BEAN);
        if (meta != null) {                 //编辑商品
            if (meta.standardId != null)
                getStandard();
            else
                setStandard(null);

            setData();
        } else {            //添加有规格商品
            standardMeta = intent.getParcelableExtra(Constant.ForIntent.GOODS_STANDARD);
            meta = new GoodsMeta();
            meta.pictures = new ArrayList<>();
            meta.standardId = standardMeta.id;
            String category = intent.getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
            meta.category = GoodsCategory.valueOf(category);
            tvCategory.setText(GoodsCategory.valueOf(category).toString());
            setStandard(standardMeta.descriptions);
        }
    }

    private void initView() {
        addFirst = findView(R.id.goods_create_add_first);
        addFirst.setOnClickListener(this);
        addPic = findView(R.id.goods_create_add_pic);
        addPic.setOnClickListener(this);
        delPic = findView(R.id.goods_create_del_pic);
        delPic.setOnClickListener(this);
        llSecond = findView(R.id.ll_second_standard);
        findView(goods_create_next).setOnClickListener(this);
        firstAddPic = findViewById(R.id.goods_create_add_first);
        firstAddPic.setOnClickListener(this);
        findView(R.id.rl_goods_main).setOnClickListener(this);
        banner.setOnPageChangeListener(this);
        banner.setImageLoader(new GlideBannerLoader());
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

    private void setData() {
        meta.isEdit = true;
        if (meta.pictures != null && meta.pictures.size() > 0) {
            setBannerStyle();
        } else {
            meta.pictures = new ArrayList<>();
        }

        etTitleDetail.setText(meta.title);
        etTitle.setText(meta.name);
        etDesc.setText(meta.note);
        etBrand.setText(meta.brand);
        tvCategory.setText(meta.category.toString());
        etSource.setText(meta.originCountry);
        etSale.setText((meta.priceAndRepertory.salePrice * 100 - meta.priceAndRepertory.pfProfit * 100) / 100 + "");
        etOrigin.setText(meta.priceAndRepertory.originPrice + "");
        etStock.setText(meta.priceAndRepertory.repertory + "");
        etProfit.setText(meta.priceAndRepertory.pfProfit + "");
        if (meta.standardKeys != null) {
            tvValue1.setText(meta.standardKeys.get(0));
            if (meta.standardKeys.size() > 1) tvValue2.setText(meta.standardKeys.get(1));
        }

        mainButton.setChecked(meta.primary);
        isMain = meta.primary;
    }

    private void setBannerStyle() {
        for (String url : meta.pictures) {
            BannerPicture bannerPicture = new BannerPicture(url);
            picList.add(bannerPicture);
            if (TextUtils.equals(url, meta.mainPicture)) {
                bannerPicture.isChecked = true;
            }
        }
        banner.setImages(meta.pictures);
        banner.start();

        firstAddPic.setVisibility(View.GONE);
        addPic.setVisibility(View.VISIBLE);
        delPic.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
    }

    private void setStandard(GoodsStandardDescriptions descriptions) {
        if (descriptions == null) {        //没规格
            findView(R.id.ll_standard).setVisibility(View.GONE);
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
                continueToFinal();
                break;
            case R.id.goods_create_add_pic:
                showChoosePhoto();
                break;
            case R.id.goods_create_add_first:
                showChoosePhoto();
                break;
            case R.id.rl_goods_main:
                if (isMain) {
                    mainButton.setChecked(false);
                    isMain = false;
                } else {
                    mainButton.setChecked(true);
                    isMain = true;
                }
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
                    checkBox.setVisibility(View.GONE);
                    return;
                }
                picList.remove(pos);
                banner.setImages(picList);
                banner.start();
                pos = picList.size() - 1;
                break;
        }
    }

    private void continueToFinal() {
        if (etSale.getText().length() == 0 || etStock.getText().length() == 0 || etProfit.length() == 0) {         //编辑商品
            ToastUtils.ToastMessage(getCtx(), getString(R.string.goods_info_not_complete));
            return;
        }
        if (findView(ll_standard).getVisibility() == View.VISIBLE && tvValue1.getText().length() == 0) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.standard_not_empty));
            return;
        } else if (llSecond.getVisibility() == View.VISIBLE && tvValue2.getText().length() == 0) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.standard_not_empty));
            return;
        }
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

        meta.primary = isMain;
        meta.brand = etBrand.getText().toString().trim();
        meta.originCountry = etSource.getText().toString().trim();
        meta.note = etDesc.getText().toString().trim();
        if (descriptions != null) {
            meta.standardKeys = new ArrayList<>();
            meta.standardKeys.add(tvValue1.getText().toString().trim());
            if (!TextUtils.isEmpty(tvValue2.getText().toString().trim()))
                meta.standardKeys.add(tvValue2.getText().toString().trim());
        }
        meta.priceAndRepertory = new GoodsPriceAndRepertory();
        meta.priceAndRepertory.originPrice = Float.parseFloat(etOrigin.getText().toString().trim());
        meta.priceAndRepertory.pfProfit = Float.parseFloat(etProfit.getText().toString().trim());
        float nunSale = Float.parseFloat(etSale.getText().toString().trim());
        int numStock = Integer.parseInt(etStock.getText().toString().trim());
        if (nunSale < 0) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.sale_price_legal));
            return;
        }
        if (numStock <= 0) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.stock_price_legal));
            return;
        }
        meta.priceAndRepertory.salePrice = nunSale;
        meta.priceAndRepertory.repertory = numStock;

        Intent intent = new Intent(this, CreateGoodsFinalActivity.class);
        intent.putExtra(Constant.ForIntent.META, meta);
        startActivity(intent);
    }

    private void uploadPic(String path) {
        showLoadingDialog();
        TribeUploader.getInstance().uploadFile("goods", "", path, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadResponseBody data) {
                banner.setVisibility(View.VISIBLE);
                picList.add(0, new BannerPicture(data.objectKey));
                banner.setImages(picList);
                banner.start();
                if (picList.size() == 1) {
                    addFirst.setVisibility(View.GONE);
                    addPic.setVisibility(View.VISIBLE);
                    delPic.setVisibility(View.VISIBLE);
                    checkBox.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void uploadFail() {
                dismissDialog();
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
        if (position > picList.size() || position == 0) return;
        pos = position - 1;
        checkBox.setChecked(picList.get(position - 1).isChecked);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void getStandard() {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getGoodsStandard(meta.standardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodsStandard>>() {
                    @Override
                    public void onNext(BaseResponse<GoodsStandard> goodListBaseResponse) {
                        setStandard(goodListBaseResponse.data.descriptions);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (LoadingDialog.getInstance().isShowing()) {
            LoadingDialog.getInstance().dismissDialog();
        } else {
            CustomAlertDialog dialog = new CustomAlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("确认要放弃创建或修改商品么?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }
}
