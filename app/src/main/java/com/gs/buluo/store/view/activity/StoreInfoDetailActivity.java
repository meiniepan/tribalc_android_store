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

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/19.
 */
public class StoreInfoDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.info_store_name)
    TextView tvName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
    @Bind(R.id.info_store_fee)
    TextView tvFee;
    @Bind(R.id.info_store_phone)
    TextView tvPhone;
    @Bind(R.id.info_store_address)
    TextView tvAddress;
    @Bind(R.id.store_area)
    TextView etArea;
    @Bind(R.id.info_store_time)
    TextView tvTime;
    @Bind(R.id.info_store_logo)
    ImageView tvLogo;
    @Bind(R.id.info_store_introduction)
    TextView etDesc;
    @Bind(R.id.store_pictures)
    RecyclerView recyclerView;
    private PictureListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.ll_store_info_address).setOnClickListener(this);
        findViewById(R.id.info_store_introduction).setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new PictureListAdapter(this);
        recyclerView.setAdapter(adapter);
        initData();
    }

    public void setData(StoreMeta data) {
        etDesc.setText(data.desc);
        tvName.setText(data.name);
        tvCategory.setText(data.category);
        tvPhone.setText(data.phone);
        etArea.setText(data.markPlace);
        tvTime.setText(data.businessHours);
        tvFee.setText(data.avgprice);
        GlideUtils.loadImage(getCtx(), data.getLogo(), tvLogo, true);
        tvAddress.setText(data.address == null ? "" : data.province + data.city + data.district + data.address);
        List<String> list =new ArrayList<>();
        list.add("oss://590974e50cf2e1e68db1a17d/1496978273891_photo");
        list.add("oss://59257f5e0cf27b75250fdd62/1495684147380_property0");
        list.add("oss://59257f5e0cf27b75250fdd62/1495684147444_property1");
        list.add("oss://59257f5e0cf27b75250fdd62/1495684146095_property2");
        list.add("oss://590974e50cf2e1e68db1a17d/1496978273891_photo");
        adapter.addAll(list);
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
        return R.layout.activity_meal_info;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        }
    }
}
