package com.evilkingmediabeta.utilitymenu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
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
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class EPGDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BindListDetailsEPGAdpater mAdapter;
    private ProgressDialog mProgressDialog;
    private List<MoviesModel> epgModelList = new ArrayList<>();
    private ImageView ivNext,ivPrev, ivUp, ivDown;
    private LinearLayout ll_search,ll_categories;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_epg_detail);
        url = getIntent().getStringExtra("url");
        recyclerView = findViewById(R.id.recyclerview);

        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ll_search = findViewById(R.id.ll_search);
        ll_categories = findViewById(R.id.categories);

        ivPrev.setVisibility(View.GONE);
        ivUp.setVisibility(View.GONE);
        ivDown.setVisibility(View.GONE);
        ivNext.setVisibility(View.GONE);
        ll_search.setVisibility(View.GONE);
        ll_categories.setVisibility(View.GONE);
        new  prepareEPGData().execute();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareEPGData extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(EPGDetailActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(url).timeout(10000).userAgent("Mozilla").get();
                Elements main = doc.select("div[class=channel-list]");
                Elements c = main.select("div[class=channels]");
                Elements cat = c.select("div[class=programs]");
                Elements mElementUrl = cat.select("a");

                for(int i=0;i<mElementUrl.size();i++)
                {

                    String hour = mElementUrl.get(i).select("div[class=hour]").text();
                    String program_category = mElementUrl.get(i).select("div[class=program-category]").text();
                    String program_title = mElementUrl.get(i).select("div[class=program-title]").text();
                    String progarm_url = mElementUrl.get(i).absUrl("href");
                    MoviesModel moviesModel = new MoviesModel();
                    moviesModel.setTitle(program_title);
                    moviesModel.setDuration(hour);
                    moviesModel.setYear(program_category);
                    moviesModel.setUrl(progarm_url);
                    epgModelList.add(moviesModel);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            if (mProgressDialog != null) {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            mAdapter = new BindListDetailsEPGAdpater(epgModelList, EPGDetailActivity.this);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(EPGDetailActivity.this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);

        }
    }



}
