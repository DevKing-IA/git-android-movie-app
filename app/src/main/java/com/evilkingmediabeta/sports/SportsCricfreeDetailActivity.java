package com.evilkingmediabeta.sports;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.CricfreeCategoryDetailAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class SportsCricfreeDetailActivity extends AppCompatActivity {

    RecyclerView sportTypeRecyclerView;
    CricfreeCategoryDetailAdapter mAdapter;
    private List<SportsModel> sportTypeModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_cricfree_detail);

        String tags = getIntent().getStringExtra("tags");

        sportTypeRecyclerView = findViewById(R.id.sportTypeRecyclerView);

        Document document = Jsoup.parse(tags);
        Elements elements = document.select("li");
        for (int i = 0; i < elements.size(); i++){
            String type = elements.get(i).select("a").text();
            String url = elements.get(i).select("a").attr("href");
            SportsModel model = new SportsModel();
            model.setTitle(type);
            model.setUrl(url);
            sportTypeModel.add(model);
        }

        mAdapter = new CricfreeCategoryDetailAdapter(sportTypeModel, this);
        RecyclerView.LayoutManager mLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new GridLayoutManager(this, 4);
        } else {
            mLayoutManager = new GridLayoutManager(this, 2);
        }
        sportTypeRecyclerView.setLayoutManager(mLayoutManager);
        sportTypeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sportTypeRecyclerView.invalidate();
        sportTypeRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }
}
