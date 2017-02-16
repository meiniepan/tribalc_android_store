package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.FacilityAdapter;
import com.gs.buluo.store.adapter.RepastBeanAdapter;
import com.gs.buluo.store.bean.CategoryBean;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.bean.StoreSetMealCreation;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.StoreInfoPresenter;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IInfoView;
import com.gs.buluo.store.view.widget.CustomAlertDialog;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/19.
 */
public class MealStoreInfoActivity extends BaseActivity implements View.OnClickListener,IInfoView {
    @Bind(R.id.info_store_name)
    TextView tvName;
    @Bind(R.id.info_sub_store_name)
    TextView tvSubName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
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
    @Bind(R.id.store_area)
    TextView etArea;

    @Bind(R.id.info_store_reserve)
    Switch sReserve;
    @Bind(R.id.store_info_facility)
    RecyclerView recyclerView;
    @Bind(R.id.store_info_cook_style)
    RecyclerView cookRecyclerView;

    @Bind(R.id.info_store_auth)
    Button mAuth;
    @Bind(R.id.info_store_edit)
    TextView tvEdit;
    @Bind(R.id.info_logo)
    TextView tvLogo;
    @Bind(R.id.info_environment)
    TextView tvEnvi;

    private ArrayList<FacilityBean> facilityList = new ArrayList<>();
    private ArrayList<CategoryBean> cookingList =new ArrayList<>();
    private Context mCtx;

