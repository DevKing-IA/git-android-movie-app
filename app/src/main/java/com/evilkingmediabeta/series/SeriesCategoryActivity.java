package com.evilkingmediabeta.series;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;

public class SeriesCategoryActivity extends AppCompatActivity {

    LinearLayout rlMovies, rlMovies1, rlMovies2, rlMovies3, rlMovies4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_category);

        rlMovies = findViewById(R.id.rlMovies);
        rlMovies1 = findViewById(R.id.rlMovies1);
        rlMovies2 = findViewById(R.id.rlMovies2);
        rlMovies3 = findViewById(R.id.rlListeIPTV);
        rlMovies4 = findViewById(R.id.rlMovies4);

        rlMovies4.setVisibility(View.GONE);
        rlMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeriesCategoryActivity.this, SeriesActivityServer4.class);
                startActivity(i);
            }
        });
        rlMovies1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeriesCategoryActivity.this, SeriesActivityServer2.class);
                startActivity(i);
            }
        });

        rlMovies2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeriesCategoryActivity.this, SeriesActivityServer3.class);
                startActivity(i);
            }
        });

        rlMovies3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeriesCategoryActivity.this, SeriesActivityServer1.class);
                startActivity(i);
            }
        });

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }
}
