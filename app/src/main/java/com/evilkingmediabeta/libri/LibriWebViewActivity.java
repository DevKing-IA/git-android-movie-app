package com.evilkingmediabeta.libri;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;

import io.fabric.sdk.android.Fabric;

public class LibriWebViewActivity extends AppCompatActivity {

    private String url = "http://amici.cgel.me/libri/";
    WebView myWebView;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_libri_web_view);

        myWebView = findViewById(R.id.webView);
        mProgressDialog = new ProgressDialog(this);

        //Config my WebView//
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
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

        myWebView.loadUrl(url);

        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

//                if (register.equals("NO")){

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
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

            }
        });

    }

//    private void showDialog(final String url, String userAgent, String contentDisposition, final String mimetype, long contentLength) {
//        final Dialog mydialog = new Dialog(mActivity);
//        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mydialog.setContentView(R.layout.custom_dialog);
//
//        dialogtitleText = mydialog.findViewById(R.id.dia_title);
//        dialogbrowserText = mydialog.findViewById(R.id.browser_text);
//        dialogmoreText = mydialog.findViewById(R.id.more_text);
//        dialogchbText = mydialog.findViewById(R.id.text_remem);
//        dialogcancelText = mydialog.findViewById(R.id.cancel);
//
//        dialogbrowserImg = mydialog.findViewById(R.id.browser_img);
//        dialogmoreImg = mydialog.findViewById(R.id.more_img);
//        dialogchbRem = mydialog.findViewById(R.id.chb_remem);
//
//        //Dialog fontType//
//        dialogchbRem.setChecked(false);
//
//        mydialog.show();
//        Window window = mydialog.getWindow();
//        if (window != null) {
//            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
//
//        dialogbrowserImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
//                final SharedPreferences.Editor editor = pref.edit();
//                editor.putString("DOWNLOAD_TYPE", "BROWSER");
//                editor.apply();
//                mydialog.dismiss();
//
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//
//                // Check whether there is or no Browser.//
//                PackageManager packageManager = getPackageManager();
//                List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent,
//                        PackageManager.MATCH_DEFAULT_ONLY);
//                boolean isIntentSafe = activities.size() > 0;
//                if (isIntentSafe){
//                    startActivity(browserIntent);
//                } else {
//                    Toast.makeText(mContext, "There is no Browser in your Android device...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        dialogmoreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
//                final SharedPreferences.Editor editor = pref.edit();
//                editor.putString("DOWNLOAD_TYPE", "EXTERNAL");
//                editor.apply();
//                mydialog.dismiss();
//
//                try{
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.putExtra(Intent.EXTRA_TEXT, url);
//                    intent.setType(mimetype);
//                    startActivity(intent);
//                } catch (Exception e){
//                    Toast.makeText(mContext, "There is no any External Download Manager..Please install this manager via google store!", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//
//        dialogchbRem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
//                    final SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("REGISTERED", "YES");
//                    editor.apply();
//                } else {
//                    SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
//                    final SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("REGISTERED", "NO");
//                    editor.apply();
//                }
//            }
//        });
//
//        dialogcancelText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mydialog.dismiss();
//            }
//        });
//
//    }

}