package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.camera.CameraActivity;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.galleryfinal.FunctionConfig;

/**
 * Created by hjn on 2017/1/12.
 */
public class Authentication2Activity extends BaseActivity {
    private String front;
    private String back;
    @Bind(R.id.identify_back_image)
    ImageView backImg;
    @Bind(R.id.identify_front_image)
    ImageView frontImg;
    private AuthenticationData data;
    private String mPhotoPath;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data = getIntent().getParcelableExtra(Constant.AUTH);

        findViewById(R.id.ll_identify_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(true);
            }
        });
        findViewById(R.id.ll_identify_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(false);
            }
        });
        findViewById(R.id.qualification_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (front==null||back==null)return;
                List<String> list=new ArrayList<>();
                Toast.makeText(Authentication2Activity.this, front, Toast.LENGTH_SHORT).show();
                list.add(front);
                list.add(back);
                data.idCardPicture = list;
                Intent intent = new Intent(getCtx(),Authentication3Activity.class);
                intent.putExtra(Constant.AUTH,data);
                startActivity(intent);
            }
        });
        findView(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showChoosePhoto(final boolean isFront) {
//        Intent intent = new Intent(Authentication2Activity.this, CameraActivity.class);
//        if (isFront){
//            startActivityForResult(intent, 100);
//        }else{
//            startActivityForResult(intent, 101);
//        }
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)
                .setEnableCamera(true)
                .setEnableRotate(true)
                .setEnablePreview(true)
                .build();
        ChoosePhotoPanel panel=new ChoosePhotoPanel(this, functionConfig, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                showLoadingDialog();
                uploadPic(string,isFront);
            }
        });
        panel.show();
    }

    private void uploadPic(String pic, final boolean isFront) {
        TribeUploader.getInstance().uploadFile("id", "", pic, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadResponseBody data) {
                dismissDialog();
                if (isFront){
                    frontImg.setVisibility(View.VISIBLE);
                    front = data.objectKey;
                    GlideUtils.loadImage(getCtx(),data.objectKey,frontImg);
//                    findViewById(R.id.identify_front).setVisibility(View.GONE);
                }else {
                    backImg.setVisibility(View.VISIBLE);
                    back=data.objectKey;
//                    findViewById(R.id.identify_back).setVisibility(View.GONE);
                    GlideUtils.loadImage(getCtx(),data.objectKey,backImg);
                }
            }

            @Override
            public void uploadFail() {
                dismissDialog();
                ToastUtils.ToastMessage(getCtx(),R.string.upload_fail);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","onActivityResult");
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path=extras.getString("path");
                String type=extras.getString("type");
                File file = new File(path);
                File fileFront = new File(getApplicationContext().getFilesDir(),"front.jpg");
                front = fileFront.getAbsolutePath();
                if (!fileFront.exists()){
                    try {
                        fileFront.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                copyFile(file,fileFront);
               // Toast.makeText(getApplicationContext(),fileFront.getAbsolutePath(), Toast.LENGTH_LONG).show();
                FileInputStream inStream = null;
                try {
                    inStream = new FileInputStream(fileFront);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    frontImg.setVisibility(View.VISIBLE);
                    frontImg.setImageBitmap(bitmap);
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            uploadPic(front,true);
        }
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path=extras.getString("path");
                String type=extras.getString("type");
                File file = new File(path);
                File fileBack = new File(getApplicationContext().getFilesDir(),"back.jpg");
                back = fileBack.getAbsolutePath();
                if (!fileBack.exists()){
                    try {
                        fileBack.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                copyFile(file,fileBack);
               // Toast.makeText(getApplicationContext(),fileBack.getAbsolutePath(), Toast.LENGTH_LONG).show();
                FileInputStream inStream = null;
                try {
                    inStream = new FileInputStream(fileBack);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    backImg.setVisibility(View.VISIBLE);
                    backImg.setImageBitmap(bitmap);
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            uploadPic(back,false);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qualification;
    }

    public void copyFile(File sourcefile, File targetFile) {
        FileInputStream input = null;
        BufferedInputStream inbuff = null;
        FileOutputStream out = null;
        BufferedOutputStream outbuff = null;

        try {

            input = new FileInputStream(sourcefile);
            inbuff = new BufferedInputStream(input);

            out = new FileOutputStream(targetFile);
            outbuff = new BufferedOutputStream(out);

            byte[] b = new byte[1024 * 5];
            int len = 0;
            while ((len = inbuff.read(b)) != -1) {
                outbuff.write(b, 0, len);
            }

            outbuff.flush();
        } catch (Exception ex) {

        } finally {
            try {

                if (inbuff != null)
                    inbuff.close();
                if (outbuff != null)
                    outbuff.close();
                if (out != null)
                    out.close();
                if (input != null)
                    input.close();
            } catch (Exception ex) {

            }
        }
    }
}
