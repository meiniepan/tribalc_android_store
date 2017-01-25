package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ListView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.NewStandardAdapter;
import com.gs.buluo.store.adapter.StandardValueAdapter;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.bean.StandardLevel;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.GroupSetStandardPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/22.
 */
public class CreateStandardActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.standard_list)
    ListView listView;
    private NewStandardAdapter standardListAdapter;
    ArrayList<GoodsPriceAndRepertory> standardList = new ArrayList<>();
    ArrayList<String> valueList = new ArrayList<>();
    ArrayList<String> value2List = new ArrayList<>();

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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.goods_create_standard_add).setOnClickListener(this);
        findViewById(R.id.standard_group_set).setOnClickListener(this);
        findViewById(R.id.create_standard_finish).setOnClickListener(this);
        findViewById(R.id.goods_create_standard_delete).setOnClickListener(this);
        view = findViewById(R.id.rl_goods_create_standard_second);
        view.setOnClickListener(this);
        valueAdapter = new StandardValueAdapter(valueList);
        value1Group.setLayoutManager(new GridLayoutManager(this, 3));
        value1Group.setAdapter(valueAdapter);

        standardListAdapter = new NewStandardAdapter(this, standardList);
        listView.setAdapter(standardListAdapter);
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
                valueList.clear();
                String value1 = etValue.getText().toString().trim();
                values1 = value1.split("、");
                if (values1 != null && values1.length > 0) {
                    Collections.addAll(valueList, values1);
                    valueAdapter.notifyDataSetChanged();
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
            case R.id.rl_goods_create_standard_add:
                if (etValue2.length() == 0) return;
                value2List.clear();
                String value2 = etValue2.getText().toString().trim();
                values2 = value2.split("、");
                if (values2 != null && values2.length > 0) {
                    Collections.addAll(value2List, values2);
                    valueAdapter2.notifyDataSetChanged();
                }
                notifyPriceList();
                break;
            case R.id.standard_group_set:
                showSettingPanel();
                break;
            case R.id.goods_create_standard_delete:
                notifyPriceList();
                break;
            case R.id.create_standard_finish:
                for (GoodsPriceAndRepertory repertory :standardList){
                    if (repertory.repertory==0||repertory.salePrice==0){
                        ToastUtils.ToastMessage(getCtx(),"规格信息填写不完整");
                        return;
                    }
                }
                setStandardResult();
                break;

        }
    }

    private void setStandardResult() {
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
        SerializableHashMap<String, GoodsPriceAndRepertory> serMap =new SerializableHashMap<>();
        serMap.setMap(map);

        Bundle bundle =new Bundle();
        bundle.putParcelable("data",standardMeta);
        bundle.putSerializable("map",serMap);
        Intent intent = new Intent();
//                intent.putExtra(Constant.STANDARD_INFO, standardMeta);
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(valueAdapter2);

        secondView.findViewById(R.id.goods_create_standard_delete2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueList.clear();
                valueList.addAll(value2List);
                valueAdapter = new StandardValueAdapter(valueList);
                value1Group.setAdapter(valueAdapter);
                if (etValue2 != null) {        //二级菜单被inflate
                    etValue.setText(etValue2.getText().toString().trim());
                    etName.setText(etName2.getText().toString().trim());
                }
                secondView.setVisibility(View.GONE);
                etName2.setText("");
                etValue2.setText("");
                value2List.clear();
                valueAdapter2.notifyDataSetChanged();
                view.setVisibility(View.VISIBLE);
                notifyPriceList();
            }
        });
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
        metaDescription.descriptions = descriptions;  //ok?
    }
}
