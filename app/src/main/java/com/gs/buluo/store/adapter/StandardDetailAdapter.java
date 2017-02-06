package com.gs.buluo.store.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

/**
 * Created by hjn on 2017/1/24.
 */
public class StandardDetailAdapter extends RecyclerAdapter<ListGoodsDetail> {
    public StandardDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<ListGoodsDetail> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StandardDetailHolder(parent);
    }

    private class StandardDetailHolder extends BaseViewHolder<ListGoodsDetail> {
        ImageView picture;
        public StandardDetailHolder(ViewGroup parent) {
            super(parent, R.layout.standard_detail_item);
        }

        @Override
        public void onInitializeView() {
            picture = findViewById(R.id.standard_detail_picture);
        }

        @Override
        public void setData(ListGoodsDetail entity) {
            super.setData(entity);
            if (entity==null)return;
            Glide.with(getContext()).load(FresoUtils.formatImageUrl(entity.mainPicture)).placeholder(R.mipmap.default_pic).into(picture);
        }

        @Override
        public void onItemViewClick(ListGoodsDetail entity) {
            super.onItemViewClick(entity);
            ToastUtils.ToastMessage(getContext(),"你点击了"+entity.name+entity.title);
        }
    }
}
