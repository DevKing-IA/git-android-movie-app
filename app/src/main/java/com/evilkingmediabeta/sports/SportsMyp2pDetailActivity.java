package com.evilkingmediabeta.sports;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
import com.evilkingmediabeta.sports.adapter.MyP2PDetailAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class SportsMyp2pDetailActivity extends AppCompatActivity {

    private List<SportsModel> models = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_my_pp_detail);
        recyclerView = findViewById(R.id.detailRecyclerView);

        String links = getIntent().getStringExtra("links");
        Document document = Jsoup.parse(links);
        Elements linkElements = document.select("a");
        for (int i = 0; i < linkElements.size(); i++){
            String title = linkElements.get(i).select("a").text();
            String url = linkElements.get(i).select("a").attr("href");
            SportsModel subModel = new SportsModel();
            subModel.setTitle(title);
            subModel.setUrl(url);
            models.add(subModel);
        }

        MyP2PDetailAdapter adapter = new MyP2PDetailAdapter(models, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
        recyclerView.setAdapter(adapter);

        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }
}
