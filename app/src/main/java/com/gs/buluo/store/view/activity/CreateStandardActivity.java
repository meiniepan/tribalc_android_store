package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ListView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.NewStandardAdapter;
import com.gs.buluo.store.adapter.StandardValueAdapter;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.bean.StandardLevel;
import com.gs.buluo.store.eventbus.StandardRemoveEvent;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChooseMainPanel;
import com.gs.buluo.store.view.widget.panel.GroupSetStandardPanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/22.
 */
public class CreateStandardActivity extends BaseActivity implements View.OnClickListener, ChooseMainPanel.OnChooseFinished {
    @Bind(R.id.standard_list)
    ListView listView;
    private NewStandardAdapter standardListAdapter;
    ArrayList<GoodsPriceAndRepertory> standardList = new ArrayList<>();
    ArrayList<String> valueList = new ArrayList<>();
    ArrayList<String> value2List = new ArrayList<>();
    HashMap<String, GoodsPriceAndRepertory> cacheMap = new HashMap<>();

    @Bind(R.id.create_standard_name)
    EditText etTitle;
    @Bind(R.id.create_standard_value)
    EditText etValue;
    @Bind(R.id.create_standard_primary)
    EditText etName;
    @Bind(R.id.goods_standard_group)
    RecyclerView value1Group;

