package com.gs.buluo.store.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.utils.CommonUtils;

import org.xutils.common.util.MD5;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class TribeUploader {
    private static TribeUploader uploader;
    private UploadCallback callback;
    private Handler handler = new Handler();
    private String name;
    private String file;

    private TribeUploader() {
    }

    public static TribeUploader getInstance() {
        if (uploader == null) {
            uploader = new TribeUploader();
        }
        return uploader;
    }

    private Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            String fileType = "image/jpeg";
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            if (bitmap == null) return;//防止空指针崩溃
            Bitmap newB = CommonUtils.compressBitmap(bitmap);
            final File picture = CommonUtils.saveBitmap2file(newB, "picture");

            UploadAccessBody body = new UploadAccessBody();
            body.key = name;
            body.contentType = fileType;
            body.contentMD5 = MD5.md5(file);
//            body.contentMD5 = "98d8826e6308556a4a0ed87e265e2573";
            TribeRetrofit.getInstance().createApi(MainApis.class).
                    getUploadUrl(TribeApplication.getInstance().getUserInfo().getId(), body)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseSubscriber<BaseResponse<UploadResponseBody>>(false) {
                        @Override
                        public void onNext(BaseResponse<UploadResponseBody> response) {
                            response.data.objectKey = "oss://" + response.data.objectKey;
                            putFile(response.data, picture, callback);
                        }

                        @Override
                        public void onError(Throwable e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.uploadFail();
                                }
                            });
                        }
                    });
        }
    };

    public void uploadFile(String name, String fileType, final String file, final UploadCallback callback) {
        this.callback = callback;
        this.name = name;
        this.file = file;
        handler.postDelayed(uploadRunnable, 100);//不等待 finish gallery 会卡顿
    }

    private void putFile(final UploadResponseBody data, File file, final UploadCallback callback) {
        try {
            URL url = new URL(data.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "image/jpeg");
//            conn.setRequestProperty("Content-MD5", MD5.md5(file));
            conn.connect();

            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.flush();
                int res = conn.getResponseCode();
                if (res == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.uploadSuccess(data);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.uploadFail();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.removeCallbacks(uploadRunnable);
    }

    public interface UploadCallback {
        void uploadSuccess(UploadResponseBody data);

        void uploadFail();
    }
}
