package com.gs.buluo.store.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.view.widget.LoadingDialog;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    Runnable uploadRunnable=new Runnable() {
        @Override
        public void run() {
            String fileType = "image/jpeg";
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            Bitmap newB = CommonUtils.compressBitmap(bitmap);
            final File picture = CommonUtils.saveBitmap2file(newB, "picture");
            new MainModel().uploadFile(picture, name, fileType, new Callback<UploadAccessResponse>() {
                @Override
                public void onResponse(Call<UploadAccessResponse> call, final Response<UploadAccessResponse> response) {
                    if (response.body() != null && response.body().code == 201) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                response.body().data.objectKey = "oss://" + response.body().data.objectKey;
                                putFile(response.body().data, picture, callback);
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(Call<UploadAccessResponse> call, Throwable t) {
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
        this.callback =callback;
        this.name =name;
        this.file =file;
        handler.postDelayed(uploadRunnable,100);//不等待 finish gallery 会卡顿
    }

    private void putFile(final UploadAccessResponse.UploadResponseBody data, File file, final UploadCallback callback) {
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
        void uploadSuccess(UploadAccessResponse.UploadResponseBody data);

        void uploadFail();
    }
}
