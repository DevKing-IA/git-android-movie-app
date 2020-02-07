package com.evilkingmediabeta.series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListSeriesCat2Adapter;
import com.evilkingmediabeta.adapter.BindListSeriespart2Adapter;
import com.evilkingmediabeta.model.MoviesModel;

public class SeriesActivityCatServer2 extends AppCompatActivity {
    private LinearLayout linearCategory;
    private RecyclerView recyclerView;
    private BindListSeriespart2Adapter mAdapter;
    private BindListSeriesCat2Adapter mAdapter2;
    private List<MoviesModel> movieList = new ArrayList<>();
    private List<MoviesModel> movieurlList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    EditText etMoviename;
    String url;
    Button btnMoviename;
    private int elementsize;
    Boolean isPrev, isNext;
    int corePoolSize = 60;
    String Currenturl;
    SearchView search;
    int i = 0;
    Boolean videoplay = false;
    String Category = "";
    int maximumPoolSize = 80;
    int position;
    int keepAliveTime = 10;
    private ArrayList<String> yearArrayList = new ArrayList<>();
    private ArrayList<String> durationArrayList = new ArrayList<>();
    private Button btnhome, btncategory, btnfilmsub, btnfilmaz;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_series_server_sub2);

        //  setContentView(R.layout.gridview_list);
        recyclerView = findViewById(R.id.recyclerview);
        btnhome = findViewById(R.id.btnhome);
        btncategory = findViewById(R.id.btncategory);
        url = getIntent().getStringExtra("url");
        position = getIntent().getIntExtra("position", 0);


        new prepareMovieData(url, "").execute();


        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SeriesActivityCatServer2.this, SeriesActivityServer2.class);
                startActivity(i);
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SeriesActivityCatServer2.this, SeriesActivityCategoryServer2.class);
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
            mProgressDialog = new ProgressDialog(SeriesActivityCatServer2.this);
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


                if (position == 0) {
                    videoplay = false;
                    Elements data = doc.getElementsByClass("se-c");

                    Log.e("size", data.size() + "");

                    for (int i = 0; i < data.size(); i++) {

                        //String urldata = data.get(i).getElementsByTag("a").attr("href");
                        String title = data.get(i).getElementsByClass("title").text();
                        MoviesModel movie = new MoviesModel();
                        movie.setTitle(title);
                        movie.setUrl(url);
                        movieList.add(movie);
                    }
                } else {
                    videoplay = true;
                    Log.e("position", position + "");

                    Elements episode = doc.getElementsByClass("se-a");

                    for (int i = 0; i < episode.size(); i++) {
                        Elements episodedata = episode.get(position - 1).select("li");
                        Log.e("size episode", episodedata.size() + "");
                        for (int j = 0; j < episodedata.size(); j++) {

                            String title = episodedata.get(j).getElementsByClass("episodiotitle").first().getElementsByTag("a").text();
                            String url = episodedata.get(j).getElementsByClass("episodiotitle").first().getElementsByTag("a").attr("href");
                            MoviesModel movie = new MoviesModel();
                            movie.setTitle(title);
                            movie.setUrl(url);
                            movieList.add(movie);
                            //String urls=url.get().attr("href");
                            //Elements body=titleelement.get(position - 1).getElementsByClass("episodiotitle");
                            //String title = titleelement.get(position -1 ).getElementsByTag("a").attr("href");
                            Log.e("url", url);

                        }
                        break;
                        //System.out.print(episode.get(position - 1));
                       /* Elements title = episode.get(position - 1).select("ul[class=episodios]").first().getElementsByClass("episodiotitle");
                        String url = episode.select("ul[class=episodios]").first().getElementsByTag("li").first().getElementsByClass("episodiotitle").attr("href");
                        Log.e("title", title + "");*/
                       /* MoviesModel movie = new MoviesModel();
                        movie.setTitle(title);
                        movie.setUrl(url);
                        movieList.add(movie);*/
                    }
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
            if(videoplay == true){
                mAdapter2 = new BindListSeriesCat2Adapter(movieList, SeriesActivityCatServer2.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer2.this, 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter2);
            }
            else{
                mAdapter = new BindListSeriespart2Adapter(movieList, SeriesActivityCatServer2.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer2.this, 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }



        }
    }

}

