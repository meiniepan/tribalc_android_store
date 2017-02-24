package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.GoodsLevel1Adapter1;
import com.gs.buluo.store.adapter.GoodsLevel1Adapter2;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.StandardLevel;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsChoosePanel extends Dialog implements View.OnClickListener, DialogInterface.OnDismissListener {
    private OnShowInDetail onShowInDetail;
    @Bind(R.id.goods_level1)
    RecyclerView leve1View1;
    @Bind(R.id.goods_level2)
    RecyclerView leve1View2;

    @Bind(R.id.goods_board_number)
    TextView mNumber;
    @Bind(R.id.goods_board_choose_kind)
    TextView mKind;
    @Bind(R.id.goods_board_choose_price)
    TextView mPrice;
    @Bind(R.id.goods_board_choose_remain)
    TextView mRemainNumber;
    @Bind(R.id.goods_choose_icon)
    ImageView mIcon;

    @Bind(R.id.goods_standard_type2)
    TextView type2;
    @Bind(R.id.goods_standard_type1)
    TextView type1;

    Context mContext;

    private int nowNum = 1;
    private String leve11Key;
    private String level2Key;
    private ListGoodsDetail defaultEntity;
    private GoodsLevel1Adapter2 adapter2;
    private GoodsLevel1Adapter1 adapter1;
    private View car;
    //    private View buy;
    private OnSelectFinish selectFinish;
    private AddCartListener addCartListener;

    public GoodsChoosePanel(Context context, OnShowInDetail onShowInDetail) {
        super(context, R.style.my_dialog);
        this.onShowInDetail = onShowInDetail;
        mContext = context;
        initView();
    }

    public void setData(GoodsStandard goodsEntity) {
        initData(goodsEntity);
    }

    public void setRepertory(ListGoodsDetail goodsDetail) {
        defaultEntity = goodsDetail;
        if (defaultEntity == null) return;
        mPrice.setText(defaultEntity.salePrice);
        mRemainNumber.setText(defaultEntity.repertory + "");
        GlideUtils.loadImage(getContext(),defaultEntity.mainPicture, mIcon);
    }

    private void initData(final GoodsStandard entity) {
        if (entity == null) {   //一级都没有
            findViewById(R.id.goods_board_repertory).setVisibility(View.GONE);
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else if (entity.descriptions.secondary == null) {    //只有一级
            setLevelOneData(entity);
            findViewById(R.id.level2_view).setVisibility(View.GONE);
        } else if (entity.descriptions.secondary != null) {  //两级
            setLevelTwoData(entity);
        }
    }

    private void setLevelOneData(GoodsStandard entity) {
        StandardLevel primary = entity.descriptions.primary;
        type1.setText(primary.label);
        final GoodsLevel1Adapter1 adapter1 = new GoodsLevel1Adapter1(mContext, primary.types);
        leve1View1.setAdapter(adapter1);

//        leve11Key = (entity.descriptions.primary.types).get(0);
        final Map<String, ListGoodsDetail> goodsMap = entity.goodsIndexes;
        for (String s : primary.types) {
            if (goodsMap.get(s) == null) {
                adapter1.setUnClickable(s);
            }
        }
        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                mKind.setText(s);
                leve11Key = s;
                defaultEntity = goodsMap.get(leve11Key);
                if (TextUtils.isEmpty(s) ||defaultEntity==null) return;
                GlideUtils.loadImage(getContext(),defaultEntity.mainPicture, mIcon);
                mPrice.setText(defaultEntity.salePrice);
                mRemainNumber.setText(defaultEntity.repertory + "");
            }
        });
    }

    private void setLevelTwoData(GoodsStandard entity) {
        final StandardLevel primary = entity.descriptions.primary;
        type1.setText(primary.label);
        adapter1 = new GoodsLevel1Adapter1(mContext, primary.types);
        leve1View1.setAdapter(adapter1);
        final StandardLevel secondary = entity.descriptions.secondary;
        type2.setText(secondary.label);
        adapter2 = new GoodsLevel1Adapter2(mContext, secondary.types);
        leve1View2.setAdapter(adapter2);
//        leve11Key = t1.types.get(0);
//        level2Key = t2.types.get(0);

        final Map<String, ListGoodsDetail> goodsMap = entity.goodsIndexes;
        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                mKind.setText(s);
                leve11Key = s;
                changeSelectAdapter2(goodsMap, adapter2, secondary.types, s);

            }
        });
        adapter2.setOnLevelClickListener(new GoodsLevel1Adapter2.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                level2Key = s;
                changeSelectAdapter1(goodsMap, adapter1, primary.types, s);
            }
        });
    }

    //点击第一级，改变第二级的状态
    private void changeSelectAdapter2(Map<String, ListGoodsDetail> goodsIndexes, GoodsLevel1Adapter2 adapter2, List<String> types, String key2) {
        if (TextUtils.isEmpty(key2)) {
            defaultEntity = null;
            adapter2.setUnClickable("");
            return;
        }
        boolean hasUnclickString = false;
        for (String s : types) {
            if (goodsIndexes.get(leve11Key + "^" + s) == null) {
                hasUnclickString = true;
                adapter2.setUnClickable(s);
            }
        }
        if (!hasUnclickString) adapter2.setUnClickable("");  //没有不能点的，把标志位置空
        defaultEntity = goodsIndexes.get(leve11Key + "^" + level2Key);
        if (defaultEntity != null) {
            mRemainNumber.setText(defaultEntity.repertory + "");
            mPrice.setText(defaultEntity.salePrice);
            GlideUtils.loadImage(getContext(),defaultEntity.mainPicture, mIcon);
        }
    }

    private void changeSelectAdapter1(Map<String, ListGoodsDetail> goodsIndexes, GoodsLevel1Adapter1 adapter1, List<String> types, String key1) {
        if (TextUtils.isEmpty(key1)) {
            defaultEntity = null;
            adapter1.setUnClickable("");
            return;
        }
        boolean hasUnclickString = false;
        for (String s : types) {
            if (goodsIndexes.get(s + "^" + level2Key) == null) {
                hasUnclickString = true;
                adapter1.setUnClickable(s);
            }
        }
        if (!hasUnclickString) adapter1.setUnClickable("");
        defaultEntity = goodsIndexes.get(leve11Key + "^" + level2Key);
        if (defaultEntity != null) {
            mRemainNumber.setText(defaultEntity.repertory + "");
            mPrice.setText(defaultEntity.salePrice);
            GlideUtils.loadImage(getContext(),defaultEntity.mainPicture, mIcon);
        }
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.choose_board, null);
        setContentView(rootView);
        setOnDismissListener(this);
        ButterKnife.bind(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        leve1View1.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        leve1View2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.goods_board_add).setOnClickListener(this);
        findViewById(R.id.goods_board_reduce).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_board_add:
                if (nowNum == defaultEntity.repertory) {
                    ToastUtils.ToastMessage(mContext, mContext.getString(R.string.not_enough_goods));
                    return;
                }
                nowNum += 1;
                mNumber.setText(nowNum + "");
                break;
            case R.id.goods_board_reduce:
                if (nowNum > 1) {
                    nowNum -= 1;
                    mNumber.setText(nowNum + "");
                }
                break;
        }
    }

    private void addCartItem() {
        addCartListener.onAddCart(defaultEntity.id, nowNum);
    }

    public void setFromShoppingCar(OnSelectFinish onSelectedFinished) {
        selectFinish = onSelectedFinished;
//        buy.setVisibility(View.GONE);
        car.setVisibility(View.GONE);
    }

    public void setAddCartListener(AddCartListener addCartListener) {
        this.addCartListener = addCartListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onShowInDetail == null || defaultEntity == null) return;
        onShowInDetail.onShow(defaultEntity.standardSnapshot, nowNum);
    }

    public void setAmount(int amount) {
        mNumber.setText(amount + "");
    }

    public interface OnSelectFinish {
        void onSelected(String newId, int nowNum);
    }

    public interface AddCartListener {
        void onAddCart(String id, int nowNum);
    }

    public interface OnShowInDetail {
        void onShow(String standard, int num);
    }
}
