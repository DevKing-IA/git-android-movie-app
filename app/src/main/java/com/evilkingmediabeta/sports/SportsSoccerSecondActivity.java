package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.evilkingmediabeta.sports.adapter.SoccerSecondAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsSoccerSecondActivity extends AppCompatActivity {

    RecyclerView matchesRecyclerView;
    EditText etSearchMatches;
    private List<SportsModel> matchModel = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    SoccerSecondAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_soccer_second);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        TextView titleView = findViewById(R.id.matchTitleView);
        titleView.setText(title + " Matches");

        matchesRecyclerView = findViewById(R.id.matchesRecyclerView);
        etSearchMatches = findViewById(R.id.etSearchMatches);

        etSearchMatches.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsSoccerSecondActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsSoccerSecondActivity.this, etSearchMatches.getWindowToken());
                }
            }
        });

        etSearchMatches.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mAdapter != null){
                    mAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new viewMatchList(url).execute();
        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class viewMatchList extends AsyncTask<String, Void, Void> {
        String url;

        private viewMatchList(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsSoccerSecondActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                matchModel.clear();
                Document doc = Jsoup.connect(url).timeout(10000).get();
                Elements body = doc.select("div[class=td-main-content-wrap td-container-wrap]").select("div[class=td-ss-main-content]").select("div[class=td-block-row]");
                for (int i = 0; i < body.size(); i++){
                    Elements body1 = body.get(i).select("div[class=td-block-span6]");
                    for (int j = 0; j < body1.size(); j++){
                        String matchTitle = body1.get(j).select("div[class=item-details]").select("h3").select("a").text();
                        String matchTime = body1.get(j).select("div[class=item-details]").select("time").text();
                        String url = body1.get(j).select("div[class=item-details]").select("h3").select("a").attr("href");
                        SportsModel model = new SportsModel();
                        model.setTitle(matchTitle);
                        model.setTime(matchTime);
                        model.setUrl(url);
                        matchModel.add(model);
                    }
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

            mAdapter = new SoccerSecondAdapter(matchModel, SportsSoccerSecondActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(SportsSoccerSecondActivity.this, 3);
            } else {
                mLayoutManager = new GridLayoutManager(SportsSoccerSecondActivity.this, 2);
            }
            matchesRecyclerView.setLayoutManager(mLayoutManager);
            matchesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            matchesRecyclerView.invalidate();
            matchesRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }

    }
}
