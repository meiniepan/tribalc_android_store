package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.FacilityAdapter;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.SelfPresenter;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.ISelfView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener, Callback<BaseResponse<CreateStoreBean>>, ISelfView {
    @Bind(R.id.store_setting_facility)
    RecyclerView recyclerView;
    @Bind(R.id.tv_address)
    TextView mDistrict;
    @Bind(R.id.tv_detail_address)
    TextView mDetailAddress;
    @Bind(R.id.self_iv_head)
    SimpleDraweeView header;
    @Bind(R.id.tv_nickname)
    TextView mName;
    @Bind(R.id.tv_number)
    TextView mPhone;
    @Bind(R.id.self_reserve)
    Switch reserveSwitch;

    Context mCtx;
    public final int addressCode = 200;
    private StoreInfo userInfo;
    private ArrayList<FacilityBean> facilityList;
    private CreateStoreBean bean;
    private FacilityAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.ll_head).setOnClickListener(this);
        findViewById(R.id.ll_address).setOnClickListener(this);
        findViewById(R.id.ll_detail_address).setOnClickListener(this);
        findViewById(R.id.ll_number).setOnClickListener(this);
        findViewById(R.id.ll_nickname).setOnClickListener(this);
        findViewById(R.id.self_back).setOnClickListener(this);
        findViewById(R.id.self_save).setOnClickListener(this);
        findViewById(R.id.self_reserve).setOnClickListener(this);

        findViewById(R.id.store_setting_introduce_area).setOnClickListener(this);
        initFacility();
        showLoadingDialog();
        new MainModel().getCreateStoreInfo(this);
        mCtx = this;
    }

    private void initFacility() {
        facilityList = new ArrayList<>();
        facilityList.add(new FacilityBean("wi-fi", R.string.wi_fi));
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
        facilityList.add(new FacilityBean("subway", R.string.subway));
        facilityList.add(new FacilityBean("valet_parking", R.string.valet_parking));
        facilityList.add(new FacilityBean("vip_rights", R.string.vip_rights));
        facilityList.add(new FacilityBean("weekend_brunch", R.string.weekend_brunch));

        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layout);
        adapter = new FacilityAdapter(this, facilityList);
        recyclerView.setAdapter(adapter);
        recyclerView.stopScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = TribeApplication.getInstance().getUserInfo();
        initData();
    }

    private void initData() {
        if (null != userInfo) {
            FresoUtils.loadImage(userInfo.getLogo(), header);
            mName.setText(userInfo.getName());
            mPhone.setText(userInfo.getPhone());
            mDistrict.setText(userInfo.getArea());
            mDetailAddress.setText(userInfo.getAddress());
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_self;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mCtx, ModifyInfoActivity.class);
        switch (view.getId()) {
            case R.id.ll_head:
                intent.setClass(mCtx, PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivity(intent);
                break;
            case R.id.ll_nickname:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.NAME);
                startActivityForResult(intent, 201);
                break;
            case R.id.ll_number:
                intent.setClass(mCtx, PhoneVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_address:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.ADDRESS);
                startActivityForResult(intent, 205);
                break;
            case R.id.ll_detail_address:
                intent.setClass(mCtx, AddressListActivity.class);
                startActivityForResult(intent, addressCode);
                break;
            case R.id.self_back:
                finish();
            case R.id.self_save:
                ArrayList<String> list = new ArrayList<>();
                for (FacilityBean bean : facilityList) {
                    if (bean.isSelect) {
                        list.add(bean.key);
                    }
                }
                bean.facilities = list;
                showLoadingDialog();
                ((SelfPresenter) mPresenter).updateUser(Constant.FACILITIES, "", bean);
                break;
            case R.id.self_reserve:
                bean.reservable = reserveSwitch.isChecked();
                ((SelfPresenter) mPresenter).updateUser(Constant.RESERVABLE, "", bean);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 201:
//                    mName.setText(data.getStringExtra(Constant.NAME));
//                    break;
//                case 205:
//                    mDistrict.setText(data.getStringExtra(Constant.AREA));
//                    break;
//            }
//        }
//    }

    @Override
    public void onResponse(Call<BaseResponse<CreateStoreBean>> call, Response<BaseResponse<CreateStoreBean>> response) {
        dismissDialog();
        bean = response.body().data;
        reserveSwitch.setChecked(bean.reservable);
        List<String> list = bean.facilities;
        for (FacilityBean bean:facilityList){
            if (list!=null&&list.contains(bean.key)){
                bean.isSelect=true;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<BaseResponse<CreateStoreBean>> call, Throwable t) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }

    @Override
    public void updateSuccess(String key, String value) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.update_success);
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }
}
