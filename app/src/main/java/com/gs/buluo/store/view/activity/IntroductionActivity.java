package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.GoodNewDetailAdapter;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by admin on 2017/2/2.
 */
public class IntroductionActivity extends BaseActivity {
    @Bind(R.id.store_introduction_text)
    TextView etIntro;
    @Bind(R.id.introduction_title)
    TextView tvTitle;
    @Bind(R.id.add_goods_detail_list)
    ListView listView;
    private ArrayList<String> intro = new ArrayList<>();
    private GoodNewDetailAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<String> list = getIntent().getStringArrayListExtra(Constant.ForIntent.INTRODUCTION);
        if (list != null) intro.addAll(list);
        adapter = new GoodNewDetailAdapter(getCtx(), intro);
        listView.setAdapter(adapter);
        findView(R.id.store_introduct_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Constant.ForIntent.INTRODUCTION, intro);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.add_goods_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });
    }

    private void choosePic() {
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(string, options);
                float width = Float.parseFloat(options.outWidth+".0");
                final String scale = options.outHeight / width+"";
                showLoadingDialog();
                TribeUploader.getInstance().uploadFile("detail" + intro.size(), "", string, new TribeUploader.UploadCallback() {
                    @Override
                    public void uploadSuccess(UploadResponseBody data) {
                        dismissDialog();
                        intro.add(data.objectKey + "?scale=" + scale.substring(0,4));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void uploadFail() {
                        dismissDialog();
                        ToastUtils.ToastMessage(getCtx(), R.string.update_fail);
                    }
                });
            }
        });
        panel.show();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_introduction;
    }
}
