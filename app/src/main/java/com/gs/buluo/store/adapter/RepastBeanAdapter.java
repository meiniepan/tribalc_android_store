package com.gs.buluo.store.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.RepastCategoryBean;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/1/19.
 */
public class RepastBeanAdapter extends RecyclerAdapter<RepastCategoryBean> {
    Context context;

    public RepastBeanAdapter(Context context, List<RepastCategoryBean> list) {
        super(context,list);
        this.context = context;
    }

    @Override
    public BaseViewHolder<RepastCategoryBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new RepastBeanAdapter.RepastHolder(parent);
    }

    private class RepastHolder extends BaseViewHolder<RepastCategoryBean> {
        private TextView text;
        private View textBg;

        RepastHolder(ViewGroup itemView) {
            super(itemView, R.layout.repast_item);
        }

        @Override
        public void onInitializeView() {
            text = findViewById(R.id.text_item);
            textBg = findViewById(R.id.text_bg);
        }

        @Override
        public void setData(RepastCategoryBean entity) {
            super.setData(entity);
            text.setText(entity.value);
            if (entity.isSelect){
                textBg.setBackgroundResource(R.drawable.facility_choosed);
                text.setTextColor(Color.WHITE);
            }
        }

        @Override
        public void onItemViewClick(RepastCategoryBean entity) {
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
