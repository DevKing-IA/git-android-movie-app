package com.evilkingmediabeta.cartoon;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

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

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;

public class AnimeUnityDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;

    private List<SportsModel> episodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_unity_detail);

        String url = getIntent().getStringExtra("url");

        recyclerView = findViewById(R.id.recyclerView);

        new prepareData(url).execute();

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareData extends AsyncTask<String, Void, Void> {
        String mainurl;

        public prepareData(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AnimeUnityDetailActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements body = doc.select("div[role=tabpanel]");
                for (int i = 0; i < body.size(); i++){
                    Elements body1 = body.get(i).select("div[class=row text-center]").get(0).select("div[class=col-lg-1 col-md-1 col-sm-6 ep-box]");
                    for (int j = 0; j < body1.size(); j++){
                        String title = body1.get(j).select("a").text();
                        String url = Constant.ANIMEUNITY_SITE_URL + body1.get(j).select("a").attr("href");
                        SportsModel model = new SportsModel();
                        model.setTitle(title);
                        model.setUrl(url);
                        episodes.add(model);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(mProgressDialog!=null) {
                mProgressDialog.dismiss();
            }

            AnimeUnityDetailAdapter mAdapter = new AnimeUnityDetailAdapter(episodes, AnimeUnityDetailActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(AnimeUnityDetailActivity.this, 3);
            } else {
                mLayoutManager = new GridLayoutManager(AnimeUnityDetailActivity.this, 2);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
