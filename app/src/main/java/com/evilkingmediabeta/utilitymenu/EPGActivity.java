package com.evilkingmediabeta.utilitymenu;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class EPGActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BindListEPGAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<MoviesModel> epgModelList = new ArrayList<>();
    private ImageView ivUp, ivDown;
    EditText searchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_epg);
        recyclerView = findViewById(R.id.recyclerview);

        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        searchview = findViewById(R.id.etSearch);

        searchview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( hasFocus ){
                    CustomKeyboardHandler.showKeyboard(EPGActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(EPGActivity.this, searchview.getWindowToken());
                }
            }
        });

        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollBy(0, -200);
            }
        });

        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollBy(0, 200);

            }
        });

        new prepareEPGData().execute();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareEPGData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(EPGActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {

                Document doc = Jsoup.connect(Constant.EPGURL).timeout(10000).userAgent("Mozilla").get();

                //For Categories
                Elements container = doc.select("div[class=container wrapper-skin-qn testata-guidatv]");
                Elements content = container.select("section[class=page-content]");

                Elements channels = content.select("div[class=channels]");
                Elements sections = channels.select("section[class=channel channel-thumbnail]");

                for(int i=0;i<sections.size();i++)
                {
                    Elements mElementUrl = sections.get(i).select("a");
                    String urlstr = mElementUrl.attr("href");
                    String url =  urlstr.substring(1);
                    String channelName = mElementUrl.select("div[class=channel-name]").text();

                    MoviesModel moviesModel = new MoviesModel();
                    moviesModel.setUrl(Constant.EPGURL+url);
                    moviesModel.setTitle(channelName);
                    epgModelList.add(moviesModel);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute (Void result){
            // Set description into TextView
            if (mProgressDialog != null) {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }


            mAdapter = new BindListEPGAdapter(epgModelList, EPGActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(EPGActivity.this, 3);
            } else {
                mLayoutManager = new GridLayoutManager(EPGActivity.this, 2);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);
            recyclerView.requestFocus();



        }

    }
}



