package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.sports.adapter.FootballDetailAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class FootballOnDemandDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog mProgressDialog;

    FootballDetailAdapter mAdapter;
    List<SportsModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_football_on_demand_detail);

        recyclerView = findViewById(R.id.detail_list_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new prepareSportsData(getIntent().getStringExtra("url")).execute();
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {

        String url;

        private prepareSportsData(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(FootballOnDemandDetailActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                modelList.clear();
                Document doc = Jsoup.connect(url).timeout(30000).get();
                Element active_body = doc.select("div[class=tab-title active]").get(0);
                String detail_url = url + active_body.select("a").attr("href");
                String title = active_body.select("a").text();
                SportsModel model = new SportsModel();
                model.setUrl(detail_url);
                model.setTitle(title);
                modelList.add(model);
                Elements body = doc.select("div[class=tab-title]");
                for (int i = 0; i < body.size(); i++){
                    String detail_url1 = url + body.get(i).select("a").attr("href");
                    String title1 = body.get(i).select("a").text();
                    SportsModel model1 = new SportsModel();
                    model1.setUrl(detail_url1);
                    model1.setTitle(title1);
                    modelList.add(model1);
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

            mAdapter = new FootballDetailAdapter(modelList, FootballOnDemandDetailActivity.this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    }
}