    private StandardValueAdapter valueAdapter;
    private StandardValueAdapter valueAdapter2;
    private View view;
    private EditText etValue2;
    private EditText etName2;
    private View secondView;
    private String[] values2;
    private String[] values1;
    private GoodsStandardMeta oldMeta;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.goods_create_standard_add).setOnClickListener(this);
        findViewById(R.id.standard_group_set).setOnClickListener(this);
        findViewById(R.id.create_standard_finish).setOnClickListener(this);
        findViewById(R.id.goods_create_standard_delete).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        view = findViewById(R.id.rl_goods_create_standard_second);
        view.setOnClickListener(this);
        valueAdapter = new StandardValueAdapter(valueList);
        value1Group.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        value1Group.setAdapter(valueAdapter);

        standardListAdapter = new NewStandardAdapter(this, standardList);
        standardListAdapter.setCache(cacheMap);
        listView.setAdapter(standardListAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            SerializableHashMap<String, GoodsPriceAndRepertory> serMap = (SerializableHashMap<String, GoodsPriceAndRepertory>) bundle.getSerializable("map");
            oldMeta = bundle.getParcelable("data");
            oldMeta.priceAndRepertoryMap = serMap.getMap();
            setOldData();
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStandardRemoved(StandardRemoveEvent event) {
        notifyPriceList();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_standard;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_create_standard_add:
                if (etValue.length() == 0) return;
                String value1 = etValue.getText().toString().trim();
                values1 = value1.split("、");
                if (values1 != null && values1.length > 0) {
                    Collections.addAll(valueList, values1);
                    removeSame(valueList);
                    valueAdapter.notifyDataSetChanged();
                }
                notifyPriceList();
                break;
            case R.id.rl_goods_create_standard_add:
                if (etValue2.length() == 0) return;
                String value2 = etValue2.getText().toString().trim();
                values2 = value2.split("、");
                if (values2 != null && values2.length > 0) {
                    Collections.addAll(value2List, values2);
                    removeSame(value2List);
                    valueAdapter2.notifyDataSetChanged();
                }
                notifyPriceList();
                break;
            case R.id.rl_goods_create_standard_second:
                view.setVisibility(View.GONE);
                ViewStub stub = (ViewStub) findViewById(R.id.standard_second_stub);
                if (stub != null) {
                    secondView = stub.inflate();
                    initSecondView(secondView);
                } else {
                    secondView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.standard_group_set:
                showSettingPanel();
                break;
            case R.id.create_standard_finish:
                if (etTitle.length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "商品规格标题不能为空");
                    return;
                }
                if (etName.length() == 0 || (value2List.size()!=0 && etName2.length() == 0)) {
                    ToastUtils.ToastMessage(getCtx(), "商品规格名称不能为空");
                    return;
                }
                for (GoodsPriceAndRepertory repertory : standardList) {
                    if (repertory.salePrice == 0) {
                        ToastUtils.ToastMessage(getCtx(), "销售价格不能为空");
                        return;
                    }
                    if (repertory.repertory == 0) {
                        ToastUtils.ToastMessage(getCtx(), "商品库存不能为空");
                        return;
                    }
                }
                showMainStandardPanel();
//                setStandardResult();
                break;
            case R.id.goods_create_standard_delete:
                removeFirstLevel();
                notifyPriceList();
                break;
            case R.id.goods_create_standard_delete2:
                secondView.setVisibility(View.GONE);
                etName2.setText("");
                etValue2.setText("");
                value2List.clear();
                valueAdapter2.notifyDataSetChanged();
                view.setVisibility(View.VISIBLE);
                notifyPriceList();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void showMainStandardPanel() {
        ChooseMainPanel panel = new ChooseMainPanel(this,standardList,this);
        panel.show();
    }

    private void removeFirstLevel() {
        valueList.clear();
        valueList.addAll(value2List);
        etValue.setText("");
        etName.setText("");
        valueAdapter.notifyDataSetChanged();
        if (etValue2 != null) {        //二级菜单被inflate
            etValue.setText(etValue2.getText().toString().trim());
            etName.setText(etName2.getText().toString().trim());
            etValue2.setText("");
            etName2.setText("");
            secondView.setVisibility(View.GONE);
            value2List.clear();
            valueAdapter2.notifyDataSetChanged();
            view.setVisibility(View.VISIBLE);
        }
    }

    private void removeSame(ArrayList<String> oldList) {
        ArrayList<String> newList = new ArrayList<>();
        for (String s : oldList) {
            if (!newList.contains(s)) newList.add(s);
        }
        oldList.clear();
        oldList.addAll(newList);
    }

    private void setOldData() {
        etTitle.setText(oldMeta.title);
        etName.setText(oldMeta.descriptions.primary.label);
        valueList.addAll(oldMeta.descriptions.primary.types);
        valueAdapter.notifyDataSetChanged();
        if (oldMeta.descriptions.secondary.label != null) {
            view.setVisibility(View.GONE);
            ViewStub stub = (ViewStub) findViewById(R.id.standard_second_stub);
            if (stub != null) {
                secondView = stub.inflate();
                initSecondView(secondView);
            }
            etName2.setText(oldMeta.descriptions.secondary.label);
            value2List.addAll(oldMeta.descriptions.secondary.types);
            valueAdapter2.notifyDataSetChanged();
        }
        setPrice();
    }

    private void setPrice() {
        HashMap<String, GoodsPriceAndRepertory> priceAndRepertoryMap = oldMeta.priceAndRepertoryMap;
        cacheMap.putAll(priceAndRepertoryMap);
        if (value2List.size() == 0) {
            for (String s : valueList) {
                standardList.add(priceAndRepertoryMap.get(s));
            }
        } else {
            for (String s1 : valueList) {
                for (String s2 : value2List) {
                    standardList.add(priceAndRepertoryMap.get(s1 + "^" + s2));
                }
            }
        }
        standardListAdapter.notifyDataSetChanged();
        CommonUtils.setListViewHeightBasedOnChildren(listView);
    }

    private void setStandardResult(String result) {
        GoodsStandardMeta standardMeta = new GoodsStandardMeta();
        standardMeta.title = etTitle.getText().toString().trim();
        setMetaDescription(standardMeta);

        HashMap<String, GoodsPriceAndRepertory> map = new HashMap<>();
        if (value2List.size() != 0) {
            for (GoodsPriceAndRepertory repo : standardList) {
                map.put(repo.firstName + "^" + repo.secondName, repo);
            }
        } else {
            for (GoodsPriceAndRepertory repo : standardList) {
                map.put(repo.firstName, repo);
            }
        }
        SerializableHashMap<String, GoodsPriceAndRepertory> serMap = new SerializableHashMap<>();
        serMap.setMap(map);

        Bundle bundle = new Bundle();
        bundle.putParcelable("data", standardMeta);
        bundle.putSerializable("map", serMap);
        bundle.putString(Constant.ForIntent.KEY,result);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showSettingPanel() {
        GroupSetStandardPanel panel = new GroupSetStandardPanel(this, new GroupSetStandardPanel.onSetFinishListener() {
            @Override
            public void onSetFinished(float price, int repertory) {
                setStandard(price, repertory);
            }
        });
        panel.show();
    }

    private void setStandard(float price, int repertory) {
        if (standardList.size() == 0) return;
        for (GoodsPriceAndRepertory bean : standardList) {
            bean.salePrice = price;
            bean.repertory = repertory;
            if (bean.secondName != null) {
                cacheMap.put(bean.firstName + "-" + bean.secondName, bean);
            } else {
                cacheMap.put(bean.firstName, bean);
            }
        }
        standardListAdapter.notifyDataSetChanged();
    }

    private void notifyPriceList() {
        standardList.clear();
        if (value2List.size() == 0) {       //如果没有二级规格
            for (String s : valueList) {
                GoodsPriceAndRepertory bean = new GoodsPriceAndRepertory();
                bean.firstName = s;
                standardList.add(bean);
            }
        } else {
            for (String s : valueList) {
                for (String s2 : value2List) {
                    GoodsPriceAndRepertory bean = new GoodsPriceAndRepertory();
                    bean.firstName = s;
                    bean.secondName = s2;
                    standardList.add(bean);
                }
            }
        }
        standardListAdapter.notifyDataSetChanged();
        CommonUtils.setListViewHeightBasedOnChildren(listView);
    }

    private void initSecondView(final View secondView) {
        RecyclerView recyclerView = (RecyclerView) secondView.findViewById(R.id.goods_create_standard_second);
        secondView.findViewById(R.id.rl_goods_create_standard_add).setOnClickListener(this);

        etValue2 = (EditText) secondView.findViewById(R.id.goods_create_standard_value2);
        etName2 = (EditText) secondView.findViewById(R.id.goods_create_standard_name2);

        valueAdapter2 = new StandardValueAdapter(value2List);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(valueAdapter2);

        secondView.findViewById(R.id.goods_create_standard_delete2).setOnClickListener(this);
    }

    public void setMetaDescription(GoodsStandardMeta metaDescription) {
        GoodsStandardDescriptions descriptions = new GoodsStandardDescriptions();
        descriptions.primary = new StandardLevel();
        descriptions.secondary = new StandardLevel();
        descriptions.primary.label = etName.getText().toString().trim();
        descriptions.primary.types = valueList;
        if (etName2 != null) {
            descriptions.secondary.label = etName2.getText().toString().trim();
            descriptions.secondary.types = value2List;
        }
        metaDescription.descriptions = descriptions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFinished(String result) {
        setStandardResult(result);
    }
}
