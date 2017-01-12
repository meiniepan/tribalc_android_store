package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.SelfPresenter;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.view.impl.ISelfView;
import com.gs.buluo.store.view.widget.panel.ModifyInfoPanel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;



/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.tv_address)
    TextView mAddress;
    @Bind(R.id.tv_detail_address)
    TextView mDetailAddress;
    @Bind(R.id.self_iv_head)
    SimpleDraweeView header;
    @Bind(R.id.tv_nickname)
    TextView mName;
    @Bind(R.id.tv_number)
    TextView mPhone;

    Context mCtx;
    public final int addressCode = 200;
    private ModifyInfoPanel panel;
    private StoreInfo userInfo;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.ll_head).setOnClickListener(this);
        findViewById(R.id.ll_address).setOnClickListener(this);
        findViewById(R.id.ll_detail_address).setOnClickListener(this);
        findViewById(R.id.ll_number).setOnClickListener(this);
        findViewById(R.id.ll_nickname).setOnClickListener(this);
        findViewById(R.id.self_back).setOnClickListener(this);

        userInfo = TribeApplication.getInstance().getUserInfo();
        mCtx = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (null != userInfo) {
            FresoUtils.loadImage(userInfo.getLogo(), header);
            mName.setText(userInfo.getNickname());
            mPhone.setText(userInfo.getPhone());
//            mAddress.setText(userInfo.getArea());
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_self;
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(mCtx,ModifyInfoActivity.class);
        switch (view.getId()) {
            case R.id.ll_head:
                intent.setClass(mCtx,PhotoActivity.class);
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE,"logo");
                startActivity(intent);
                break;
            case R.id.ll_nickname:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.NICKNAME);
                startActivityForResult(intent,201);
                break;
            case R.id.ll_number:
                startActivity(new Intent(this, PhoneVerifyActivity.class));
                break;
            case R.id.ll_address:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.ADDRESS);
                startActivityForResult(intent,205);
                break;
            case R.id.ll_detail_address:
                startActivityForResult(new Intent(this, AddressListActivity.class), addressCode);
                break;
            case R.id.self_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 201:
                    mName.setText(data.getStringExtra(Constant.NICKNAME));
                    break;
                case 205:
                    mAddress.setText(data.getStringExtra(Constant.ADDRESS));
                    break;
            }
        }
    }
}
