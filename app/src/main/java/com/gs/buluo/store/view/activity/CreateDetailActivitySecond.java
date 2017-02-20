package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.FacilityAdapter;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.network.MainService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.AppManager;
import com.gs.buluo.store.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/12.
 */
public class CreateDetailActivitySecond extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.create_facility)
    RecyclerView recyclerView;
    @Bind(R.id.create_logo)
    TextView tvLogo;
    @Bind(R.id.create_environment)
    TextView tvEnvi;
    @Bind(R.id.create_topic)
    EditText etTopic;
    @Bind(R.id.create_recommend)
    EditText etRecommend;
    @Bind(R.id.create_store_fee)
    EditText etFee;
    @Bind(R.id.create_store_reserve)
    Switch sReserve;

    private ArrayList<FacilityBean> facilityList;
    private StoreMeta storeBean;
    private StoreSetMealCreation mealCreation;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        storeBean = getIntent().getParcelableExtra(Constant.ForIntent.STORE_BEAN);
        mealCreation = new StoreSetMealCreation();
        findViewById(R.id.create_finish).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.create_logo_area).setOnClickListener(this);
        findViewById(R.id.create_environment_area).setOnClickListener(this);
        initFacility();
        recyclerView.setAdapter(new FacilityAdapter(this, facilityList));
    }

    private void initFacility() {
        facilityList = new ArrayList<>();
        facilityList.add(new FacilityBean(getString(R.string.subway)));
        facilityList.add(new FacilityBean(getString(R.string.bar)));
        facilityList.add(new FacilityBean(getString(R.string.baby_chair)));
        facilityList.add(new FacilityBean(getString(R.string.business_circle)));
        facilityList.add(new FacilityBean(getString(R.string.business_dinner)));
        facilityList.add(new FacilityBean(getString(R.string.facilities_for_disabled)));
        facilityList.add(new FacilityBean(getString(R.string.organic_food)));
        facilityList.add(new FacilityBean(getString(R.string.parking)));
        facilityList.add(new FacilityBean(getString(R.string.pet_ok)));
        facilityList.add(new FacilityBean(getString(R.string.room)));
        facilityList.add(new FacilityBean(getString(R.string.restaurants_of_hotel)));
        facilityList.add(new FacilityBean(getString(R.string.scene_seat)));
        facilityList.add(new FacilityBean(getString(R.string.small_party)));
        facilityList.add(new FacilityBean(getString(R.string.weekend_brunch)));
        facilityList.add(new FacilityBean( getString(R.string.valet_parking)));
        facilityList.add(new FacilityBean(getString( R.string.vip_rights)));
        facilityList.add(new FacilityBean(getString(R.string.wi_fi)));

        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layout);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_detail_second;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.create_finish:
                storeBean.topics = etTopic.getText().toString().trim();
                setFacility();
                createStore();
                break;
            case R.id.create_logo_area:
                intent.setClass(this, PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivityForResult(intent, 200);
                break;
            case R.id.create_environment_area:
                intent.setClass(this, PhotoActivity.class);
                intent.putStringArrayListExtra(Constant.ENVIRONMENT, storeBean.pictures);
                startActivityForResult(intent, 201);
                break;
        }
    }

    private void setFacility() {
        List<String> list = new ArrayList<>();
        for (FacilityBean bean : facilityList) {
            if (bean.isSelect) {
                list.add(bean.value);
            }
        }
        storeBean.facilities = list;
    }

    private void createStore() {
        TribeRetrofit.getInstance().createApi(MainService.class).createStore(TribeApplication.getInstance().getUserInfo().getId(), storeBean).
                enqueue(new TribeCallback<StoreMeta>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<StoreMeta>> response) {
                        saveStore(response.body().data);
                        Intent intent = new Intent();
                        intent.setClass(CreateDetailActivitySecond.this, CreateStoreFinishActivity.class);
                        AppManager.getAppManager().finishActivity(CreateDetailActivity.class);
                        AppManager.getAppManager().finishActivity(CreateStoreVarietyActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<StoreMeta> body) {
                        ToastUtils.ToastMessage(CreateDetailActivitySecond.this, R.string.connect_fail);
                    }
                });
        mealCreation.reservable = sReserve.isChecked();
        mealCreation.coordinate = storeBean.coordinate;
        mealCreation.personExpense = etFee.getText().toString().trim();
        mealCreation.name = storeBean.name;
        mealCreation.pictures = storeBean.pictures;
        mealCreation.recommendedReason = etRecommend.getText().toString().trim();
        mealCreation.topics = storeBean.topics;
        TribeRetrofit.getInstance().createApi(MainService.class).createServe(TribeApplication.getInstance().getUserInfo().getId(), mealCreation).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {

            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(CreateDetailActivitySecond.this, R.string.connect_fail);
            }
        });
    }

    private void saveStore(StoreMeta data) {
        StoreInfoDao storeInfoDao = new StoreInfoDao();
        StoreInfo first = storeInfoDao.findFirst();
        first.setLogo(data.getLogo());
        first.setName(data.getName());
        first.setStoreType(data.getStoreType());
        first.setAuthenticationStatus(data.getAuthenticationStatus());
        storeInfoDao.update(first);
        TribeApplication.getInstance().setUserInfo(data);
        EventBus.getDefault().post(new SelfEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean.logo = data.getStringExtra(Constant.LOGO);
            tvLogo.setText("1张");
        } else if (data != null && requestCode == 201 && resultCode == 202) {   //environment
            ArrayList<String> enPictures = data.getStringArrayListExtra(Constant.ENVIRONMENT);
            storeBean.pictures = enPictures;
            if (enPictures!=null&&enPictures.size()>0) mealCreation.mainPicture = enPictures.get(0);
            tvEnvi.setText(enPictures.size() + "张");
        }
    }
}
