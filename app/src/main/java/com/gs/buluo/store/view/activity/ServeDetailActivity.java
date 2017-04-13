package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.DetailStore;
import com.gs.buluo.store.bean.DetailStoreSetMeal;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.ServeApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideBannerLoader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.pulltozoom.PullToZoomScrollViewEx;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class ServeDetailActivity extends BaseActivity implements View.OnClickListener{
    Context mCtx;
    TextView tvName;
    private TextView tvPrice;
    private TextView tvCollectNum;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvReason;
    private TextView tvMarkplace;
    private TextView tvDistance;
    private TextView tvBrand;
    private TextView tvTime;
    private TextView tvTopic;
    private Banner banner;
    private String id;
    private LinearLayout facilitiesGroup;
    private HashMap<String, Integer> map = new HashMap<>();
    private LatLng des;
    ImageView logo;
    private TextView counts;
    ArrayList<String> mFacilities;

    @Bind(R.id.detail_scroll_view)
    PullToZoomScrollViewEx scrollView;
    private List<Double> coordinate;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        id = getIntent().getStringExtra(Constant.SERVE_ID);
        getDetailInfo(id);
        mCtx = this;
        setBarColor(R.color.transparent);
        initMap();
        initContentView();
    }

    private void initContentView() {
        View zoomView = LayoutInflater.from(this).inflate(R.layout.detail_zoom_layout, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.detail_content_layout, null, false);
        View headView = LayoutInflater.from(this).inflate(R.layout.detail_head_layout, null, false);
        banner = (Banner) zoomView.findViewById(R.id.server_detail_banner);
        logo = (ImageView) zoomView.findViewById(R.id.server_detail_logo);

        tvName = (TextView) contentView.findViewById(R.id.server_detail_name);
        tvPrice = (TextView) contentView.findViewById(R.id.server_detail_person_price);
        tvCollectNum = (TextView) contentView.findViewById(R.id.server_detail_collect);
        tvAddress = (TextView) contentView.findViewById(R.id.service_shop_address);
        tvPhone = (TextView) contentView.findViewById(R.id.service_shop_number);
        tvReason = (TextView) contentView.findViewById(R.id.server_detail_comment_reason);
        tvMarkplace = (TextView) contentView.findViewById(R.id.server_detail_markPlace);
        tvDistance = (TextView) contentView.findViewById(R.id.server_detail_distance);
        tvBrand = (TextView) contentView.findViewById(R.id.server_detail_category);
        tvTime = (TextView) contentView.findViewById(R.id.server_detail_work_time);
        tvTopic = (TextView) contentView.findViewById(R.id.server_detail_topic);
        facilitiesGroup = (LinearLayout) contentView.findViewById(R.id.server_detail_facilities);
        counts = (TextView) contentView.findViewById(R.id.server_detail_facilities_counts);

        contentView.findViewById(R.id.service_phone_call).setOnClickListener(this);
        contentView.findViewById(R.id.service_location).setOnClickListener(this);
        contentView.findViewById(R.id.service_call_server).setOnClickListener(this);
        contentView.findViewById(R.id.rl_server_detail_facilities).setOnClickListener(this);


        int screenWidth = CommonUtils.getScreenWidth(this);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(screenWidth, (int) (12.0F * (screenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_service_detail;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.service_phone_call:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.service_location:
                intent.setClass(mCtx, MapActivity.class);
                double[] array = new double[]{coordinate.get(0), coordinate.get(1)};
                intent.putExtra(Constant.ForIntent.SERVE_POSITION, array);
                startActivity(intent);
                break;
            case R.id.service_booking_food:
                ToastUtils.ToastMessage(mCtx, R.string.no_function);
                break;
            case R.id.service_booking_seat:
                break;
            case R.id.service_call_server:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data1 = Uri.parse("tel:" + getString(R.string.help_phone));
                intent.setData(data1);
                startActivity(intent);
                break;
            case R.id.rl_server_detail_facilities:
                if (mFacilities == null) return;
                intent.setClass(getCtx(), FacilityDetailActivity.class);
                intent.putStringArrayListExtra(Constant.ForIntent.FANCILITY, mFacilities);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void getDetailInfo(String id) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).getServeDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<DetailStoreSetMeal>>() {
                    @Override
                    public void onNext(BaseResponse<DetailStoreSetMeal> goodListBaseResponse) {
                        DetailStoreSetMeal data = goodListBaseResponse.data;
                        if (mRoot != null) setData(data);
                    }
                });
    }


    private void setData(DetailStoreSetMeal data) {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImageLoader(new GlideBannerLoader());
        banner.isAutoPlay(false);
        banner.setImages(data.pictures);
        banner.start();
        tvName.setText(data.name);
        DetailStore detailStore = data.detailStore;
        tvPhone.setText(detailStore.phone);
        tvAddress.setText(detailStore.address == null ? "" : detailStore.city + detailStore.district + detailStore.address);
        tvCollectNum.setText(detailStore.collectionNum + "");
        tvMarkplace.setText(detailStore.markPlace);
        tvPrice.setText(data.personExpense);
        tvReason.setText(data.recommendedReason);
        tvBrand.setText((detailStore.cookingStyle != null && detailStore.cookingStyle.size() != 0) ? detailStore.cookingStyle.get(0) + " | " : detailStore.category.toString() + " | ");
        String businessHours = detailStore.businessHours;
        if (businessHours == null) tvTime.setVisibility(View.GONE);
        else tvTime.setText("每天 " + businessHours);

        tvTopic.setText(data.topics);
        if (data.detailStore.coordinate != null) {
            coordinate = data.detailStore.coordinate;
            setDistance(coordinate);
        }
        ArrayList<String> facilities = detailStore.facilities;
        setFacilities(facilities);
        if (facilities != null && facilities.size() > 4) {
            counts.setText("+".concat(facilities.size() - 4 + ""));
        }
        GlideUtils.loadImage(this, detailStore.logo, logo, true);
    }

    private void initMap() {
        map.put(getString(R.string.baby_chair), R.mipmap.baby_chair);
        map.put(getString(R.string.bar), R.mipmap.bar);
        map.put(getString(R.string.business_circle), R.mipmap.business_circle);
        map.put(getString(R.string.business_dinner), R.mipmap.business_dinner);
        map.put(getString(R.string.facilities_for_disabled), R.mipmap.facilities_for_disabled);
        map.put(getString(R.string.organic_food), R.mipmap.organic_food);
        map.put(getString(R.string.parking), R.mipmap.parking);
        map.put(getString(R.string.pet_ok), R.mipmap.pet_ok);
        map.put(getString(R.string.restaurants_of_hotel), R.mipmap.restaurants_of_hotel);
        map.put(getString(R.string.room), R.mipmap.room);
        map.put(getString(R.string.scene_seat), R.mipmap.scene_seat);
        map.put(getString(R.string.small_party), R.mipmap.small_party);
        map.put(getString(R.string.subway), R.mipmap.subway);
        map.put(getString(R.string.valet_parking), R.mipmap.valet_parking);
        map.put(getString(R.string.vip_rights), R.mipmap.vip_rights);
        map.put(getString(R.string.weekend_brunch), R.mipmap.weekend_brunch);
        map.put(getString(R.string.wi_fi), R.mipmap.wi_fi);
    }

    public void setFacilities(List<String> facilities) {
        if (facilities == null || facilities.size() == 0) return;
        mFacilities = new ArrayList<>();
        for (String facility : facilities) {
            View facilityView = View.inflate(this, R.layout.serve_detail_facility, null);
            ImageView iv = (ImageView) facilityView.findViewById(R.id.facility_image);
            TextView tv = (TextView) facilityView.findViewById(R.id.facility_text);
            tv.setText(facility);
            Integer resId = map.get(facility);
            if (resId!=null)iv.setImageResource(resId);
            mFacilities.add(facility + "," + resId);
            if (facilitiesGroup.getChildCount() == 4) continue;
            facilitiesGroup.addView(facilityView);
        }
    }

    public void setDistance(List<Double> distance) {
        des = new LatLng(distance.get(1), distance.get(0));
        LatLng myPos = TribeApplication.getInstance().getPosition();
        if (myPos != null)
            tvDistance.setText(" | " + CommonUtils.getDistance(des, myPos));
    }
}
