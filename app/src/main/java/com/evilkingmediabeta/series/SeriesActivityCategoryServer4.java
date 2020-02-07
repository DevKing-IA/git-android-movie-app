package com.evilkingmediabeta.series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListSeriesCategory4Adapter;
import com.evilkingmediabeta.model.MoviesModel;

public class SeriesActivityCategoryServer4 extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BindListSeriesCategory4Adapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<MoviesModel> movieList = new ArrayList<>();

    Button btnhome, btncategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_category_server2);

        recyclerView = findViewById(R.id.recyclerview);
        btnhome = findViewById(R.id.btnhome);
        btncategory = findViewById(R.id.btncategory);

        new prepareMovieData(Constant.SERIESURL4, "").execute();

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SeriesActivityCategoryServer4.this, SeriesActivityServer4.class);
                startActivity(i);

            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SeriesActivityCategoryServer4.this, SeriesActivityCategoryServer2.class);
                startActivity(i);
            }
        });



    }

    private class prepareMovieData extends AsyncTask<String, Void, Void> {
        String movieUrl;
        String mainurl;

        public prepareMovieData(String mainurl, String movieurl1Cinema) {
            this.movieUrl = movieurl1Cinema;
            this.mainurl = mainurl;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityCategoryServer4.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {
                // Connect to the web site
                Document doc = Jsoup.connect(mainurl + "/" + movieUrl).timeout(10000).get();

                Elements data = doc.select("ul#cat_mob").first().getElementsByTag("li");


                for (int i = 0; i < data.size(); i++) {

                    String url = data.select("li").get(i).getElementsByTag("a").attr("href");
                    String title = data.select("li").get(i).getElementsByTag("a").text();
                    Log.e("url", url + "" + title);
                    MoviesModel movie = new MoviesModel();
                    movie.setUrl(url);
                    movie.setTitle(title);
                    movieList.add(movie);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            mAdapter = new BindListSeriesCategory4Adapter(movieList, SeriesActivityCategoryServer4.this);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCategoryServer4.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

}
