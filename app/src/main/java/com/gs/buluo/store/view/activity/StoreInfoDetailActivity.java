package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.PictureListAdapter;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/19.
 */
public class StoreInfoDetailActivity extends BaseActivity  {
    @BindView(R.id.info_store_name)
    TextView tvName;
    @BindView(R.id.info_store_category)
    TextView tvCategory;
    @BindView(R.id.info_store_fee)
    TextView tvFee;
    @BindView(R.id.info_store_phone)
    TextView tvPhone;
    @BindView(R.id.info_store_address)
    TextView tvAddress;
    @BindView(R.id.store_area)
    TextView etArea;
    @BindView(R.id.info_store_time)
    TextView tvTime;
    @BindView(R.id.info_store_logo)
    ImageView tvLogo;
    @BindView(R.id.info_store_introduction)
    TextView etDesc;
    @BindView(R.id.store_tags)
    TextView tvTags;
    @BindView(R.id.info_store_email)
    TextView tvEmail;
    @BindView(R.id.store_pictures)
    RecyclerView recyclerView;
    private PictureListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PictureListAdapter(this);
        recyclerView.setAdapter(adapter);
        initData();
    }

    public void setData(StoreMeta data) {
        etDesc.setText(data.desc);
        tvName.setText(data.name);
        tvCategory.setText(data.category);
        tvPhone.setText(data.serviceLine);
        etArea.setText(data.province + data.city + data.district);
        tvAddress.setText(data.address);
        tvTime.setText(data.businessHours);
        tvFee.setText(data.avgprice);
        if (data.sellingPoint!=null){
            StringBuilder tags = new StringBuilder();
            for (String s : data.sellingPoint) {
                tags.append(s).append(",");
            }
            tvTags.setText(tags.toString().length() > 0 ? tags.toString().substring(0, tags.length() - 1) : "");
        }

        tvEmail.setText(data.email);
        GlideUtils.loadImage(getCtx(), data.getLogo(), tvLogo, true);
        adapter.addAll(data.pictures);
    }

    private void initData() {
        showLoadingDialog();
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MainApis.class).getStoreDetailInfo(id, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreMeta>>() {
                    @Override
                    public void onNext(BaseResponse<StoreMeta> response) {
                        setData(response.data);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_info;
    }
}
