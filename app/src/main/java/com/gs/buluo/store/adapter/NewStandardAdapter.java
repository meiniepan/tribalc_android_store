package com.gs.buluo.store.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.common.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.http.GET;

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
    private ConcurrentHashMap<String, GoodsPriceAndRepertory> cache;
    private GoodsPriceAndRepertory listBean;
    private Thread thread;

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
        return standardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        listBean = standardList.get(position);
        if (listBean.secondName != null) {
            tvName.setText(listBean.firstName + "-" + listBean.secondName);
            GoodsPriceAndRepertory repertory = cache.get(listBean.firstName + "^" + listBean.secondName);
            if (repertory != null){
                Log.e("NewStandardAdapter", "getView: 没有"+position);
                listBean = repertory;
            }
        } else {
            tvName.setText(listBean.firstName);
            GoodsPriceAndRepertory repertory = cache.get(listBean.firstName);
            if (repertory != null){
                listBean = repertory;
            }
        }

        if (listBean.originPrice != 0)
            etOrigin.setText(listBean.originPrice + "");
        else{
            etOrigin.setText("");
        }

        if (listBean.salePrice != 0)
            etSale.setText(listBean.salePrice + "");
        else{
            etSale.setText("");
        }

        if (listBean.repertory != 0)
            etRepo.setText(listBean.repertory + "");
        else{
            etRepo.setText("");
        }

        etRepo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==MotionEvent.ACTION_UP)
                    etRepo.setCursorVisible(true);
                return false;
            }
        });
        etOrigin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==MotionEvent.ACTION_UP)
                    etOrigin.setCursorVisible(true);
                return false;
            }
        });
        etSale.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==MotionEvent.ACTION_UP)
                    etSale.setCursorVisible(true);
                return false;
            }
        });

        etOrigin.setTag(position);
        etSale.setTag(position);
        etRepo.setTag(position);
        etOrigin.addTextChangedListener(new MyTextWatcher(0, etOrigin));
        etSale.addTextChangedListener(new MyTextWatcher(1, etSale));
        etRepo.addTextChangedListener(new MyTextWatcher(2, etRepo));

        convertView.findViewById(R.id.item_standard_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void setCache(ConcurrentHashMap<String, GoodsPriceAndRepertory> cache) {
        this.cache = cache;
    }

    private class MyTextWatcher implements TextWatcher {
        EditText editText;
        int pos;            //横向pos

        MyTextWatcher(int i, EditText editText) {
            pos = i;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            if (s != null && !"".equals(s.toString())) {
                Integer currentPos = (Integer) editText.getTag();  //竖直pos
                final GoodsPriceAndRepertory gpa = standardList.get(currentPos);
                if (gpa == null)  return;
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (pos < standardList.size()) {
                            if (pos == 0)
                                gpa.originPrice = Float.parseFloat(s.toString());
                            else if (pos == 1)
                                gpa.salePrice = Float.parseFloat(s.toString());
                            else
                                gpa.repertory = Integer.parseInt(s.toString());

                            if (gpa.secondName != null) {
                                cache.put(gpa.firstName + "^" + gpa.secondName, gpa);
                            } else {
                                cache.put(gpa.firstName, gpa);
                            }
                        }else {
                            thread.interrupt();
                        }
                    }
                });
                thread.start();
            }
        }
    }
}
