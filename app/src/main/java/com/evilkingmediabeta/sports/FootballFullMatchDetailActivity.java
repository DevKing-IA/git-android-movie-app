package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.sports.adapter.FootballAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class FootballFullMatchDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search;
    ImageView prev, next;
    ProgressDialog mProgressDialog;

    FootballAdapter mAdapter;
    List<MoviesModel> modelList = new ArrayList<>();
    int all_page_num;
    int current_pagenum = 1;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_football_full_match_detail);

        url = getIntent().getStringExtra("url");

        search = findViewById(R.id.etSearch);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        prev.setVisibility(View.GONE);
        new getAllPageCount().execute();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_pagenum --;
                new prepareSportsData().execute();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_pagenum ++;
                new prepareSportsData().execute();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
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

        new prepareSportsData().execute();
    }

    private class getAllPageCount extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(url).timeout(30000).get();
                Elements page_bar_body = doc.select("ul[class=page-numbers]").select("li");
                if (page_bar_body.size() == 0) {
                    all_page_num = 1;
                } else {
                    all_page_num = Integer.parseInt(page_bar_body.get(page_bar_body.size()-2).select("a").text());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(FootballFullMatchDetailActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                modelList.clear();
                Document document = Jsoup.connect(url + "page/" + current_pagenum).timeout(30000).get();
                Elements body = document.select("article");
                for (int i = 0; i < body.size(); i++){
                    String url = body.get(i).select("div[class=content-thumbnail]").select("a").attr("href");
                    String image = body.get(i).select("div[class=content-thumbnail]").select("img").attr("src");
                    String title = body.get(i).select("div[class=content-thumbnail]").select("img").attr("title");
                    MoviesModel model = new MoviesModel();
                    model.setUrl(url);
                    model.setTitle(title);
                    model.setImage(image);
                    modelList.add(model);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mProgressDialog != null){
                mProgressDialog.dismiss();
            }

            mAdapter = new FootballAdapter(modelList, FootballFullMatchDetailActivity.this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            if (current_pagenum == 1){
                prev.setVisibility(View.GONE);
            } else if (current_pagenum < all_page_num){
                prev.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            } else if (current_pagenum == all_page_num){
                next.setVisibility(View.GONE);
            }
            if (all_page_num == 1){
                prev.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}
