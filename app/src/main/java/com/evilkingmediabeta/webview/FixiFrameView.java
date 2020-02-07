package com.evilkingmediabeta.webview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FixiFrameView extends Activity {
    private ProgressDialog mProgressDialog;

    private WebView mWebview ;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mWebview  = new WebView(this);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webview, int errorCode, String description, String failingUrl) {
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl("file:///android_asset/htmls/error.html");
                    }
                });
            };

        });

        Bundle bundle = getIntent().getExtras();
        String imageLink = bundle.getString("URL");
        mWebview.loadUrl(imageLink);
        setContentView(mWebview);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }




}
