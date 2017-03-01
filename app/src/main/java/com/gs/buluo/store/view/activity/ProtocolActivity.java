package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/28.
 */

public class ProtocolActivity extends BaseActivity {
    @Bind(R.id.web_view)
    WebView webView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        webView.loadUrl(Constant.Base.BASE+"page.html");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_protocol;
    }
}
