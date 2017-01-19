package com.gs.buluo.store.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
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
        private View textBg;

        FacilityHolder(ViewGroup itemView) {
            super(itemView, R.layout.facility_item);
        }

        @Override
        public void onInitializeView() {
            text = findViewById(R.id.text_item);
            textBg = findViewById(R.id.text_bg);
        }

        @Override
        public void setData(FacilityBean entity) {
            super.setData(entity);
            text.setText(entity.value);
            if (entity.isSelect){
                textBg.setBackgroundResource(R.drawable.facility_choosed);
                text.setTextColor(Color.WHITE);
            }
        }

        @Override
        public void onItemViewClick(FacilityBean entity) {
            if (entity.isSelect){
                textBg.setBackgroundResource(R.drawable.text_background_round);
                text.setTextColor(0xff2a2a2a);
                entity.isSelect=false;
            }else {
                textBg.setBackgroundResource(R.drawable.facility_choosed);
                text.setTextColor(Color.WHITE);
                entity.isSelect=true;
            }
        }
    }
}
