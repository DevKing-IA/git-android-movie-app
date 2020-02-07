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
import com.evilkingmediabeta.sports.adapter.SoccerMainAdapter;
import com.evilkingmediabeta.sports.adapter.SoccerSecondAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsSoccerActivity extends AppCompatActivity {
    private RecyclerView highlightRecyclerView, latestRecyclerView;
    ListView highlightListView;
    private List<SportsModel> highlightModel = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    List<String> highlightList = new ArrayList<>();
    List<String> subhighlightNodeList = new ArrayList<>();
    SoccerMainAdapter mainAdapter;
    SoccerSecondAdapter mAdapter;
    Button btnLatest, btnOld;
    EditText etSearchLatest;
    LinearLayout latestLy, oldLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_soccer);

        highlightRecyclerView = findViewById(R.id.highlightRecyclerView);
        latestRecyclerView = findViewById(R.id.latestRecyclerView);
        highlightListView = findViewById(R.id.highlightListView);

        latestLy = findViewById(R.id.latestLy);
        oldLy = findViewById(R.id.oldLy);

        btnLatest = findViewById(R.id.btnLatest);
        btnOld = findViewById(R.id.btnOld);
        etSearchLatest = findViewById(R.id.etSearchLatest);

        etSearchLatest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsSoccerActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsSoccerActivity.this, etSearchLatest.getWindowToken());
                }
            }
        });

        etSearchLatest.addTextChangedListener(new TextWatcher() {
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

        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestLy.setVisibility(View.VISIBLE);
                oldLy.setVisibility(View.GONE);
                new prepareSportsData(Constant.SPORTS_SOCCER_URL, "latest").execute();
            }
        });

        btnOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestLy.setVisibility(View.GONE);
                oldLy.setVisibility(View.VISIBLE);
                new prepareSportsData(Constant.SPORTS_SOCCER_URL, "old").execute();
            }
        });

        highlightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayContents(position);
            }
        });

        new prepareSportsData(Constant.SPORTS_SOCCER_URL, "latest").execute();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private void displayContents(int pos){
        highlightModel.clear();
        Document document = Jsoup.parse(subhighlightNodeList.get(pos));
        Elements liElements = document.select("li");
        for (int i = 0; i < liElements.size(); i++){
            String title = liElements.get(i).select("a").text();
            String url = liElements.get(i).select("a").attr("href");
            SportsModel model = new SportsModel();
            model.setTitle(title);
            model.setUrl(url);
            highlightModel.add(model);
        }

        mainAdapter = new SoccerMainAdapter(highlightModel, this);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(this, 1);
        highlightRecyclerView.setLayoutManager(mLayoutManager);
        highlightRecyclerView.setItemAnimator(new DefaultItemAnimator());
        highlightRecyclerView.invalidate();
        highlightRecyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
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
            mProgressDialog = new ProgressDialog(SportsSoccerActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                highlightModel.clear();
                Document doc = Jsoup.connect(url).timeout(60000).get();
                if(category.equals("latest")) {
                    Elements body = doc.select("div[class=td_block_inner td-column-2]").select("div[class=td-block-row]");
                    for (int i = 0; i < body.size(); i++){
                        Elements body1 = body.get(i).select("div[class=td-block-span4]");
                        for (int j = 0; j < body1.size(); j++){
                            String matchTitle = body1.get(j).select("div[class=td_module_mx4 td_module_wrap td-animation-stack]").select("h3").select("a").text();
                            String matchTime = "";
                            String url = body1.get(j).select("div[class=td_module_mx4 td_module_wrap td-animation-stack]").select("h3").select("a").attr("href");
                            SportsModel model = new SportsModel();
                            model.setTitle(matchTitle);
                            model.setTime(matchTime);
                            model.setUrl(url);
                            highlightModel.add(model);
                        }
                    }
                } else {
                    String bodyStr = doc.select("ul[id=menu-main-menu-1]").select("li").get(0).select("ul[class=sub-menu]").get(0).select("li").toString();
                    Document doc1 = Jsoup.parse(bodyStr);
                    Elements body = doc1.getElementsByAttributeValueContaining("class", "menu-item-has-children");
                    for (int i = 0; i < body.size(); i++){
                        String league = body.get(i).select("a").get(0).text();
                        String ulTagStr = body.get(i).select("ul").toString();
                        highlightList.add(league);
                        subhighlightNodeList.add(ulTagStr);
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

            if(category.equals("latest")){
                mAdapter = new SoccerSecondAdapter(highlightModel, SportsSoccerActivity.this);
                RecyclerView.LayoutManager mLayoutManager;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    mLayoutManager = new GridLayoutManager(SportsSoccerActivity.this, 3);
                } else {
                    mLayoutManager = new GridLayoutManager(SportsSoccerActivity.this, 2);
                }
                latestRecyclerView.setLayoutManager(mLayoutManager);
                latestRecyclerView.setItemAnimator(new DefaultItemAnimator());
                latestRecyclerView.invalidate();
                latestRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                ListAdapter listAdapter = new ListAdapter(SportsSoccerActivity.this, 0, highlightList);
                highlightListView.setAdapter(listAdapter);
            }

        }

    }

    private class ListAdapter extends ArrayAdapter<String> {

        private ListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
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
