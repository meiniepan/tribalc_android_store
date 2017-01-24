package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Date;

/**
 * Created by hjn on 2017/1/24.
 */
public class StoreGoodsListAdapter extends RecyclerAdapter<GoodsMeta> {
    public StoreGoodsListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<GoodsMeta> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StoreGoodsHolder(parent);
    }


    private class StoreGoodsHolder extends BaseViewHolder<GoodsMeta> {
        public ImageView picture;
        public TextView name;
        public TextView repertory;
        public TextView price;
        public TextView saleNum;
        public TextView time;


        public StoreGoodsHolder(ViewGroup parent) {
            super(parent, R.layout.store_goods_item);
        }

        @Override
        public void onInitializeView() {
            picture=findViewById(R.id.goods_item_picture);
            name = findViewById(R.id.goods_item_name);
            repertory = findViewById(R.id.goods_item_repertory);
            price = findViewById(R.id.goods_item_price);
            saleNum = findViewById(R.id.goods_item_sale);
            time = findViewById(R.id.goods_item_create_time);

            findViewById(R.id.goods_item_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.ToastMessage(getContext(),"下架");
                }
            });

            findViewById(R.id.goods_item_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public void setData(GoodsMeta entity) {
            super.setData(entity);
            picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(getContext()).load(FresoUtils.formatImageUrl(entity.mainPicture)).placeholder(R.mipmap.default_pic).into(picture);
            name.setText(entity.name);
            repertory.setText(entity.priceAndRepertory.repertory+"");
            price.setText(entity.priceAndRepertory.salePrice+"");
            saleNum.setText(100+"");
            time.setText(TribeDateUtils.dateFormat5(new Date()));
        }
    }
}
