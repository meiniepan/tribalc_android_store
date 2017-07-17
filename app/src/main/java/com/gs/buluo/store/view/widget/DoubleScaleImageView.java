package com.gs.buluo.store.view.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

/**
 * Created by hjn on 2017/1/6.
 */

public class DoubleScaleImageView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mOnce;
    private boolean mSetOnce ;

    /**
     * 初始化时缩放的值
     */
    private float mInitScale;

    /**
     * 双击放大值到达的值
     */
    private float mMidScale;

    /**
     * 放大的最大值
     */
    private float mMaxScale;

    private Matrix mScaleMatrix;

    /**
     * 捕获用户多指触控时缩放的比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    // **********自由移动的变量***********
    /**
     * 记录上一次多点触控的数量
     */
    private int mLastPointerCount;

    private float mLastX;
    private float mLastY;
    private float centreX;
    private float centreY;

    private int mTouchSlop;
    private boolean isCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    // *********双击放大与缩小*********
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;

    private boolean isDisallowInterceptTouchEvent = false;

    private onLongClick mOnLongClick;
    private onClick mOnClick;

    public interface onLongClick {
        void onLongClick();
    }
    public interface onClick {
        void onClick();
    }
    public void setOnLongClick(onLongClick longClick){
        mOnLongClick = longClick;
    }
    public void setClick(onClick click){
        mOnClick = click;
    }


    public DoubleScaleImageView(Context context) {
        this(context, null);
    }

    public DoubleScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // init
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed( MotionEvent e ) {
                        if ( null != mOnClick ) {
                            mOnClick.onClick();
                        }
                        return super.onSingleTapConfirmed( e );
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        if ( null != mOnLongClick ) {
                            mOnLongClick.onLongClick();
                        }
                        super.onLongPress(e);
                    }
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if (isAutoScale) {
                            return true;
                        }

                        float x = e.getX();
                        float y = e.getY();

                        if (getScale() < mMidScale) {
//                                post(new AutoScaleRunnable(mMidScale, x, y));
                            zoomView(mMidScale, x, y);
                            isDisallowInterceptTouchEvent = true;
//                                isAutoScale = true;
                        } else {
//                                post(new AutoScaleRunnable(mInitScale, x, y));
                            zoomView(mInitScale, x, y);
                            isDisallowInterceptTouchEvent = false;
//                                isAutoScale = true;
                        }
                        return true;
                    }
                });
    }

    private void zoomView(float mTargetScale, float x, float y){

        float currentScale = getScale();

        //设置为我们的目标值
        float scale = mTargetScale/currentScale;
        mScaleMatrix.postScale(scale, scale, x, y);
        checkBorderAndCenterWhenScale();
        setImageMatrix(mScaleMatrix);

        isAutoScale = false;
    }

    /**
     * 获取ImageView加载完成的图片
     */
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            // 得到控件的宽和高
            int width = getWidth();
            int height = getHeight();

            // 得到我们的图片，以及宽和高
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int dh = drawable.getIntrinsicHeight();
            int dw = drawable.getIntrinsicWidth();

            float scale = 1.0f;

            // 图片的宽度大于控件的宽度，图片的高度小于空间的高度，我们将其缩小
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            // 图片的宽度小于控件的宽度，图片的高度大于空间的高度，我们将其缩小
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }

            // 缩小值
            if (dw > width && dh > height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            // 放大值
            if (dw < width && dh < height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            /**
             * 得到了初始化时缩放的比例
             */
            mInitScale = scale;
            mMaxScale = mInitScale * 2;
            mMidScale = mInitScale * 2;
            centreX = width / 2;
            centreY = height / 2;

            // 将图片移动至控件的中间
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, centreX,
                    centreY);
            setImageMatrix(mScaleMatrix);

            mOnce = true;

            if(mSetOnce){
                restoreView();
                mSetOnce = false;
            }
        }
    }

    public void setmOnce(boolean one){
        this.mOnce = one;
        mSetOnce = true;
    }
    /**
     * 注册OnGlobalLayoutListener这个接口
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 取消OnGlobalLayoutListener这个接口
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    // 缩放区间时initScale maxScale
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }

        // 缩放范围的控制
        if ((scale < mMaxScale && scaleFactor > 1.0f)
                || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }

            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }

            // 缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /**
     * 获得图片放大缩小以后的宽和高，以及left，right，top，bottom
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 在缩放的时候进行边界以及我们的位置的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 缩放时进行边界检测，防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
                isDisallowInterceptTouchEvent = false;
            }else {
                isDisallowInterceptTouchEvent = true;

            }
            if (rectF.right <= width) {
                deltaX = width - rectF.right;
                isDisallowInterceptTouchEvent = false;
            }else {
                isDisallowInterceptTouchEvent = true;

            }
        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }

        /**
         * 如果宽度或高度小于空间的宽或者高，则让其居中
         */
        if (rectF.width() <= width) {
            deltaX = width / 2f - rectF.right + rectF.width() / 2f;
            isDisallowInterceptTouchEvent = false;
        }else {
            isDisallowInterceptTouchEvent = true;
        }

        if (rectF.height() < height) {
            deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        // 拿到多点触控的数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {

//                case MotionEvent.ACTION_DOWN:
//                    if (rectF.width()>getWidth() +0.01|| rectF.height()>getHeight()+0.01) {
//                        if(getParent()!= null)
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                    break;

            case MotionEvent.ACTION_MOVE:
                if(getParent() != null){
                    getParent().requestDisallowInterceptTouchEvent(isDisallowInterceptTouchEvent);
                }
//                    if (rectF.width()>getWidth()+0.01 || rectF.height()>getHeight()+0.01) {
//                        if(getParent()!= null)
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                    }
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 如果宽度小于控件宽度，不允许横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        // 如果高度小于控件高度，不允许纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);

                        checkBorderWhenTranslate();

                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;

            default:
                break;
        }

        return true;
    }

    public void restoreView(){
        zoomView(mInitScale, centreX, centreY);
        isDisallowInterceptTouchEvent = false;
    }

    /**
     * 当移动时进行边界检查
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int heigth = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < heigth && isCheckTopAndBottom) {
            deltaY = heigth - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        if(rectF.left > 0 || rectF.right < width && isCheckLeftAndRight){
            isDisallowInterceptTouchEvent = false;
        }else {
            isDisallowInterceptTouchEvent = true;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 判断是否是move
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

}
