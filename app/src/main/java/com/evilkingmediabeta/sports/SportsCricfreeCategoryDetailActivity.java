package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.evilkingmediabeta.sports.adapter.CricfreeAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsCricfreeCategoryDetailActivity extends AppCompatActivity {

    private List<String> scheduleList = new ArrayList<>();
    private List<String> urlNodeStrList = new ArrayList<>();
    private List<SportsModel> models = new ArrayList<>();
    private RecyclerView categoryDetailRecyclerView;
    private ListView categoryDetailListView;
    private ProgressDialog mProgressDialog;
    EditText etSearch;
    CricfreeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_cricfree_category_detail);

        String url = getIntent().getStringExtra("url");

        categoryDetailListView = findViewById(R.id.categoryDetailListView);
        categoryDetailRecyclerView = findViewById(R.id.categoryDetailRecyclerView);

        etSearch = findViewById(R.id.etSearch);

        categoryDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayContents(position);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsCricfreeCategoryDetailActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsCricfreeCategoryDetailActivity.this, etSearch.getWindowToken());
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new prepareSportsData(url).execute();
        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private void displayContents(int pos){
        models.clear();
        Document document = Jsoup.parse(urlNodeStrList.get(pos));
        Elements trElements = document.select("tr");
        for (int i = 0; i < trElements.size(); i++){
            Elements tds = trElements.get(i).select("td");
            String time = tds.get(1).text();
            String team;
            if(tds.size() == 6){
                team = tds.get(3).select("a").text();
            } else {
                team = tds.get(3).select("a").text() + "  " + tds.get(4).text() + "  " + tds.get(5).select("a").text();
            }
            String url = tds.get(tds.size()-1).select("a").attr("href");
            SportsModel urls = new SportsModel();
            urls.setTeam1(team);
            urls.setTime(time);
            urls.setUrl(url);
            models.add(urls);
        }

        adapter = new CricfreeAdapter(models, this);
        RecyclerView.LayoutManager mLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new GridLayoutManager(this, 1);
        }
        categoryDetailRecyclerView.setLayoutManager(mLayoutManager);
        categoryDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryDetailRecyclerView.invalidate();
        categoryDetailRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {
        String url;
        String flag;

        private prepareSportsData(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsCricfreeCategoryDetailActivity.this);
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
                Elements listEle = doc.select("div[class=match_list]");
                if(listEle.size() == 0){
                    flag = "empty";
                } else {
                    flag = "not empty";
                    Elements body = doc.select("div[class=panel_mid]").select("div[class=match_list]").select("div[class=panel_mid_body]");
                    for (int i = 0; i < body.size(); i ++){
                        String schedule = body.get(i).select("div[class=section_head]").select("h2").text();
                        String urlNodeStr = body.get(i).select("div[class=table-responsive competition_tbl]").select("table[class=table table-condensed table-striped table-hover]").toString();
                        scheduleList.add(schedule);
                        urlNodeStrList.add(urlNodeStr);
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

            if(flag.equals("empty")){
                finish();
                Toast.makeText(SportsCricfreeCategoryDetailActivity.this, "This video is not available at the moment, but will be up soon.", Toast.LENGTH_LONG).show();
            } else {
                ScheduleListAdapter scheduleListAdapter = new ScheduleListAdapter(SportsCricfreeCategoryDetailActivity.this, 0, scheduleList);
                categoryDetailListView.setAdapter(scheduleListAdapter);
            }

        }

    }

    private class ScheduleListAdapter extends ArrayAdapter<String> {

        private ScheduleListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(R.layout.simple_list_item, parent, false);
            }
            String title = getItem(position);

            TextView titleTextView = v.findViewById(R.id.text_view);

            if(title != null) {
                titleTextView.setText(title);
            }

            return v;
        }
    }
}