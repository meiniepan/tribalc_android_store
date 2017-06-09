package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.CropCircleTransformation;
import com.gs.buluo.store.utils.GlideUtils;

import java.io.File;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/6.
 */

public class PayCodeActivity extends BaseActivity {
    @Bind(R.id.qr_image)
    ImageView image;
    private Bitmap bitmap;
    private ImageView qrImage;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        bitmap = CommonUtils.createQRImage(this, TribeApplication.getInstance().getUserInfo().getId(), DensityUtils.dip2px(this, 240));
        image.setImageBitmap(bitmap);
        findViewById(R.id.qr_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap();
            }
        });

        findViewById(R.id.receive_bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), BillActivity.class);
                intent.putExtra(Constant.FACE, true);
                startActivity(intent);
            }
        });
    }

    @Bind(R.id.parent)
    RelativeLayout relativeLayout;

    private void saveBitmap() {
        final View qrView = View.inflate(this, R.layout.receive_view, null);
        qrImage = (ImageView) qrView.findViewById(R.id.download_image);
        TextView textView = (TextView) qrView.findViewById(R.id.download_text);
        textView.setText("向" + TribeApplication.getInstance().getUserInfo().getName() + "商户付款");
        String logo = TribeApplication.getInstance().getUserInfo().getLogo();
        if (logo != null) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(DensityUtils.dip2px(this, 35), DensityUtils.dip2px(this, 35)));
            Glide.with(this).load(GlideUtils.formatImageUrl(logo)).bitmapTransform(new CropCircleTransformation(this)).
                    into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            Drawable current = resource.getCurrent();
                            Bitmap qrWithLogo = createQRWithLogo(current);
                            qrImage.setImageBitmap(qrWithLogo);
                            createQR(qrView);
                        }
                    });
        } else {
            qrImage.setImageBitmap(bitmap);
            createQR(qrView);
        }
    }

    private void createQR(View view) {
        Bitmap bitmap = screenShot(view);
        if (bitmap != null) {
            File file = CommonUtils.saveBitmap2file(bitmap, "pay_code.jpg");
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
                ToastUtils.ToastMessage(getCtx(), R.string.save_success);
                bitmap.recycle();
            }
        }
    }

    private Bitmap createQRWithLogo(Drawable logo) {
        Bitmap qrCodeBitmap = null;
        Bitmap mBitmap = drawableToBitmap(logo);
        qrCodeBitmap = CommonUtils.createQRCodeWithLogo(TribeApplication.getInstance().getUserInfo().getId(), DensityUtils.dip2px(this, 240), mBitmap);
        return qrCodeBitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public final Bitmap screenShot(View view) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_code;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
    }
}
