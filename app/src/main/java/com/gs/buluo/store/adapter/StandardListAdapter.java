package com.gs.buluo.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.AddGoodsWithStandardActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

/**
 * Created by hjn on 2017/1/23.
 */
public class StandardListAdapter extends RecyclerAdapter<GoodsStandardMeta> {
    Context mCtx;

    public StandardListAdapter(Context context) {
        super(context);
        mCtx=context;
    }
    @Override
    public BaseViewHolder<GoodsStandardMeta> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StandardListHolder(parent);
    }

    class StandardListHolder extends BaseViewHolder<GoodsStandardMeta>{
        TextView title;

        public StandardListHolder(ViewGroup parent) {
            super(parent, R.layout.standard_list_item);
        }

        @Override
        public void onInitializeView() {
            title = findViewById(R.id.standard_item_title);
            findViewById(R.id.standard_item_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     mCtx.startActivity(new Intent(mCtx,AddGoodsWithStandardActivity.class));
                }
            });
        }

        @Override
        public void setData(GoodsStandardMeta entity) {
            super.setData(entity);
            title.setText(entity.title);
        }

        @Override
        public void onItemViewClick(GoodsStandardMeta entity) {
            super.onItemViewClick(entity);
            ToastUtils.ToastMessage(mCtx,"click");
        }
    }
}
