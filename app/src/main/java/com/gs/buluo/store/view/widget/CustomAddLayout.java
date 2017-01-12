package com.gs.buluo.store.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hjn on 2017/1/11.
 */

public class CustomAddLayout extends ViewGroup {
    private int mCellWidth;
    private int mCellHeight;

    public CustomAddLayout(Context context) {
        this(context,null);
    }

    public CustomAddLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomAddLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmCellWidth(int w) {
        mCellWidth = w;
        requestLayout();
    }

    public void setmCellHeight(int h) {
        mCellHeight = h;
        requestLayout();
    }

    /**
     * 控制子控件的换行
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cellWidth = mCellWidth;
        int cellHeight = mCellHeight;
        if (cellHeight==0||cellWidth==0){
            return;
        }
        int columns = (r - l) / cellWidth;
        if (columns < 0) {
            columns = 1;
        }
        int x = 0;
        int y = 0;
        int i = 0;
        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            final View childView = getChildAt(j);
            // 获取子控件Child的宽高
            int w = childView.getMeasuredWidth();
            int h = childView.getMeasuredHeight();
            // 计算子控件的顶点坐标
            int left = x + ((cellWidth - w) / 2);
            int top = y + ((cellHeight - h) / 2);
            // int left = x;
            // int top = y;
            // 布局子控件
            childView.layout(20*(j%3)+left, top, 20*(j%3)+left + w, top + h);

            if (i >= (columns - 1)) {
                i = 0;
                x = 0;
                y += cellHeight+20;
            } else {
                i++;
                x += cellWidth;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
        // 记录ViewGroup中Child的总个数
        int count = getChildCount();
        // 设置子空间Child的宽高
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            childView.measure(cellWidthSpec, cellHeightSpec);
        }
        int c = count/3 +1;
        setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
                resolveSize(c*mCellHeight, heightMeasureSpec));
    }

}
