package com.gs.buluo.store.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.R;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/2/6.
 */
public class PhotoAdapter extends RecyclerAdapter<String> {
    private  OnDeleteListener onDeleteListener;
    public PhotoAdapter(Context context, ArrayList<String> pictures,OnDeleteListener onDeleteListener) {
        super(context,pictures);
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public BaseViewHolder<String> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new PhotoHolder(parent);
    }

    private class PhotoHolder extends BaseViewHolder<String> {
        ImageView content;
        ImageView delete;
        public PhotoHolder(ViewGroup parent) {
            super(parent, R.layout.photo_item);
        }

        @Override
        public void onInitializeView() {
            content = findViewById(R.id.photo_content);
            delete = findViewById(R.id.photo_delete);
        }

        @Override
        public void setData(final String entity) {
            Glide.with(getContext()).load(FresoUtils.formatImageUrl(entity)).centerCrop().into(content);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(entity);
                    onDeleteListener.onDelete();
                }
            });
        }
    }

    public interface OnDeleteListener{
        void onDelete();
    }
}
