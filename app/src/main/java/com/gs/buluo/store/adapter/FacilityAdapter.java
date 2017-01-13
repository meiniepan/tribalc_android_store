package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.FacilityBean;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/1/10.
 */
public class FacilityAdapter extends RecyclerAdapter<FacilityBean> {
    Context context;

    public FacilityAdapter(Context context, List<FacilityBean> list) {
        super(context,list);
        this.context = context;
    }

    @Override
    public BaseViewHolder<FacilityBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new FacilityHolder(parent);
    }

    private class FacilityHolder extends BaseViewHolder<FacilityBean> {

        private TextView text;

        FacilityHolder(ViewGroup itemView) {
            super(itemView, R.layout.facility_item);
        }

        @Override
        public void onInitializeView() {
            text = findViewById(R.id.text_item);
        }

        @Override
        public void setData(FacilityBean entity) {
            super.setData(entity);
            text.setText(entity.value);
            if (entity.isSelect){
                text.setBackgroundResource(R.color.custom_color);
            }
        }

        @Override
        public void onItemViewClick(FacilityBean entity) {
            if (entity.isSelect){
                text.setBackground(null);
                entity.isSelect=false;
            }else {
                text.setBackgroundResource(R.color.custom_color);
                entity.isSelect=true;
            }
        }
    }
}
