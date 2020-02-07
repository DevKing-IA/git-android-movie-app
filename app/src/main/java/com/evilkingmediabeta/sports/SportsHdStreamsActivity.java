package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.BindListSports1Adapter;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsHdStreamsActivity extends AppCompatActivity {
    private RecyclerView sportScheduleRecyclerView;
    public static RecyclerView sportScheduleDetailRecyclerView;
    private BindListSports1Adapter mAdapter;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> dateAndtimeList = new ArrayList<>();
    private ArrayList<String> detailList = new ArrayList<>();
    public static EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sport_stream);
        sportScheduleRecyclerView = findViewById(R.id.sportScheduleRecyclerView);
        sportScheduleDetailRecyclerView = findViewById(R.id.sportScheduleDetailRecyclerView);
        etSearch = findViewById(R.id.etSearch);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsHdStreamsActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsHdStreamsActivity.this, etSearch.getWindowToken());
                }
            }
        });

        new prepareSportsData().execute();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsHdStreamsActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(Constant.SPORTSURL1).timeout(10000).get();

                Elements container = doc.select("div[class=container]");
                Elements content = container.select("div[class=page-content]");

                Elements h1 = content.select("h1");
                Elements h2 = content.select("h2");
                Elements p = content.select("p");
                ArrayList<String> dateArrayList = new ArrayList<>();
                ArrayList<String> timeArrayList = new ArrayList<>();

                for(int i=0;i<h1.size();i++)
                {
                    if(i > 0) {
                        dateArrayList.add(h1.get(i).text());
                    }
                }
                for(int i=0;i<h2.size();i++)
                {
                    timeArrayList.add(h2.get(i).text());

                }

                for (int i = 0; i < dateArrayList.size(); i++){
                    dateAndtimeList.add(dateArrayList.get(i) + "," + timeArrayList.get(i));
                }

                for(int i = 0;i<dateAndtimeList.size();i++)
                {
                    detailList.add(p.get(i).toString());
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
                    mProgressDialog.dismiss();
                }
                mAdapter = new BindListSports1Adapter(dateAndtimeList, SportsHdStreamsActivity.this,  detailList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SportsHdStreamsActivity.this, 1);
                sportScheduleRecyclerView.setLayoutManager(mLayoutManager);
                sportScheduleRecyclerView.setItemAnimator(new DefaultItemAnimator());
                sportScheduleRecyclerView.invalidate();
                sportScheduleRecyclerView.setAdapter(mAdapter);

            }

        }
}

