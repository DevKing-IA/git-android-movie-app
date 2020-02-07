package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.sports.adapter.FootballFullMatchAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class FootballFullMatchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog mProgressDialog;

    FootballFullMatchAdapter detailAdapter;
    List<SportsModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_football_full_match);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getFullMatch(getIntent().getStringExtra("childNodes"));
    }

    private void getFullMatch(String childNodes){
        Document doc = Jsoup.parse(childNodes);
        Elements body = doc.select("ul").select("li");
        for (int i = 0; i < body.size(); i++){
            String title = body.get(i).select("a").select("span").text();
            String nodes = body.get(i).children().toString();
            SportsModel model = new SportsModel();
            model.setTitle(title);
            model.setLinkNodeString(nodes);
            modelList.add(model);
        }

        detailAdapter = new FootballFullMatchAdapter(modelList, this);
        recyclerView.setAdapter(detailAdapter);
        detailAdapter.notifyDataSetChanged();
    }
}
