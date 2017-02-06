package com.gs.buluo.store.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.CategoryBean;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/1/19.
 */
public class RepastBeanAdapter extends RecyclerAdapter<CategoryBean> {
    Context context;
    private int limit = 0;
    private int count = 0;

    public RepastBeanAdapter(Context context, List<CategoryBean> list) {
        super(context,list);
        this.context = context;
    }

    @Override
    public BaseViewHolder<CategoryBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new RepastBeanAdapter.RepastHolder(parent);
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private class RepastHolder extends BaseViewHolder<CategoryBean> {
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
        public void setData(CategoryBean entity) {
            super.setData(entity);
            text.setText(entity.value);
            if (entity.isSelect){
                textBg.setBackgroundResource(R.drawable.facility_choosed);
                text.setTextColor(Color.WHITE);
            }
        }

        @Override
        public void onItemViewClick(CategoryBean entity) {
            if (entity.isSelect){
                count--;
                textBg.setBackgroundResource(R.drawable.text_background_round);
                text.setTextColor(0xff2a2a2a);
                entity.isSelect=false;
            }else {
                if (limit!=0&&count>=limit){
                    ToastUtils.ToastMessage(getContext(),R.string.tags_max);
                    return;
                }
                count++;
                textBg.setBackgroundResource(R.drawable.facility_choosed);
                text.setTextColor(Color.WHITE);
                entity.isSelect=true;
            }
        }
    }
}
