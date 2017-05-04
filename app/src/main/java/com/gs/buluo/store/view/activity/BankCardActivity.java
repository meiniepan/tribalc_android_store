package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.BankCardListAdapter;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.presenter.BankCardPresenter;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.view.impl.ICardView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardActivity extends BaseActivity implements ICardView {
    @Bind(R.id.card_list)
    ListView cardList;
    @Bind(R.id.card_manager)
    TextView manage;
    @Bind(R.id.card_status_layout)
    StatusLayout statusLayout;
    private BankCardListAdapter adapter;

    private boolean canDelete = false;
    private boolean isFromCash;
    private View actionView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        actionView = findViewById(R.id.card_add_card);
        isFromCash = getIntent().getBooleanExtra(Constant.CASH_FLAG, false);
        adapter = new BankCardListAdapter(this);
        cardList.setAdapter(adapter);
        getData();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(Constant.SUCCEED, TribeApplication.getInstance().getUserInfo().getAuthenticationStatus())) {
                    ToastUtils.ToastMessage(getCtx(), "您尚未进行商户认证，无法绑定银行卡");
                    return;
                }
                startActivity(new Intent(BankCardActivity.this, AddBankCardActivity.class));
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canDelete) {
                    hideDeleteView();
                } else {
                    showDeleteView();
                }
            }
        });
        if (isFromCash) {
            cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BankCard card = (BankCard) adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.BANK_CARD, card);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        statusLayout.showProgressView();
        ((BankCardPresenter) mPresenter).getCardList(TribeApplication.getInstance().getUserInfo().getId());
        actionView.setVisibility(View.GONE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData();
    }

    private void hideDeleteView() {
        adapter.hideDelete();
        canDelete = false;
        manage.setText(R.string.manage);
    }

    private void showDeleteView() {
        adapter.showDelete();
        manage.setText(R.string.finish);
        canDelete = true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_card_list;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BankCardPresenter();
    }

    @Override
    public void getCardInfoSuccess(List<BankCard> data) {
        if (data.size() == 0) {
            statusLayout.showEmptyView("您还未添加银行卡");
            return;
        }
        statusLayout.showContentView();
        adapter.setData(data);
        actionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(int res) {
        statusLayout.showErrorView(getString(res));
    }


}
