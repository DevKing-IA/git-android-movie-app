package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.sports.adapter.FootballAdapter;
import com.evilkingmediabeta.sports.adapter.FootballFullMatchAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class FootballOnDemandActivity extends AppCompatActivity {

    Button homeBtn, fullBtn;
    LinearLayout homeLy;
    RecyclerView recyclerView, fullmatch_recyclerView;
    EditText search;
    ImageView prev, next;
    ProgressDialog mProgressDialog;

    FootballAdapter mAdapter;
    FootballFullMatchAdapter detailAdapter;
    List<MoviesModel> modelList = new ArrayList<>();
    List<SportsModel> fullmatchmodel_list = new ArrayList<>();
    int all_page_num;
    int current_pagenum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_football_on_demand);

        homeBtn = findViewById(R.id.homeBtn);
        fullBtn = findViewById(R.id.fullBtn);
        homeLy = findViewById(R.id.homeLy);
        search = findViewById(R.id.etSearch);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        recyclerView = findViewById(R.id.recyclerView);
        fullmatch_recyclerView = findViewById(R.id.fullmatch_recyclerview);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLy.setVisibility(View.VISIBLE);
                fullmatch_recyclerView.setVisibility(View.GONE);
                new prepareSportsData().execute();
            }
        });

        fullBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullmatch_recyclerView.setVisibility(View.VISIBLE);
                homeLy.setVisibility(View.GONE);
                new getFullMatch().execute();
                fullmatch_recyclerView.setLayoutManager(new GridLayoutManager(FootballOnDemandActivity.this, 2));
                fullmatch_recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });

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

                Document doc = Jsoup.connect(Constant.SPORTS_FOOTBALL_ON_DEMAND_URL).timeout(30000).get();
                Elements page_bar_body = doc.select("ul[class=page-numbers]").select("li");
                all_page_num = Integer.parseInt(page_bar_body.get(page_bar_body.size()-2).select("a").text());

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
            mProgressDialog = new ProgressDialog(FootballOnDemandActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                modelList.clear();
                Document document = Jsoup.connect(Constant.SPORTS_FOOTBALL_ON_DEMAND_URL + "page/" + current_pagenum).timeout(30000).get();
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
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            mAdapter = new FootballAdapter(modelList, FootballOnDemandActivity.this);
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

        }

    }

    private class getFullMatch extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(FootballOnDemandActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                fullmatchmodel_list.clear();
                Document document = Jsoup.connect(Constant.SPORTS_FOOTBALL_ON_DEMAND_URL).timeout(30000).get();
                Elements menus = document.select("ul[class=menu]").get(1).select("li").get(1).select("ul[class=sub-menu]").get(0).children();
                for (int i = 0; i < menus.size(); i++){
                    String title = menus.get(i).select("a").first().select("span").text();
                    String childNodes = menus.get(i).children().toString();
                    SportsModel model = new SportsModel();
                    model.setTitle(title);
                    model.setLinkNodeString(childNodes);
                    fullmatchmodel_list.add(model);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            detailAdapter = new FootballFullMatchAdapter(fullmatchmodel_list, FootballOnDemandActivity.this);
            fullmatch_recyclerView.setAdapter(detailAdapter);
            detailAdapter.notifyDataSetChanged();

        }

    }
}
