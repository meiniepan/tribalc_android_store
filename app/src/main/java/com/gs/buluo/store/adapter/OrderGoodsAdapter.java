package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gs.buluo.store.bean.ListGoods;
import com.gs.buluo.store.bean.CartItem;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.holder.OrderGoodsItemHolder;
import com.gs.buluo.store.utils.GlideUtils;

import java.util.List;

/**
 * Created by hjn on 2016/11/28.
 */
public class OrderGoodsAdapter extends BaseAdapter {
    private final List<CartItem> itemList1;
    private final OrderBean order;
    private Context mCtx;

    public OrderGoodsAdapter(List<CartItem> itemList, Context context, OrderBean entity) {
        mCtx = context;
        itemList1 = itemList;
        this.order = entity;
    }

    @Override
    public int getCount() {
        return itemList1.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        CartItem cartItem = itemList1.get(position);
        ListGoods goods = cartItem.goods;
        OrderGoodsItemHolder holder = null;
        if (convertView == null) {
            holder = new OrderGoodsItemHolder(mCtx);
            convertView = holder.getHolderView(parent);
        } else {
            holder = (OrderGoodsItemHolder) convertView.getTag();
        }
        holder.name.setText(goods.name);
        holder.brand.setText(goods.brand);
        holder.people.setText(order.nickName);
        holder.account.setText(order.user);
        GlideUtils.loadImage(mCtx,goods.mainPicture, holder.picture);

        convertView.setTag(holder);
        return convertView;
    }
}
