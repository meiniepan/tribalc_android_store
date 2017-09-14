package com.gs.buluo.store.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/22.
 */
public class NewStandardAdapter extends BaseAdapter {
    private List<GoodsPriceAndRepertory> standardList;
    Context context;
    private ConcurrentHashMap<String, GoodsPriceAndRepertory> cache;

    private class NewStandardHolder {
        EditText etOrigin;
        EditText etSale;
        EditText etRepo;
        EditText etProfit;
        TextView tvName;

        View getConvertView(ViewGroup parent) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.create_standard_item, parent, false);
            etOrigin = (EditText) convertView.findViewById(R.id.item_standard_origin);
            etSale = (EditText) convertView.findViewById(R.id.item_standard_sale);
            etRepo = (EditText) convertView.findViewById(R.id.item_standard_repertory);
            etProfit = (EditText) convertView.findViewById(R.id.item_standard_profit);
            tvName = (TextView) convertView.findViewById(R.id.item_standard_name);
            return convertView;
        }
    }


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

    private NewStandardHolder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new NewStandardHolder();
            convertView = holder.getConvertView(parent);
        } else {
            holder = (NewStandardHolder) convertView.getTag();
        }

        GoodsPriceAndRepertory listBean = standardList.get(position);
        if (listBean.secondName != null) {
            holder.tvName.setText(listBean.firstName + "-" + listBean.secondName);
            GoodsPriceAndRepertory repertory = cache.get(listBean.firstName + "^" + listBean.secondName);
            if (repertory != null) {
                Log.e("NewStandardAdapter", "getView: 没有" + position);
                listBean = repertory;
            }
        } else {
            holder.tvName.setText(listBean.firstName);
            GoodsPriceAndRepertory repertory = cache.get(listBean.firstName);
            if (repertory != null) {
                listBean = repertory;
            }
        }

        if (listBean.originPrice != 0) {
            holder.etOrigin.setText(listBean.originPrice + "");
        } else {
            holder.etOrigin.setText("");
        }

        if (listBean.salePrice != 0)
            holder.etSale.setText(listBean.salePrice + "");
        else {
            holder.etSale.setText("");
        }

        if (listBean.repertory != 0)
            holder.etRepo.setText(listBean.repertory + "");
        else {
            holder.etRepo.setText("");
        }

        if (listBean.pfProfit != 0)
            holder.etProfit.setText(listBean.pfProfit + "");
        else {
            holder.etProfit.setText("");
        }

        holder.etRepo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    holder.etRepo.setCursorVisible(true);
                return false;
            }
        });
        holder.etOrigin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    holder.etOrigin.setCursorVisible(true);
                return false;
            }
        });
        holder.etSale.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    holder.etSale.setCursorVisible(true);
                return false;
            }
        });

        holder.etOrigin.setTag(position);
        holder.etSale.setTag(position);
        holder.etRepo.setTag(position);
        holder.etProfit.setTag(position);
        holder.etOrigin.addTextChangedListener(new MyTextWatcher(0, holder.etOrigin));
        holder.etSale.addTextChangedListener(new MyTextWatcher(1, holder.etSale));
        holder.etRepo.addTextChangedListener(new MyTextWatcher(2, holder.etRepo));
        holder.etProfit.addTextChangedListener(new MyTextWatcher(3, holder.etProfit));

        convertView.findViewById(R.id.item_standard_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardList.remove(position);
                notifyDataSetChanged();
            }
        });
        convertView.setTag(holder);
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
            if (standardList == null || standardList.size() == 0) return;
            if (s != null && !"".equals(s.toString())) {
                final Integer currentPos = (Integer) editText.getTag();  //竖直pos
                final GoodsPriceAndRepertory gpa = standardList.get(currentPos);
                if (gpa == null) return;
                setCache(s, currentPos, gpa);
            }
        }

        private void setCache(final Editable s, final Integer currentPos, GoodsPriceAndRepertory gpa) {
            Observable.just(gpa).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<GoodsPriceAndRepertory>() {
                        @Override
                        public void call(GoodsPriceAndRepertory goodsPriceAndRepertory) {
                            if (currentPos < standardList.size()) {
                                if (pos == 0)
                                    goodsPriceAndRepertory.originPrice = Float.parseFloat(s.toString());
                                else if (pos == 1)
                                    goodsPriceAndRepertory.salePrice = Float.parseFloat(s.toString());
                                else if (pos == 2) {
                                    goodsPriceAndRepertory.repertory = Integer.parseInt(s.toString());
                                } else if (pos == 3) {
                                    goodsPriceAndRepertory.pfProfit = Integer.parseInt(s.toString());
                                }
                                if (goodsPriceAndRepertory.secondName != null) {
                                    cache.put(goodsPriceAndRepertory.firstName + "^" + goodsPriceAndRepertory.secondName, goodsPriceAndRepertory);
                                } else {
                                    cache.put(goodsPriceAndRepertory.firstName, goodsPriceAndRepertory);
                                }
                            }
                        }
                    });
        }
    }
}
