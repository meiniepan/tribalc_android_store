package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.utils.TribeDateUtils;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.identify_birthdayTime)
    TextView mBirthTime;
    @Bind(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @Bind(R.id.verify_name)
    EditText mName;
    @Bind(R.id.identify_sex)
    TextView mSex;
    @Bind(R.id.identify_sign)
    ImageView mSign;

    @Bind(R.id.identify_finish)
    TextView mFinish;

    private long birthday;
    private StoreInfo storeInfo = new StoreInfo();
    private StoreInfoDao dao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        dao = new StoreInfoDao();
        findViewById(R.id.identify_back).setOnClickListener(this);
        mFinish.setOnClickListener(this);
        mBirthTime.setOnClickListener(this);
        mSex.setOnClickListener(this);

//        if (storeInfo.getLegalPersonIdCardNo() != null) {
//            StoreInfo infoEntity = new StoreInfoDao().findFirst();
//            mIdCardNumber.setText(storeInfo.getLegalPersonIdCardNo());
//            mName.setText(storeInfo.getLegalPersonName());
//            switch (sensitiveEntity.getEnumStatus()){
//                case SUCCESS:
//                    mSign.setVisibility(View.VISIBLE);
//                    mSign.setImageResource(R.mipmap.identify_success);
//                    mBirthTime.setOnClickListener(null);
//                    mSex.setOnClickListener(null);
//                    mIdCardNumber.setFocusable(false);
//                    mName.setFocusable(false);
//                    mFinish.setVisibility(View.GONE);
//                    break;
//                case PROCESSING:
//                    ViewStub stub= (ViewStub) findViewById(R.id.identify_processing);
//                    View view= stub.inflate();
//                    view.findViewById(R.id.identify_processing_back).setOnClickListener(this);
//                    findViewById(R.id.identify_parent).setVisibility(View.GONE);
//                    mBirthTime.setOnClickListener(null);
//                    mSex.setOnClickListener(null);
//                    mIdCardNumber.setFocusable(false);
//                    mName.setFocusable(false);
//                    mFinish.setVisibility(View.GONE);
//                    break;
//                case FAILURE:
//                    mSign.setVisibility(View.VISIBLE);
//                    mSign.setImageResource(R.mipmap.identify_fail);
//                    mFinish.setText("重新提交认证");
//                    mFinish.setVisibility(View.VISIBLE);
//                    mFinish.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            doVerify();
//                        }
//                    });
//                    break;
//            }
//        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(VerifyActivity.this, ModifyInfoActivity.class);
        switch (v.getId()) {
            case R.id.identify_birthdayTime:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.BIRTHDAY);
                intent.putExtra(Constant.BIRTHDAY, mBirthTime.getText().toString().trim());
                startActivityForResult(intent, 201);
                break;
            case R.id.identify_finish:
                String id = mIdCardNumber.getText().toString().trim();
                if (mBirthTime.length() == 0 || mIdCardNumber.length() == 0 || mName.length() == 0 || mSex.length() == 0) {
                    ToastUtils.ToastMessage(VerifyActivity.this, R.string.not_empty);
                    return;
                }
                if (id.length() != 18 && id.length() != 16 && id.length() != 15) {
                    ToastUtils.ToastMessage(VerifyActivity.this, getString(R.string.wrong_id_number));
                    return;
                }
                break;
            case R.id.identify_back:
                finish();
            case R.id.identify_processing_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 201) {
                birthday = Long.parseLong(data.getStringExtra(Constant.BIRTHDAY));
                mBirthTime.setText(TribeDateUtils.dateFormat5(new Date(birthday)));
            }
        }
    }

}