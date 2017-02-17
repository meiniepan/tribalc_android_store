package com.gs.buluo.store.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/2/13.
 */
public class MainStandardAdapter extends BaseAdapter {
    Context mCtx;
    ArrayList<GoodsPriceAndRepertory> standardList;
    private GoodsPriceAndRepertory repertory;

    public MainStandardAdapter(Context context, ArrayList<GoodsPriceAndRepertory> standardList) {
        mCtx = context;
        this.standardList = standardList;
    }

    @Override
    public int getCount() {
        return standardList.size();
    }

    @Override
    public Object getItem(int position) {
        return standardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.choose_main_item,parent,false);
        }
        final TextView text = (TextView) convertView.findViewById(R.id.choose_item_name);
        RadioButton checkBox = (RadioButton) convertView.findViewById(R.id.choose_item_check);
        repertory = standardList.get(position);

        text.setText(repertory.firstName + (repertory.secondName == null ? "  " : " - "+repertory.secondName));
        checkBox.setChecked(repertory.checked);
        if (repertory.checked){
            text.setBackgroundResource(R.drawable.facility_choosed);
            text.setTextColor(0xfff2f2f2);
        }else {
            text.setBackgroundResource(R.drawable.text_background_round);
            text.setTextColor(0xff2a2a2a);
        }

        return convertView;
    }
}
