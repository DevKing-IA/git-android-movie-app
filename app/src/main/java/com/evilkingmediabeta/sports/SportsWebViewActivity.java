package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;

import io.fabric.sdk.android.Fabric;

public class SportsWebViewActivity extends AppCompatActivity {
    String webUrl, flag;
    ProgressDialog mProgressDialog;
    WebView myWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_webview);
        myWebView = findViewById(R.id.webView);
        mProgressDialog = new ProgressDialog(this);

        flag = getIntent().getStringExtra("flag");
        webUrl = getIntent().getStringExtra("web_url");

        //        if(flag.equals("ace")) webUrl = "acestream://"+webUrl;
        //        else if(flag.equals("xmtv")) webUrl = "xmtv://"+webUrl;
        //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
        //        startActivity(intent);

        //Config my WebView//
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.setInitialScale(0);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon){
                mProgressDialog.setMessage("Loading..");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                super.onPageStarted(webView, url, favicon);
            }
            @Override
            public void onPageFinished(WebView webView, String url){
                super.onPageFinished(webView, url);
                mProgressDialog.dismiss();
            }

        });

        myWebView.loadUrl(webUrl);


//        myWebView.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//
////                if (register.equals("NO")){
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//                } else {
//                    if (type.equals("EXTERNAL")){
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.putExtra(Intent.EXTRA_TEXT, url);
//                        intent.setType(mimetype);
//                        startActivity(intent);
//                    } else {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//
//                        // Check whether there is or no Browser.//
//                        PackageManager packageManager = getPackageManager();
//                        List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent,
//                                PackageManager.MATCH_DEFAULT_ONLY);
//                        boolean isIntentSafe = activities.size() > 0;
//                        if (isIntentSafe){
//                            startActivity(browserIntent);
//                        } else {
//                            Toast.makeText(LibriWebViewActivity.this, "There is no Browser in your Android device...", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//            }
//        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mProgressDialog.dismiss();
            finish();
            if (myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        super.onPause();
    }
}

