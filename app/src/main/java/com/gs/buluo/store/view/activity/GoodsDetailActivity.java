package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.GoodsDetailPresenter;
import com.gs.buluo.store.utils.FrescoImageLoader;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IGoodDetialView;
import com.gs.buluo.store.view.widget.panel.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, IGoodDetialView, GoodsChoosePanel.AddCartListener, GoodsChoosePanel.OnShowInDetail {
    private List<String> list;
    @Bind(R.id.goods_detail_pictures)
    Banner mBanner;
    @Bind(R.id.goods_detail_name)
    TextView tvName;
    @Bind(R.id.goods_detail_price)
    TextView tvPrice;
    @Bind(R.id.goods_detail_brand)
    TextView tvBrand;
    @Bind(R.id.goods_detail_phone)
    TextView tvPhone;
    @Bind(R.id.goods_detail_count)
    TextView tvCount;
    @Bind(R.id.good_brand_img)
    SimpleDraweeView brandImg;
    @Bind(R.id.goods_detail_standard)
    TextView tvStandard;

    @Bind(R.id.goods_detail_price_old)
    TextView tvPriceOld;
    @Bind(R.id.goods_detail_tip)
    TextView tvTip;
    @Bind(R.id.goods_detail_price_point)
    TextView tvPricePoint;

    Context context;
    private GoodsChoosePanel panel;
    private String id;
    private ListGoodsDetail goodsEntity;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        context = this;
        id = getIntent().getStringExtra(Constant.GOODS_ID);

        ((GoodsDetailPresenter) mPresenter).getGoodsDetaii(id);
        showLoadingDialog();

        findViewById(R.id.goods_detail_back).setOnClickListener(this);
        findViewById(R.id.goods_detail_choose).setOnClickListener(this);
        panel = new GoodsChoosePanel(this, this);
        panel.setAddCartListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_detail_back:
                finish();
                break;
            case R.id.goods_detail_choose:
                if (!checkUser(context)) return;
                if (panel != null) {
                    panel.show();
                }
                break;
        }
    }

    public void setGoodsPrice(String goodsPrice) {
        String[] array = goodsPrice.split("\\.");

        if (array.length > 1) {
            tvPrice.setText("￥" + array[0] + ".");
            tvPricePoint.setText(array[1]);
        } else {
            tvPrice.setText("￥" + goodsPrice);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsDetailPresenter();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void getDetailSuccess(ListGoodsDetail goodsEntity) {
        dismissDialog();
        this.goodsEntity = goodsEntity;
        panel.setRepertory(goodsEntity);
        if (this.goodsEntity.standardId != null) {
            ((GoodsDetailPresenter) mPresenter).getGoodsStandard(goodsEntity.standardId);
        } else {         //商品无规格信息，使用默认商品信息
            panel.setData(null);
        }

        list = new ArrayList<>();
        list = goodsEntity.pictures;
        FresoUtils.loadImage(goodsEntity.tMarkStore.logo, brandImg);
        tvName.setText(goodsEntity.title);
        setGoodsPrice(goodsEntity.salePrice);
        tvBrand.setText(goodsEntity.tMarkStore.name);
        tvCount.setText(goodsEntity.saleQuantity);
        tvPriceOld.setText("¥" + goodsEntity.originPrice);
        StringBuffer tag = new StringBuffer();
        for (String s : goodsEntity.tags) {
            tag.append(s).append("/");
        }
        tvTip.setText(tag.toString().substring(0, tag.length() - 1));

        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.isAutoPlay(false);
        mBanner.setImages(list);
        mBanner.start();
    }

    @Override
    public void getStandardSuccess(GoodsStandard standard) {
        panel.setData(standard);
    }

    @Override
    public void addSuccess() {
        ToastUtils.ToastMessage(this, R.string.add_success);
        panel.dismiss();
    }

    public void addToShoppingCart(String id, int num) {
        ((GoodsDetailPresenter) mPresenter).addCartItem(id, num);
    }

    @Override
    public void onAddCart(String id, int nowNum) {
        addToShoppingCart(id, nowNum);
    }

    @Override
    public void onShow(String standard, int num) {
        if (standard != null) {
            tvStandard.setText(standard + "        " + num + "件");
        } else {
            tvStandard.setText(num + "件");
        }
        tvStandard.setTextColor(0xff9a9a9a);
    }
}