    @Bind(R.id.info_store_introduction)
    EditText etDesc;
    private StoreMeta storeBean;
    private StoreSetMealCreation mealCreation;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_environment).setOnClickListener(this);
        findViewById(R.id.info_store_edit).setOnClickListener(this);
        findViewById(R.id.ll_store_info_address).setOnClickListener(this);
        findViewById(R.id.info_store_introduction).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        mAuth.setOnClickListener(this);
        initFacility();
        initData();
    }

    @Override
    public void setData(StoreMeta data) {
        dismissDialog();
        storeBean = data;
        if (data.category == StoreMeta.StoreCategory.REPAST) {
            initCookingStyle();
        }
        if ("NOT_START".equals(data.authenticationStatus)) {
            mAuth.setVisibility(View.VISIBLE);
        }
        tvEnvi.setText(data.pictures == null? "":data.pictures.size()+"张");
        etDesc.setText(data.desc);
        tvName.setText(data.name);
        tvSubName.setText(data.subbranchName);
        tvCategory.setText(data.category.toString());
        tvPhone.setText(data.phone);
        tvOrPhone.setText(data.otherPhone);
        etArea.setText(data.markPlace);
        tvAddress.setText(data.address==null? "":data.province + data.city + data.district + data.address);
        List<String> list = data.facilities;
        if (list!=null){
            for (FacilityBean bean : facilityList) {
                if (list.contains(bean.value)) {
                    bean.isSelect = true;
                }
            }
        }
        recyclerView.setAdapter(new FacilityAdapter(this, facilityList));

        List<String> cookingStyles  = data.cookingStyle;
        if (cookingStyles!=null){
            for (CategoryBean bean :cookingList){
                if (cookingStyles.contains(bean.value)){
                    bean.isSelect = true;
                }
            }
        }
        cookRecyclerView.setAdapter(new RepastBeanAdapter(getCtx(), cookingList));
    }

    private void initCookingStyle() {
        findView(R.id.ll_cook_style).setVisibility(View.VISIBLE);
        cookingList.add(new CategoryBean("西餐"));
        cookingList.add(new CategoryBean("咖啡厅"));
        cookingList.add(new CategoryBean("日料"));
        cookingList.add(new CategoryBean("自助餐"));
        cookingList.add(new CategoryBean("西班牙菜"));
        cookingList.add(new CategoryBean("意大利菜"));
        cookingList.add(new CategoryBean("火锅"));
        cookingList.add(new CategoryBean("融合菜"));
        cookingList.add(new CategoryBean("韩国料理"));
        cookingList.add(new CategoryBean("东南亚菜"));
        cookingList.add(new CategoryBean("粤菜"));
        cookingList.add(new CategoryBean("法国菜"));
        cookingList.add(new CategoryBean("云南菜"));
        cookingList.add(new CategoryBean("台湾菜"));
        cookingList.add(new CategoryBean("其他"));
        cookingList.add(new CategoryBean("德国菜"));

        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        cookRecyclerView.setLayoutManager(layout);
    }

    @Override
    public void setMealData(StoreSetMealCreation mealData) {
        dismissDialog();
        mealCreation = mealData;
        tvFee.setText(mealData.personExpense);
        tvRecommend.setText(mealData.recommendedReason);
        tvTopic.setText(mealData.topics);
        sReserve.setChecked(mealData.reservable);
    }

    private void initFacility() {
        facilityList.add(new FacilityBean(getString(R.string.subway)));
        facilityList.add(new FacilityBean(getString(R.string.bar)));
        facilityList.add(new FacilityBean(getString(R.string.business_circle)));
        facilityList.add(new FacilityBean(getString(R.string.business_dinner)));
        facilityList.add(new FacilityBean(getString(R.string.facilities_for_disabled)));
        facilityList.add(new FacilityBean( getString(R.string.organic_food)));
        facilityList.add(new FacilityBean(getString( R.string.parking)));
        facilityList.add(new FacilityBean(getString(R.string.pet_ok)));
        facilityList.add(new FacilityBean(getString(R.string.room)));
        facilityList.add(new FacilityBean(getString(R.string.restaurants_of_hotel)));
        facilityList.add(new FacilityBean( getString(R.string.scene_seat)));
        facilityList.add(new FacilityBean( getString(R.string.small_party)));
        facilityList.add(new FacilityBean(getString(R.string.weekend_brunch)));
        facilityList.add(new FacilityBean(getString( R.string.valet_parking)));
        facilityList.add(new FacilityBean( getString(R.string.vip_rights)));
        facilityList.add(new FacilityBean(getString(R.string.wi_fi)));

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
        ((StoreInfoPresenter)mPresenter).getDetailStoreInfo();
        ((StoreInfoPresenter)mPresenter).getSetMeal();
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
                intent.setClass(mCtx, Authentication1Activity.class);
                startActivity(intent);
                break;
            case R.id.info_store_logo:
                intent.setClass(mCtx, PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivityForResult(intent, 200);
                break;
            case R.id.info_store_environment:
                intent.setClass(mCtx, PhotoActivity.class);
                intent.putExtra(Constant.ENVIRONMENT,storeBean.pictures);
                startActivityForResult(intent, 201);
                break;
            case R.id.ll_store_info_address:
                intent.setClass(mCtx, CreateStoreAddressActivity.class);
                startActivityForResult(intent, 202);
                break;
            case R.id.info_store_edit:
                updateStoreInfo();
                break;
            case R.id.back:
                back();
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
            if (enPictures!=null&&enPictures.size()>0){
                mealCreation.mainPicture = enPictures.get(0);
            }
            tvEnvi.setText(enPictures.size() + "张");
        } else if (data != null &&requestCode == 202 && resultCode == RESULT_OK) {
            String area = data.getStringExtra(Constant.AREA);
            String address = data.getStringExtra(Constant.ADDRESS);
            String[] arrs = area.split("-");
            storeBean.province = arrs[0];
            storeBean.city = arrs[1];
            storeBean.district = arrs[2];
            storeBean.address = address;
            double lan = data.getDoubleExtra(Constant.LATITUDE, 0);
            double lon = data.getDoubleExtra(Constant.LONGITUDE,0);
            storeBean.coordinate = new double[]{lon,lan};
            tvAddress.setText(area + storeBean.address);
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

    private void updateStoreInfo() {
        showLoadingDialog();
        storeBean.name = tvName.getText().toString().trim();
        storeBean.subbranchName = tvSubName.getText().toString().trim();
        storeBean.otherPhone = tvOrPhone.getText().toString().trim();
        storeBean.desc = etDesc.getText().toString().trim();
        storeBean.markPlace = etArea.getText().toString().trim();
        setFacility();
        setCookingStyle();
        ((StoreInfoPresenter)mPresenter).updateStore(storeBean);

        mealCreation.personExpense = tvFee.getText().toString().trim();
        mealCreation.topics = tvTopic.getText().toString().trim();
        mealCreation.reservable = sReserve.isChecked();
        mealCreation.pictures = storeBean.pictures;
        mealCreation.name = storeBean.name;
        mealCreation.category = storeBean.category;
        mealCreation.recommendedReason = tvRecommend.getText().toString().trim();
        ((StoreInfoPresenter)mPresenter).updateMeal(mealCreation);
    }

    private void setCookingStyle() {
        storeBean.cookingStyle =new ArrayList<>();
        for (CategoryBean bean  : cookingList){
            if (bean.isSelect){
                storeBean.cookingStyle.add(bean.value);
            }
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new StoreInfoPresenter();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        new CustomAlertDialog.Builder(mCtx).setTitle(getString(R.string.reminder)).setMessage(getString(R.string.save_update)).
                setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStoreInfo();
            }
        }).setNegativeButton(getString(R.string.not_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this,res);
    }

    @Override
    public void updateSuccess() {
        dismissDialog();
        ToastUtils.ToastMessage(this,R.string.update_success);
        finish();
    }
}
