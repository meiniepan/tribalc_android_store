package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.TagAdapter;
import com.gs.buluo.store.bean.CategoryBean;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.RequestBodyBean.CreateGoodsRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.CreateGoodsResponse;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.model.GoodsModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/23.
 */
public class CreateGoodsFinalActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.create_goods_number)
    EditText etNum;
    @Bind(R.id.create_goods_fee)
    EditText etFee;
    @Bind(R.id.create_goods_category)
    RecyclerView recyclerView;
    @Bind(R.id.create_goods_tags)
    EditText etTags;
    private GoodsMeta goodsMeta;
    private GoodsStandardMeta standardMeta;

    private boolean published;
    private ArrayList<CategoryBean> categoryBeanList;
    private TagAdapter beanAdapter;
    private GoodsModel model;
    private String primaryStandardKey;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        model = new GoodsModel();
        Intent intent = getIntent();
        goodsMeta = intent.getParcelableExtra(Constant.ForIntent.META);
        setCategoryData();
        Bundle bundle = intent.getExtras();
        standardMeta = bundle.getParcelable("data");
        primaryStandardKey = bundle.getString(Constant.ForIntent.KEY);
        SerializableHashMap<String, GoodsPriceAndRepertory> serializableHashMap = (SerializableHashMap<String, GoodsPriceAndRepertory>) bundle.getSerializable("map");
        if (serializableHashMap != null)
            standardMeta.priceAndRepertoryMap = serializableHashMap.getMap();

        findView(R.id.create_goods_save).setOnClickListener(this);
        findView(R.id.create_goods_publish).setOnClickListener(this);
        findView(R.id.create_goods_desc).setOnClickListener(this);

        setTags();
    }

    private void setTags() {
        ArrayList<String> tags = goodsMeta.tags;
        if (tags != null) {
            for (CategoryBean bean : categoryBeanList) {
                if (tags.contains(bean.value)) {
                    bean.isSelect = true;
                }
            }
        }
        etNum.setText(goodsMeta.number);
        etFee.setText(goodsMeta.expressFee ==0? "":goodsMeta.expressFee+"");
    }

    private void setCategoryData() {
        switch (goodsMeta.category) {
            case OFFICE:
                setOfficeData();
                break;
            case MAKEUP:
                setBeautyData();
                break;
            case VIP:
                break;
            case HOUSE:
                setHouseData();
                break;
            case PENETRATION:
                setPenetrationData();
                break;
            case LIVING:
                setLivingData();
                break;
            case FOOD:
                setFoodData();
                break;
        }
        if (categoryBeanList!=null){
            beanAdapter = new TagAdapter(this, categoryBeanList);
            beanAdapter.setLimit(3);
            recyclerView.setAdapter(beanAdapter);
        }
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layout);
    }

    private void setFoodData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("休闲食品"));
        categoryBeanList.add(new CategoryBean("饮料冲调"));
        categoryBeanList.add(new CategoryBean("地方特产"));
        categoryBeanList.add(new CategoryBean("中外名酒"));
        categoryBeanList.add(new CategoryBean("新鲜水果"));
        categoryBeanList.add(new CategoryBean("名茶"));
    }

    private void setLivingData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("保温壶"));
        categoryBeanList.add(new CategoryBean("玻璃杯"));
        categoryBeanList.add(new CategoryBean("锅具套装"));
        categoryBeanList.add(new CategoryBean("压力锅"));
        categoryBeanList.add(new CategoryBean("汤锅"));
        categoryBeanList.add(new CategoryBean("水壶"));
        categoryBeanList.add(new CategoryBean("保鲜盒"));
        categoryBeanList.add(new CategoryBean("储物架"));
        categoryBeanList.add(new CategoryBean("净化除味"));
        categoryBeanList.add(new CategoryBean("烘焙/烧烤"));
        categoryBeanList.add(new CategoryBean("刀剪菜盘"));
        categoryBeanList.add(new CategoryBean("餐具 "));
    }

    private void setPenetrationData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("营养辅食"));
        categoryBeanList.add(new CategoryBean("童车童床"));
        categoryBeanList.add(new CategoryBean("尿裤湿巾"));
        categoryBeanList.add(new CategoryBean("洗护用品"));
        categoryBeanList.add(new CategoryBean("培养用品"));
        categoryBeanList.add(new CategoryBean("奶粉"));
    }

    private void setHouseData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("收纳艺品"));
        categoryBeanList.add(new CategoryBean("布艺软饰"));
        categoryBeanList.add(new CategoryBean("抱枕靠垫"));
        categoryBeanList.add(new CategoryBean("床上用品"));
        categoryBeanList.add(new CategoryBean("创意家具"));
        categoryBeanList.add(new CategoryBean("花瓶花艺"));
        categoryBeanList.add(new CategoryBean("浴室用品"));
        categoryBeanList.add(new CategoryBean("净化除味"));
        categoryBeanList.add(new CategoryBean("装饰字画"));
        categoryBeanList.add(new CategoryBean("地毯地垫"));
        categoryBeanList.add(new CategoryBean("灯具"));
        categoryBeanList.add(new CategoryBean("毯子"));
        categoryBeanList.add(new CategoryBean("家纺"));
    }

    private void setBeautyData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("面部护理"));
        categoryBeanList.add(new CategoryBean("身体护理"));
        categoryBeanList.add(new CategoryBean("口腔护理"));
        categoryBeanList.add(new CategoryBean("香水"));
        categoryBeanList.add(new CategoryBean("彩妆"));
        categoryBeanList.add(new CategoryBean("洗发护发"));
        categoryBeanList.add(new CategoryBean("敏感可用"));
        categoryBeanList.add(new CategoryBean("睫毛膏"));
        categoryBeanList.add(new CategoryBean("油性肌肤"));
        categoryBeanList.add(new CategoryBean("干性肌肤"));
        categoryBeanList.add(new CategoryBean("无酒精"));
        categoryBeanList.add(new CategoryBean("纯植物"));
    }

    private void setOfficeData() {
        categoryBeanList = new ArrayList<>();
        categoryBeanList.add(new CategoryBean("办公设备"));
        categoryBeanList.add(new CategoryBean("办公耗材"));
        categoryBeanList.add(new CategoryBean("传真设备"));
        categoryBeanList.add(new CategoryBean("打印机"));
        categoryBeanList.add(new CategoryBean("多功能一体机"));
        categoryBeanList.add(new CategoryBean("碎纸机"));
        categoryBeanList.add(new CategoryBean("考勤机"));
        categoryBeanList.add(new CategoryBean("保险柜"));
        categoryBeanList.add(new CategoryBean("办公家具"));
        categoryBeanList.add(new CategoryBean("办公文具"));
        categoryBeanList.add(new CategoryBean("文件管理"));
        categoryBeanList.add(new CategoryBean("财会用品"));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_goods_final;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_goods_save:
                published = false;
                createGoods(false);
                break;
            case R.id.create_goods_publish:
                published = true;
                createGoods(true);
                break;
            case R.id.create_goods_desc:
                Intent intent = new Intent(this, IntroductionActivity.class);
                intent.putExtra(Constant.ForIntent.FLAG, Constant.GOODS);
                intent.putExtra(Constant.ForIntent.INTRODUCTION,goodsMeta.detail);
                startActivityForResult(intent, 201);
                break;
        }
    }

    private void createGoods(boolean b) {
        if (etFee.length() != 0)
            goodsMeta.expressFee = Float.parseFloat(etFee.getText().toString().trim());
        goodsMeta.number = etNum.getText().toString().trim();
        goodsMeta.published = b;
        goodsMeta.tags = new ArrayList<>();
        if (etTags.length() > 0) {
            List<String> asList = Arrays.asList(etTags.getText().toString().trim().split("、"));
            goodsMeta.tags.addAll(asList);
        }
        for (CategoryBean bean : categoryBeanList) {
            if (bean.isSelect)
                goodsMeta.tags.add(bean.value);
        }
        if (goodsMeta.tags.size()>3){
            ToastUtils.ToastMessage(this,R.string.tags_max);
            return;
        }
        if (goodsMeta.isEdit) {
            updateGoods();
        } else {
            doCreateGoods();
        }
    }

    private void doCreateGoods() {
        CreateGoodsRequestBody body = new CreateGoodsRequestBody();
        body.goodsMeta = goodsMeta;
        if (standardMeta != null) {
            body.standardMeta = standardMeta;
        }
        body.primaryStandardKey = primaryStandardKey;
        model.createGoods(body, new Callback<CreateGoodsResponse>() {
            @Override
            public void onResponse(Call<CreateGoodsResponse> call, Response<CreateGoodsResponse> response) {
                ToastUtils.ToastMessage(getCtx(), R.string.add_success);
                Intent intent = new Intent(getCtx(), MainActivity.class);
                intent.putExtra(Constant.ForIntent.FLAG, Constant.GOODS);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<CreateGoodsResponse> call, Throwable t) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
            }
        });

    }

    private void updateGoods() {
        model.updateGoods(goodsMeta.id, goodsMeta, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                ToastUtils.ToastMessage(getCtx(), R.string.update_success);
                Intent intent = new Intent(getCtx(), MainActivity.class);
                intent.putExtra(Constant.ForIntent.FLAG, Constant.GOODS);
                intent.putExtra(Constant.PUBLISHED, published);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            goodsMeta.detail = data.getStringExtra(Constant.ForIntent.INTRODUCTION);
        }
    }
}
