package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.evilkingmediabeta.sports.adapter.LiveStreamAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class SportsTopLiveActivity extends AppCompatActivity {

    private List<SportsModel> models = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    ImageView ivUp, ivDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_top_live);
        recyclerView = findViewById(R.id.recyclerview);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);

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

        new prepareSportsData(Constant.SPORTS_HULK_STREAM_URL).execute();
        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {
        String url;

        private prepareSportsData(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsTopLiveActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                models.clear();
                Document doc = Jsoup.connect(url).timeout(20000).get();
                Elements body = doc.select("ul[class=nav nav-pills flex-column]").select("li");
                for (int i = 1; i < body.size(); i ++){
//                    String team = body.get(i).select("td").get(2).select("a").text();
//                    String time = body.get(i).select("td").get(3).text();
//                    String urlNodeStr = body.get(i).select("td").get(2).select("a").attr("onclick");
//                    SportsModel myp2pModel = new SportsModel();
//                    myp2pModel.setTeam1(team);
//                    myp2pModel.setTime(time);
//                    myp2pModel.setLinkNodeString(urlNodeStr);
//                    models.add(myp2pModel);
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

            LiveStreamAdapter adapter = new LiveStreamAdapter(models, SportsTopLiveActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(SportsTopLiveActivity.this, 2);
            } else {
                mLayoutManager = new GridLayoutManager(SportsTopLiveActivity.this, 1);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(adapter);

        }

    }
}
