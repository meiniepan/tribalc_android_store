package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.FacilityAdapter;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.StoreSetMeal;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/19.
 */
public class StoreInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.info_store_name)
    TextView tvName;
    @Bind(R.id.info_sub_store_name)
    TextView tvSubName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
    @Bind(R.id.info_store_area)
    TextView tvArea;
    @Bind(R.id.info_store_fee)
    TextView tvFee;
    @Bind(R.id.info_store_phone)
    TextView tvPhone;
    @Bind(R.id.info_store_other_phone)
    TextView tvOrPhone;
    @Bind(R.id.info_store_address)
    TextView tvAddress;
    @Bind(R.id.info_store_recommend)
    TextView tvRecommend;
    @Bind(R.id.info_store_topic)
    TextView tvTopic;

    @Bind(R.id.info_store_reserve)
    Switch sReserve;
    @Bind(R.id.store_info_facility)
    RecyclerView recyclerView;

    @Bind(R.id.info_store_auth)
    Button mAuth;
    @Bind(R.id.info_store_edit)
    TextView tvEdit;

    private boolean isEdit;
    private ArrayList<FacilityBean> facilityList;
    private Context mCtx;

    @Bind(R.id.info_introduction_arrow)
    View introArrow;
    @Bind(R.id.info_logo_arrow)
    View logoArrow;
    @Bind(R.id.info_en_arrow)
    View enArrow;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.info_store_introduction).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_edit).setOnClickListener(this);
        mAuth.setOnClickListener(this);
        initFacility();
        initData();
    }

    public void setData(CreateStoreBean data) {
        if ("NOT_START".equals(data.storeAuthenticationStatus)) {
            mAuth.setVisibility(View.VISIBLE);
        }
        tvName.setText(data.name);
        tvSubName.setText(data.subbranchName);
        tvCategory.setText(data.category.toString());
        tvArea.setText(data.province + data.city + data.district);
        tvPhone.setText(data.phone);
        tvOrPhone.setText(data.otherPhone);
        tvAddress.setText(data.address);
        List<String> list = data.facilities;
        for (FacilityBean bean : facilityList) {
            if (list.contains(bean.key)) {
                bean.isSelect = true;
            }
        }
        recyclerView.setAdapter(new FacilityAdapter(this, facilityList));
    }

    public void setMealData(StoreSetMeal mealData) {
        tvFee.setText(mealData.personExpense);
        tvRecommend.setText(mealData.recommendedReason);
        tvTopic.setText(mealData.topics);
        sReserve.setChecked(mealData.isReservable);
    }

    private void initFacility() {
        facilityList = new ArrayList<>();
        facilityList.add(new FacilityBean("subway", R.string.subway));
        facilityList.add(new FacilityBean("bar", R.string.bar));
        facilityList.add(new FacilityBean("business_circle", R.string.business_circle));
        facilityList.add(new FacilityBean("business_dinner", R.string.business_dinner));
        facilityList.add(new FacilityBean("facilities_for_disabled", R.string.facilities_for_disabled));
        facilityList.add(new FacilityBean("organic_food", R.string.organic_food));
        facilityList.add(new FacilityBean("parking", R.string.parking));
        facilityList.add(new FacilityBean("pet_ok", R.string.pet_ok));
        facilityList.add(new FacilityBean("room", R.string.room));
        facilityList.add(new FacilityBean("restaurants_of_hotel", R.string.restaurants_of_hotel));
        facilityList.add(new FacilityBean("scene_seat", R.string.scene_seat));
        facilityList.add(new FacilityBean("small_party", R.string.small_party));
        facilityList.add(new FacilityBean("weekend_brunch", R.string.weekend_brunch));
        facilityList.add(new FacilityBean("valet_parking", R.string.valet_parking));
        facilityList.add(new FacilityBean("vip_rights", R.string.vip_rights));
        facilityList.add(new FacilityBean("wi-fi", R.string.wi_fi));

        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layout);
    }

    private void initData() {
        showLoadingDialog();
        new MainModel().getDetailStoreInfo(new TribeCallback<CreateStoreBean>() {
            @Override
            public void onSuccess(Response<BaseResponse<CreateStoreBean>> response) {
                setData(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CreateStoreBean> body) {
                dismissDialog();
                ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
            }
        });

        new MainModel().getSetMeal(new TribeCallback<StoreSetMeal>() {
            @Override
            public void onSuccess(Response<BaseResponse<StoreSetMeal>> response) {
                dismissDialog();
                setMealData(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<StoreSetMeal> body) {
                dismissDialog();
                ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_info;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.info_store_auth:
                intent.setClass(mCtx, QualificationActivity.class);
                startActivity(intent);
                break;
            case R.id.info_store_edit:
                if (isEdit){
                    updateStoreInfo();
                    tvEdit.setText("编辑");
                    isEdit=false;
                }else {
                    tvEdit.setText("完成");
                    isEdit=true;
                }
                break;
        }
    }

    private void updateStoreInfo() {

    }
}
