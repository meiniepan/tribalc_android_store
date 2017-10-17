package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;

import butterknife.BindView;

/**
 * Created by hjn on 2016/12/28.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra(Constant.WEB_URL);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLightTouchEnabled(true);
        if (url==null){
            webView.loadUrl(Constant.Base.BASE+"page.html");
        }else {
            webView.loadUrl(url);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_protocol;
    }
}
