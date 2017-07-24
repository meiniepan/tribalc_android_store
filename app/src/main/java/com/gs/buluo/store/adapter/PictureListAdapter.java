package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.R;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;
import com.gs.buluo.store.view.widget.panel.BigImagePanel;

/**
 * Created by hjn on 2017/1/24.
 */
public class PictureListAdapter extends RecyclerAdapter<String> {
    public PictureListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<String> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StandardDetailHolder(parent);
    }

    private class StandardDetailHolder extends BaseViewHolder<String> {
        ImageView picture;

        public StandardDetailHolder(ViewGroup parent) {
            super(parent, R.layout.standard_detail_item);
        }

        @Override
        public void onInitializeView() {
            picture = findViewById(R.id.standard_detail_picture);
        }

        @Override
        public void setData(String entity) {
            super.setData(entity);
            if (entity == null) return;
            Glide.with(getContext()).load(GlideUtils.formatImageUrl(entity)).placeholder(R.mipmap.default_pic).into(picture);
        }

        @Override
        public void onItemViewClick(String entity) {
            super.onItemViewClick(entity);
            BigImagePanel panel = new BigImagePanel(getContext(), getData());
            panel.setPos(getData().indexOf(entity));
            panel.show();
        }
    }
}
