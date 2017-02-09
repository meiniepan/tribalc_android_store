package com.gs.buluo.store.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.GoodsStandardDescriptions;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.model.GoodsModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by hjn on 2017/1/23.
 */
public class StandardListAdapter extends RecyclerAdapter<GoodsStandardMeta> {
    private List<GoodsStandardMeta> metaList;
    Context mCtx;
    private int currentPos = 0;


    public StandardListAdapter(Context context, List<GoodsStandardMeta> list) {
        super(context, list);
        metaList = list;
        mCtx = context;
    }

    @Override
    public BaseViewHolder<GoodsStandardMeta> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StandardListHolder(parent);
    }

    public int getCurrentPos() {
        return currentPos;
    }

    class StandardListHolder extends BaseViewHolder<GoodsStandardMeta> {
        TextView title;
        RadioButton radioButton;
        RecyclerView recyclerView;
        private StandardDetailAdapter adapter;

        public StandardListHolder(ViewGroup parent) {
            super(parent, R.layout.standard_list_item);
        }

        @Override
        public void onInitializeView() {
            radioButton = findViewById(R.id.goods_standard_select);
            title = findViewById(R.id.standard_item_title);
            recyclerView = findViewById(R.id.standard_detail_list);
        }

        @Override
        public void setData(final GoodsStandardMeta entity) {
            super.setData(entity);
            title.setText(entity.title);
            if (metaList.indexOf(entity) == currentPos) {
                radioButton.setChecked(true);
                recyclerView.setVisibility(View.VISIBLE);
                if (currentPos==0)return;
                getDetailData(entity);
            }else {
                radioButton.setChecked(false);
                recyclerView.setVisibility(View.GONE);
            }
        }

        private void getDetailData(GoodsStandardMeta entity) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            adapter = new StandardDetailAdapter(getContext());
            recyclerView.setAdapter(adapter);
            getGoodsList(entity);
        }

        private void getGoodsList(GoodsStandardMeta entity) {
            new GoodsModel().getGoodsStandard(entity.id, new TribeCallback<GoodsStandard>() {
                @Override
                public void onSuccess(Response<BaseResponse<GoodsStandard>> response) {
                    setGoodList(response.body().data);
                }

                @Override
                public void onFail(int responseCode, BaseResponse<GoodsStandard> body) {
                    ToastUtils.ToastMessage(getContext(),"获取规格商品信息失败");
                }
            });

        }

        private void setGoodList(GoodsStandard data) {
            GoodsStandardDescriptions descriptions = data.descriptions;
            Map<String, ListGoodsDetail> goodsIndexes = data.goodsIndexes;
            if (goodsIndexes.isEmpty())return;

            List<String> list=new ArrayList<>();
            if (data.descriptions.secondary==null){
                for (String value1 :descriptions.primary.types){
                    list.add(value1);
                }
            }else {
                for (String value1 :descriptions.primary.types){
                    for (String value2 :descriptions.secondary.types){
                        list.add(value1+"^"+value2);
                    }
                }
            }
            ArrayList<ListGoodsDetail> goodsDetails = new ArrayList<>();
            for (String s :list){
                goodsDetails.add(goodsIndexes.get(s));
            }
            adapter.addAll(goodsDetails);
        }

        @Override
        public void onItemViewClick(GoodsStandardMeta entity) {
            super.onItemViewClick(entity);
            currentPos = metaList.indexOf(entity);
            notifyDataSetChanged();
        }

    }
}
