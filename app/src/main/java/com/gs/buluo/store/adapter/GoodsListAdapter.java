package com.gs.buluo.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.ListGoods;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.activity.GoodsDetailActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class GoodsListAdapter extends RecyclerAdapter<ListGoods> {
    private List<ListGoods> mDatas;
    Context mCtx;

    public GoodsListAdapter(Context context) {
        super(context);
        mCtx = context;
    }

    @Override
    public BaseViewHolder<ListGoods> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new GoodsHolder(parent);
    }

    class GoodsHolder extends BaseViewHolder<ListGoods> {
        ImageView picture;
        TextView name;
        TextView price;
        TextView brand;

        public GoodsHolder(ViewGroup parent) {
            super(parent, R.layout.good_list_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            name = findViewById(R.id.goods_list_name);
            brand = findViewById(R.id.goods_list_brand);
            price = findViewById(R.id.goods_list_price);
            picture = findViewById(R.id.goods_list_picture);
        }

        @Override
        public void setData(ListGoods entity) {
            super.setData(entity);
            name.setText(entity.name);
            price.setText("￥" + entity.salePrice);
            brand.setText(entity.brand);
            GlideUtils.loadImage(getContext(),entity.mainPicture, picture);
        }

        @Override
        public void onItemViewClick(ListGoods listGoods) {
            super.onItemViewClick(listGoods);
            Intent intent = new Intent(mCtx, GoodsDetailActivity.class);
            intent.putExtra(Constant.GOODS_ID, listGoods.id);
            mCtx.startActivity(intent);
        }
    }
}
