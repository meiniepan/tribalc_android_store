package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.GoodNewDetailAdapter;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.GoodsDetailPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideBannerLoader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IGoodDetialView;
import com.gs.buluo.store.view.widget.panel.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, IGoodDetialView, GoodsChoosePanel.OnShowInDetail {
    private List<String> list;
    @BindView(R.id.goods_detail_pictures)
    Banner mBanner;
    @BindView(R.id.goods_detail_name)
    TextView tvName;
    @BindView(R.id.goods_detail_price)
    TextView tvPrice;
    @BindView(R.id.goods_detail_brand)
    TextView tvBrand;
    @BindView(R.id.goods_detail_phone)
    TextView tvPhone;
    @BindView(R.id.goods_detail_count)
    TextView tvCount;
    @BindView(R.id.good_brand_img)
    ImageView brandImg;
    @BindView(R.id.goods_detail_standard)
    TextView tvStandard;

    @BindView(R.id.goods_detail_price_old)
    TextView tvPriceOld;
    @BindView(R.id.goods_detail_tip)
    TextView tvTip;
    @BindView(R.id.goods_detail_price_point)
    TextView tvPricePoint;
    @BindView(R.id.goods_detail_detail)
    ListView listView;

    Context context;
    private GoodsChoosePanel panel;
    private String id;
    private ListGoodsDetail goodsEntity;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        context = this;
        mBanner.setImageLoader(new GlideBannerLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.isAutoPlay(false);
        id = getIntent().getStringExtra(Constant.GOODS_ID);
        showLoadingDialog();
        ((GoodsDetailPresenter) mPresenter).getGoodsDetail(id);

        findViewById(R.id.goods_detail_back).setOnClickListener(this);
        findViewById(R.id.goods_detail_choose).setOnClickListener(this);
        panel = new GoodsChoosePanel(this, this);
        listView.setFocusable(false);
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
    public void getDetailSuccess(ListGoodsDetail goodsEntity) {
        dismissDialog();
        this.goodsEntity = goodsEntity;
        panel.setRepertory(goodsEntity);
        if (this.goodsEntity.standardId != null) {
            ((GoodsDetailPresenter) mPresenter).getGoodsStandard(goodsEntity.standardId);
        } else {         //商品无规格信息，使用默认商品信息
            panel.setData(null);
        }

        setData(goodsEntity);
    }

    private void setData(ListGoodsDetail goodsEntity) {
        list = new ArrayList<>();
        list = goodsEntity.pictures;
        tvName.setText(goodsEntity.title);
        setGoodsPrice(goodsEntity.salePrice);
        tvCount.setText(goodsEntity.saleQuantity);
        if (goodsEntity.detail !=null) listView.setAdapter(new  GoodNewDetailAdapter(getCtx(),goodsEntity.detail,true));
        CommonUtils.setListViewHeightBasedOnChildren(listView);
        if (goodsEntity.tMarkStore!=null){
            GlideUtils.loadImage(getCtx(),goodsEntity.tMarkStore.logo, brandImg);
            tvBrand.setText(goodsEntity.tMarkStore.name);
            tvPhone.setText(goodsEntity.tMarkStore.phone);
        }

        tvPriceOld.setText("¥" + (goodsEntity.originPrice==null?0:goodsEntity.originPrice));
        if (goodsEntity.tags!=null){
            StringBuffer tag = new StringBuffer();
            for (String s : goodsEntity.tags) {
                tag.append(s).append("/");
            }
            if (tag.length()>1)tvTip.setText(tag.toString().substring(0, tag.length() - 1));
        }

        mBanner.setImages(list);
        mBanner.start();
    }

    @Override
    public void getStandardSuccess(GoodsStandard standard) {
        panel.setData(standard);
        if (standard==null||standard.descriptions==null||standard.descriptions.primary==null)return;
        tvStandard.setText("请选择 "+standard.descriptions.primary.label+"  " +(standard.descriptions.secondary==null?"":standard.descriptions.secondary.label));
    }

    @Override
    public void addSuccess() {
        ToastUtils.ToastMessage(this, R.string.add_success);
        panel.dismiss();
    }


    @Override
    public void onShow(ListGoodsDetail goodsDetail, int num) {
        String standard = goodsDetail.standardSnapshot;
        if (standard != null) {
            String[] split = standard.split("\\|");
            if (split.length>1){
                tvStandard.setText("已选："+split[0].split(":")[1] +" " +split[1].split(":")[1] + "        " + num + "件");
            }else {
                tvStandard.setText(split[0].split(":")[1]+"        "+num+"件");
            }
        } else {
            tvStandard.setText(num + "件");
        }
        tvStandard.setTextColor(0xff9a9a9a);
        setData(goodsDetail);
    }

    @Override
    public void showError(int res, String message) {
        dismissDialog();
        ToastUtils.ToastMessage(this, message);
    }
}
