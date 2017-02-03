package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.FacilityAdapter;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.StoreSetMealResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.MainService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.CustomAlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/19.
 */
public class MealStoreInfoActivity extends BaseActivity implements View.OnClickListener {
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
    @Bind(R.id.info_logo)
    TextView tvLogo;
    @Bind(R.id.info_environment)
    TextView tvEnvi;


    private boolean isEdit;
    private ArrayList<FacilityBean> facilityList;
    private Context mCtx;

    @Bind(R.id.info_introduction_arrow)
    View introArrow;
    @Bind(R.id.info_logo_arrow)
    View logoArrow;
    @Bind(R.id.info_en_arrow)
    View enArrow;
    private CreateStoreBean storeBean;
    private StoreSetMealCreation mealCreation;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.info_store_introduction).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_edit).setOnClickListener(this);
        findViewById(R.id.info_area).setOnClickListener(this);
        findViewById(R.id.info_store_introduction).setOnClickListener(this);
        mAuth.setOnClickListener(this);
        initFacility();
        initData();
    }

    public void setData(CreateStoreBean data) {
        storeBean = data;
        if ("NOT_START".equals(data.authenticationStatus)) {
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

    public void setMealData(StoreSetMealCreation mealData) {
        mealCreation = mealData;
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

        new MainModel().getSetMeal(new Callback<StoreSetMealResponse>() {
            @Override
            public void onResponse(Call<StoreSetMealResponse> call, Response<StoreSetMealResponse> response) {
                dismissDialog();
                if (response!=null&&response.body() != null && response.body().code == 200) {
                    setMealData(response.body().data.get(0));
                }else {
                    ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<StoreSetMealResponse> call, Throwable t) {
                ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
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
            case R.id.info_store_auth:
                intent.setClass(mCtx, IdentificationActivity.class);
                startActivity(intent);
                break;
            case R.id.info_store_logo:
                intent.setClass(mCtx,PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE,"logo");
                startActivityForResult(intent,200);
                break;
            case R.id.info_store_environment:
                intent.setClass(mCtx,PhotoActivity.class);
                startActivityForResult(intent,201);
                break;
            case R.id.info_area:
                intent.setClass(mCtx,CreateStoreAddressActivity.class);
                startActivityForResult(intent,202);
                break;

            case R.id.info_store_edit:
                updateStoreInfo();
                break;
            case R.id.info_store_introduction:
                intent.setClass(mCtx,IntroductionActivity.class);
                startActivityForResult(intent,203);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean.logo = data.getStringExtra(Constant.LOGO);
            tvLogo.setText("1张");
        } else if (data != null && requestCode == 201 && resultCode == 202) {   //environment
            ArrayList<String> enPictures = data.getStringArrayListExtra(Constant.ENVIRONMENT);
            storeBean.pictures = enPictures;
            tvEnvi.setText(enPictures.size() + "张");
        } else if (requestCode == 202 && resultCode == RESULT_OK){
            String area = data.getStringExtra(Constant.AREA);
            String address = data.getStringExtra(Constant.ADDRESS);
            String[] arrs = area.split("-");
            storeBean.province = arrs[0];
            storeBean.city = arrs[1];
            storeBean.district = arrs[2];
            storeBean.address = address;
            tvArea.setText(area);
            tvAddress.setText(storeBean.address);
        } else if (requestCode == 203 && resultCode == RESULT_OK){

        }
    }

    private void setFacility() {
        List<String> list = new ArrayList<>();
        for (FacilityBean bean : facilityList) {
            if (bean.isSelect) {
                list.add(bean.key);
            }
        }
        storeBean.facilities = list;
    }

    private void updateStoreInfo() {
        showLoadingDialog();
        storeBean.name = tvName.getText().toString().trim();
        storeBean.subbranchName = tvSubName.getText().toString().trim();
        storeBean.otherPhone = tvOrPhone.getText().toString().trim();
        storeBean.isReservable = sReserve.isChecked();
        setFacility();
        new MainModel().updateStore(TribeApplication.getInstance().getUserInfo().getId(), "name,subbranchName,desc,otherPhone,province,city,district,address" +
                ",pictures,facilities", "", storeBean, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                saveStore(storeBean);
                ToastUtils.ToastMessage(mCtx,R.string.update_success);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
            }
        });

        mealCreation.personExpense = tvFee.getText().toString().trim();
        mealCreation.topics = tvTopic.getText().toString().trim();
        mealCreation.isReservable = sReserve.isChecked();
        mealCreation.pictures =storeBean.pictures;
        mealCreation.name = storeBean.name;
        mealCreation.recommendedReason = tvRecommend.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(MainService.class).updateMeal(mealCreation.id,"name,pictures,topics,recommendedReason,personExpense,isReservable",mealCreation)
        .enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                dismissDialog();
                finish();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                dismissDialog();
                ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
            }
        });
    }

    private void saveStore(CreateStoreBean data) {
        StoreInfoDao storeInfoDao = new StoreInfoDao();
        StoreInfo first = storeInfoDao.findFirst();
        data.setToken(first.token);
        storeInfoDao.update(data);
        TribeApplication.getInstance().setUserInfo(data);
        EventBus.getDefault().post(new SelfEvent());
    }

    @Override
    public void onBackPressed() {
        new CustomAlertDialog.Builder(mCtx).setTitle("提示").setMessage("确定保存更改么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStoreInfo();
            }
        }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}
