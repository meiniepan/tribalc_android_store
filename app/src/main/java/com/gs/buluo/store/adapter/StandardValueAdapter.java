package com.gs.buluo.store.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.store.R;

import java.util.List;

/**
 * Created by hjn on 2017/1/22.
 */
public class StandardValueAdapter extends RecyclerView.Adapter<StandardValueAdapter.StandardValueHolder> {
    List<String> list;

    public StandardValueAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public StandardValueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StandardValueHolder(parent, R.layout.deletable_view);
    }

    @Override
    public void onBindViewHolder(StandardValueHolder holder, final int position) {
        holder.textView.setText(list.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StandardValueHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View delete;

        public StandardValueHolder(ViewGroup parent, int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            textView = (TextView) itemView.findViewById(R.id.custom_text);
            delete = itemView.findViewById(R.id.delete_icon);
        }
    }
}
