package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.RepastBeanAdapter;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.RepastCategoryBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.StoreInfo;
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
 * Created by hjn on 2017/1/10.
 */
public class CreateDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.create_title)
    TextView title;
    @Bind(R.id.create_food_variety)
    RecyclerView recyclerView;

    TextView tvName;
    TextView tvSubName;
    TextView tvType;
    TextView tvPhone;
    TextView tvOtherPhone;
    TextView tvAddress;
    TextView tvLogo;
    TextView tvEnvi;
    TextView tvTime;
    EditText recommendReason;
    @Bind(R.id.store_create_next)
    TextView button;

    CreateStoreBean storeBean;
    private List<RepastCategoryBean> categoryBeanList;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        storeBean = getIntent().getParcelableExtra(Constant.ForIntent.STORE_BEAN);
        initView();
        StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
        storeBean.phone = userInfo.getPhone();
        tvPhone.setText(userInfo.getPhone());

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvType.setText(storeBean.category.toString());

        findViewById(R.id.store_create_address_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateDetailActivity.this, CreateStoreAddressActivity.class), 202);
            }
        });

    }

    private void initView() {
        ViewStub storeStub = (ViewStub) findViewById(R.id.create_goods_stub);
        ViewStub serveStub = (ViewStub) findViewById(R.id.create_serve_stub);
        if (Constant.GOODS.equals(storeBean.storeType)) {
            setStoreData(storeStub);
        } else {
            setServeData(serveStub);
        }
    }

    private void setStoreData(ViewStub storeStub) {
        title.setText("创建商铺");
        View storeView = storeStub.inflate();
        tvName = (TextView) storeView.findViewById(R.id.store_name);
        tvType = (TextView) storeView.findViewById(R.id.store_type);
        tvPhone = (TextView) storeView.findViewById(R.id.store_phone);
        tvOtherPhone = (TextView) storeView.findViewById(R.id.store_other_phone);
        tvAddress = (TextView) storeView.findViewById(R.id.store_address);
        recommendReason = (EditText) storeView.findViewById(R.id.create_recommend);
        tvLogo = (TextView) storeView.findViewById(R.id.create_logo);
        tvEnvi = (TextView) storeView.findViewById(R.id.create_environment);
        recommendReason.clearFocus();
        button.setText(R.string.submit);
        storeView.findViewById(R.id.create_logo_area).setOnClickListener(this);
        storeView.findViewById(R.id.create_environment_area).setOnClickListener(this);
        findViewById(R.id.store_create_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvName.length() == 0 || tvAddress.length() == 0) {
                    ToastUtils.ToastMessage(CreateDetailActivity.this, "信息填写不完整");
                    return;
                }
                setCookingStyle();
                storeBean.name = tvName.getText().toString().trim();
                storeBean.phone = tvPhone.getText().toString().trim();
                storeBean.otherPhone = tvOtherPhone.getText().toString().trim();
                storeBean.recommendedReason = recommendReason.getText().toString().trim();
                createStore();
            }
        });
    }

    private void setCookingStyle() {
        if (Constant.SET_MEAL.equals(storeBean.storeType)) {
            List<String> list = new ArrayList<>();
            for (RepastCategoryBean bean :categoryBeanList) {
                if (bean.isSelect){
                    list.add(bean.value);
                }
            }
            storeBean.cookingStyle = list;
        }
    }

    private void setServeData(ViewStub serveStub) {
        title.setText("创建店铺");
        View serveView = serveStub.inflate();
        tvName = (TextView) serveView.findViewById(R.id.store_name);
        tvSubName = (TextView) serveView.findViewById(R.id.store_sub_name);
        tvType = (TextView) serveView.findViewById(R.id.store_type);
        tvPhone = (TextView) serveView.findViewById(R.id.store_phone);
        tvOtherPhone = (TextView) serveView.findViewById(R.id.store_other_phone);
        tvAddress = (TextView) serveView.findViewById(R.id.store_address);
        if (storeBean.category == CreateStoreBean.StoreCategory.REPAST) {
            findViewById(R.id.create_food_area).setVisibility(View.VISIBLE);
            initFoodCategory();
        }
        tvTime = (TextView) serveView.findViewById(R.id.store_time);
        serveView.findViewById(R.id.store_create_time).setOnClickListener(this);
        findViewById(R.id.store_create_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvName.length() == 0 || tvAddress.length() == 0) {
                    ToastUtils.ToastMessage(CreateDetailActivity.this, "信息填写不完整");
                    return;
                }
                storeBean.name = tvName.getText().toString().trim();
                storeBean.subbranchName = tvSubName.getText().toString().trim();
                storeBean.phone = tvPhone.getText().toString().trim();
                storeBean.otherPhone = tvOtherPhone.getText().toString().trim();

                Intent intent = new Intent(CreateDetailActivity.this, CreateDetailActivitySecond.class);
                intent.putExtra(Constant.ForIntent.STORE_BEAN, storeBean);
                startActivity(intent);
            }
        });
    }

    private void createStore() {
        TribeRetrofit.getInstance().createApi(MainService.class).createStore(TribeApplication.getInstance().getUserInfo().getId(), storeBean).
                enqueue(new TribeCallback<StoreInfo>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<StoreInfo>> response) {
                        saveStore(response.body().data);
                        Intent intent = new Intent();
                        intent.setClass(CreateDetailActivity.this, CreateStoreFinishActivity.class);
                        AppManager.getAppManager().finishActivity(CreateStoreVarietyActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<StoreInfo> body) {
                        ToastUtils.ToastMessage(CreateDetailActivity.this, R.string.connect_fail);
                    }
                });
    }

    private void saveStore(StoreInfo data) {
        StoreInfoDao storeInfoDao = new StoreInfoDao();
        StoreInfo first = storeInfoDao.findFirst();
        data.setToken(first.token);
        storeInfoDao.update(data);
        TribeApplication.getInstance().setUserInfo(data);
        EventBus.getDefault().post(new SelfEvent());
    }

    private void initFoodCategory() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new RepastCategoryBean("西餐"));
        categoryBeanList.add(new RepastCategoryBean("咖啡厅"));
        categoryBeanList.add(new RepastCategoryBean("日料"));
        categoryBeanList.add(new RepastCategoryBean("自助餐"));
        categoryBeanList.add(new RepastCategoryBean("粤菜"));
        categoryBeanList.add(new RepastCategoryBean("意大利菜"));
        categoryBeanList.add(new RepastCategoryBean("火锅"));
        categoryBeanList.add(new RepastCategoryBean("融合菜"));
        categoryBeanList.add(new RepastCategoryBean("韩国料理"));
        categoryBeanList.add(new RepastCategoryBean("东南亚菜"));
        categoryBeanList.add(new RepastCategoryBean("西班牙菜"));
        categoryBeanList.add(new RepastCategoryBean("法国菜"));
        categoryBeanList.add(new RepastCategoryBean("云南菜"));
        categoryBeanList.add(new RepastCategoryBean("台湾菜"));
        categoryBeanList.add(new RepastCategoryBean("德国菜"));
        categoryBeanList.add(new RepastCategoryBean("其他"));
        recyclerView.setAdapter(new RepastBeanAdapter(CreateDetailActivity.this, categoryBeanList));

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
        return R.layout.activity_create_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 202 && resultCode == RESULT_OK) {
            String area = data.getStringExtra(Constant.AREA);
            String address = data.getStringExtra(Constant.ADDRESS);
            String[] arrs = area.split("-");
            storeBean.province = arrs[0];
            storeBean.city = arrs[1];
            storeBean.district = arrs[2];
            storeBean.address = address;
            tvAddress.setText(area + address);
        } else if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean.logo = data.getStringExtra(Constant.LOGO);
            tvLogo.setText("一张");
        } else if (data != null && requestCode == 201 && resultCode == 202) {   //environment
            ArrayList<String> enPictures = data.getStringArrayListExtra(Constant.ENVIRONMENT);
            storeBean.pictures = enPictures;
            if (enPictures != null) tvEnvi.setText(enPictures.size() + "张");
        } else if (data != null && requestCode == 203 && resultCode == RESULT_OK) {
            storeBean.businessHours = data.getStringExtra(Constant.SERVE_TIME);
            tvTime.setText(storeBean.businessHours);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.create_logo_area:
                intent.setClass(this, PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo");
                startActivityForResult(intent, 200);
                break;
            case R.id.create_environment_area:
                intent.setClass(this, PhotoActivity.class);
                startActivityForResult(intent, 201);
                break;
            case R.id.store_create_time:
                intent.setClass(this, ServeTimeActivity.class);
                startActivityForResult(intent, 203);
        }
    }
}
