package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/1/10.
 */
public class FacilityAdapter extends RecyclerAdapter<Integer> {
    Context context;

    public FacilityAdapter(Context context, ArrayList<Integer> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public BaseViewHolder<Integer> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new FacilityHolder(parent);
    }

    private class FacilityHolder extends BaseViewHolder<Integer> {

        private TextView text;

        FacilityHolder(ViewGroup itemView) {
            super(itemView, R.layout.facility_item);
        }

        @Override
        public void onInitializeView() {
            text = findViewById(R.id.text_item);
        }

        @Override
        public void setData(Integer entity) {
            super.setData(entity);
            text.setText(entity);
        }

        @Override
        public void onItemViewClick(Integer entity) {
            text.setBackgroundResource(R.color.custom_color);
        }
    }
}
