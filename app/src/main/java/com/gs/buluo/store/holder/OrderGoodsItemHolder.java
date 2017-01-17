package com.gs.buluo.store.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.store.R;

/**
 * Created by hjn on 2016/11/28.
 */
public class OrderGoodsItemHolder {
    Context mCtx;

    public OrderGoodsItemHolder(Context context) {
        mCtx = context;
    }

    public TextView name;
    public TextView brand;
    public SimpleDraweeView picture;
    public TextView account;
    public TextView people;
    public View view;

    public View getHolderView(ViewGroup parent) {
        view = LayoutInflater.from(mCtx).inflate(R.layout.order_goods_item, parent,false);
        name = (TextView) view.findViewById(R.id.order_item_goods_name);
        brand = (TextView) view.findViewById(R.id.order_item_goods_brand);
        account = (TextView) view.findViewById(R.id.order_item_goods_account);
        people = (TextView) view.findViewById(R.id.order_item_people);
        picture = (SimpleDraweeView) view.findViewById(R.id.order_item_goods_head);
        return view;
    }
}