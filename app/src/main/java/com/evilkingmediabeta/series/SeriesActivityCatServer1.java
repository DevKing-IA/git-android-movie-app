package com.evilkingmediabeta.series;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListSeries1Adapter;


public class SeriesActivityCatServer1 extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BindListSeries1Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_series_category);
        recyclerView = findViewById(R.id.recyclerview);
        arrayList.add("Serie-tv-streaming");
        arrayList.add("Serie TV Americane");
        arrayList.add("Serie TV Italiane");
        arrayList.add("avventura");
        arrayList.add("azione");
        arrayList.add("biografico");
        arrayList.add("classici");
        arrayList.add("comico");
        arrayList.add("commedia");
        arrayList.add("demenziale");
        arrayList.add("drama");
        arrayList.add("fantascienza");
        arrayList.add("fantasy");
        arrayList.add("giallo");
        arrayList.add("guerra");
        arrayList.add("horror");
        arrayList.add("legal Drama");
        arrayList.add("medical Drama");
        arrayList.add("musical");
        arrayList.add("noir");
        arrayList.add("poliziesco");
        arrayList.add("prison Drama");
        arrayList.add("sentimentale");
        arrayList.add("storico");
        arrayList.add("teen Drama");
        arrayList.add("thriller");
        arrayList.add("western");

        mAdapter = new BindListSeries1Adapter(arrayList, SeriesActivityCatServer1.this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer1.this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
        recyclerView.setAdapter(mAdapter);
    }

}
