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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.CricfreeAdapter;
import com.evilkingmediabeta.sports.adapter.CricfreeCategoryAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsCricfreeActivity extends AppCompatActivity {

    private List<String> scheduleList = new ArrayList<>();
    private List<String> urlNodeStrList = new ArrayList<>();
    private List<SportsModel> models = new ArrayList<>();
    private RecyclerView homeRecyclerView, categoryRecyclerView;
    private ListView homeListView;
    private LinearLayout homeLy, categoryLy;
    private ProgressDialog mProgressDialog;
    Button btnHome, btnCategory;
    EditText etSearch, etSearchCategory;
    CricfreeAdapter adapter;
    CricfreeCategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_cricfree);

        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeListView = findViewById(R.id.homeListview);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        btnHome = findViewById(R.id.btnHome);
        btnCategory = findViewById(R.id.btnCategory);

        homeLy = findViewById(R.id.homeLy);
        categoryLy = findViewById(R.id.categoryLy);

        etSearch = findViewById(R.id.etSearch);
        etSearchCategory = findViewById(R.id.etSearchCategory);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsCricfreeActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsCricfreeActivity.this, etSearch.getWindowToken());
                }
            }
        });

        etSearchCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsCricfreeActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsCricfreeActivity.this, etSearchCategory.getWindowToken());
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

        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(categoryAdapter != null){
                    categoryAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayContents(position);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeLy.setVisibility(View.VISIBLE);
                categoryLy.setVisibility(View.GONE);
                new prepareSportsData(Constant.SPORTS_CRICFREE_URL, "home").execute();
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLy.setVisibility(View.VISIBLE);
                homeLy.setVisibility(View.GONE);
                categoryRecyclerView.requestFocus();
                new prepareSportsData(Constant.SPORTS_CRICFREE_URL, "category").execute();
            }
        });

        new prepareSportsData(Constant.SPORTS_CRICFREE_URL, "home").execute();
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
            String url;
            if(tds.size() == 6){
                team = tds.get(3).select("a").text();
                url = tds.get(5).select("a").attr("href");
            } else {
                team = tds.get(3).select("a").text() + "  " + tds.get(4).text() + "  " + tds.get(5).select("a").text();
                url = tds.get(7).select("a").attr("href");
            }
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
        homeRecyclerView.setLayoutManager(mLayoutManager);
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeRecyclerView.invalidate();
        homeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {
        String url;
        String category;

        private prepareSportsData(String url, String category) {
            this.url = url;
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsCricfreeActivity.this);
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
                if(category.equals("home")){
                    Elements body = doc.select("div[class=panel_mid]").select("div[class=match_list]").select("div[class=panel_mid_body]");
                    for (int i = 0; i < body.size(); i ++){
                        String schedule = body.get(i).select("div[class=section_head]").select("h2").text();
                        String urlNodeStr = body.get(i).select("div[class=table-responsive competition_tbl]").select("table[class=table table-condensed table-striped table-hover]").toString();
                        scheduleList.add(schedule);
                        urlNodeStrList.add(urlNodeStr);
                    }
                } else {
                    Elements ulBody1 = doc.select("ul[class=nav navbar-nav]").select("li[class=active dropdown]");
                    SportsModel model1 = new SportsModel();
                    model1.setTitle(ulBody1.select("a[class=dropdown-toggle]").select("span").text());
                    model1.setLinkNodeString(ulBody1.select("ul[class=dropdown-menu]").toString());
                    models.add(model1);
                    Elements ulBody2 = doc.select("ul[class=nav navbar-nav]").select("li[class=active ]");
                    for (int i = 0; i < ulBody2.size(); i++){
                        String sportType = ulBody2.get(i).select("a").select("span").text();
                        String urlNodeStr = ulBody2.get(i).toString();
                        SportsModel model2 = new SportsModel();
                        model2.setTitle(sportType);
                        model2.setLinkNodeString(urlNodeStr);
                        models.add(model2);
                    }
                    Elements ulBody3 = doc.select("ul[class=nav navbar-nav]").select("li[class=dropdown]");
                    SportsModel model3 = new SportsModel();
                    model3.setTitle(ulBody3.select("a[class=dropdown-toggle]").select("span").text());
                    model3.setLinkNodeString(ulBody3.select("ul[class=dropdown-menu]").toString());
                    models.add(model3);
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

            if(category.equals("home")){
                ScheduleListAdapter scheduleListAdapter = new ScheduleListAdapter(SportsCricfreeActivity.this, 0, scheduleList);
                homeListView.setAdapter(scheduleListAdapter);
            } else {
                categoryAdapter = new CricfreeCategoryAdapter(models, SportsCricfreeActivity.this);
                RecyclerView.LayoutManager mLayoutManager;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    mLayoutManager = new GridLayoutManager(SportsCricfreeActivity.this, 4);
                } else {
                    mLayoutManager = new GridLayoutManager(SportsCricfreeActivity.this, 2);
                }
                categoryRecyclerView.setLayoutManager(mLayoutManager);
                categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                categoryRecyclerView.invalidate();
                categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();

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