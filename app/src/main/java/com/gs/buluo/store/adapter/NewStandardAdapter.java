package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;

import java.util.List;

/**
 * Created by hjn on 2017/1/22.
 */
public class NewStandardAdapter extends BaseAdapter {
    List<GoodsPriceAndRepertory> standardList;
    Context context;
    private EditText etOrigin;
    private EditText etSale;
    private EditText etRepo;
    private TextView tvName;

    public NewStandardAdapter(Context context, List<GoodsPriceAndRepertory> standardListBeen) {
        standardList = standardListBeen;
        this.context = context;
    }

    @Override
    public int getCount() {
        return standardList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.create_standard_item, null);
        }
        etOrigin = (EditText) convertView.findViewById(R.id.item_standard_origin);
        etSale = (EditText) convertView.findViewById(R.id.item_standard_sale);
        etRepo = (EditText) convertView.findViewById(R.id.item_standard_repertory);
        tvName = (TextView) convertView.findViewById(R.id.item_standard_name);
        convertView.findViewById(R.id.item_standard_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardList.remove(position);
                notifyDataSetChanged();
            }
        });
        GoodsPriceAndRepertory createStandardListBean = standardList.get(position);
        if (createStandardListBean.secondName != null)
            tvName.setText(createStandardListBean.firstName + "-" + createStandardListBean.secondName);
        else
            tvName.setText(createStandardListBean.firstName);

        etOrigin.setText(createStandardListBean.orginPrice+"");
        etSale.setText(createStandardListBean.salePrice+"");
        etRepo.setText(createStandardListBean.repertory+"");
        createStandardListBean.orginPrice = Float.parseFloat(etOrigin.getText().toString().trim());
        createStandardListBean.salePrice = Float.parseFloat(etSale.getText().toString().trim());
        createStandardListBean.repertory = Integer.parseInt(etRepo.getText().toString().trim());

        convertView.findViewById(R.id.item_standard_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
