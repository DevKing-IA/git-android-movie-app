package com.evilkingmediabeta.musica;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.util.List;

import com.evilkingmediabeta.R;

public class MusicWebViewActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE = 1;
    TextView toolbarText;
    WebView myWebView;
    String uri = "https://mpgun.com/";
    Context mContext;
    Activity mActivity;
    com.pnikosis.materialishprogress.ProgressWheel progressWheel;
    ImageButton imgRef, imgBack, imgFor;
    String javascript = "";

    TextView dialogtitleText, dialogbrowserText, dialogmoreText, dialogchbText, dialogcancelText;
    ImageView dialogbrowserImg, dialogmoreImg;
    CheckBox dialogchbRem;
    String type = "EXTERNAL";
    String register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        init();

        //Config my WebView//
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient(){

           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("mpgun.com")){
                    view.loadUrl(url);
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon){
                myWebView.setVisibility(View.GONE);
                progressWheel.setVisibility(View.VISIBLE);
                super.onPageStarted(webView, url, favicon);
            }
            @Override
            public void onPageFinished(WebView webView, String url){
                super.onPageFinished(webView, url);
                progressWheel.setVisibility(View.GONE);
                myWebView.setVisibility(View.VISIBLE);
            }

        });

        myWebView.loadUrl(uri);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myWebView.canGoBack()){
                    myWebView.goBack();
                }
            }
        });

        imgFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myWebView.canGoForward()){
                    myWebView.goForward();
                }
            }
        });



        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
                type = pref.getString("DOWNLOAD_TYPE","EXTERNAL");

                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("FIRSTOPENED", MODE_PRIVATE);
                register = pref.getString("REGISTERED","NO");

                if (register.equals("NO")){
                    showDialog(url, userAgent, contentDisposition, mimetype, contentLength);
                } else {
                    if (type.equals("EXTERNAL")){
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, url);
                        intent.setType(mimetype);
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        // Check whether there is or no Browser.//
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        boolean isIntentSafe = activities.size() > 0;
                        if (isIntentSafe){
                            startActivity(browserIntent);
                        } else {
                            Toast.makeText(mContext, "There is no Browser in your Android device...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


//                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                        request.setMimeType(mimetype);
//                        request.allowScanningByMediaScanner();
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                        downloadManager.enqueue(request);
//                        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void showDialog(final String url, String userAgent, String contentDisposition, final String mimetype, long contentLength) {
        final Dialog mydialog = new Dialog(mActivity);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.custom_dialog);

        dialogtitleText = mydialog.findViewById(R.id.dia_title);
        dialogbrowserText = mydialog.findViewById(R.id.browser_text);
        dialogmoreText = mydialog.findViewById(R.id.more_text);
        dialogchbText = mydialog.findViewById(R.id.text_remem);
        dialogcancelText = mydialog.findViewById(R.id.cancel);

        dialogbrowserImg = mydialog.findViewById(R.id.browser_img);
        dialogmoreImg = mydialog.findViewById(R.id.more_img);
        dialogchbRem = mydialog.findViewById(R.id.chb_remem);

        //Dialog fontType//
        dialogchbRem.setChecked(false);

        mydialog.show();
        Window window = mydialog.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        dialogbrowserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref.edit();
                editor.putString("DOWNLOAD_TYPE", "BROWSER");
                editor.apply();
                mydialog.dismiss();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Check whether there is or no Browser.//
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe){
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(mContext, "There is no Browser in your Android device...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialogmoreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref.edit();
                editor.putString("DOWNLOAD_TYPE", "EXTERNAL");
                editor.apply();
                mydialog.dismiss();

                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.setType(mimetype);
                    startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(mContext, "There is no any External Download Manager..Please install this manager via google store!", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialogchbRem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    editor.putString("REGISTERED", "YES");
                    editor.apply();
                } else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("DOWNLOAD_TYPE", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    editor.putString("REGISTERED", "NO");
                    editor.apply();
                }
            }
        });

        dialogcancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

    }



    private void init() {
        Toolbar toolbar = findViewById(R.id.musica_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_prev);
        }
        toolbarText = findViewById(R.id.toolbar_title);

        toolbarText.setText("Scarica MP3");
//        javascript = "javascript: document.getElementById('nav').hidden = true;\n" +
//                "document.getElementById('form_text').hidden = true;\n" +
//                "document.getElementById('control_sources').style.display = 'none';\n" +
//                "document.getElementById('text').hidden = true;\n" +
//                "document.getElementById('footer').hidden = true;";

        mContext = getApplicationContext();
        mActivity = MusicWebViewActivity.this;

        myWebView = findViewById(R.id.webView);
        progressWheel = findViewById(R.id.progress_wheel);

        imgRef = findViewById(R.id.btn_ref);
        imgBack = findViewById(R.id.btn_back);
        imgFor = findViewById(R.id.btn_forward);

        imgRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.reload();
            }
        });

        // Check permission for write external storage
        checkPermission();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebView.canGoBack()) {
            this.myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void checkPermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Write external storage permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSION_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            mActivity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            } else {
                // Permission already granted
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                }else {
                    // Permission denied
                }
            }
        }
    }


}