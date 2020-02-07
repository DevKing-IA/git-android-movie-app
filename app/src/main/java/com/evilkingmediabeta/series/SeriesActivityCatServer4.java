package com.evilkingmediabeta.series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.evilkingmediabeta.adapter.BindListSeries4SubAdapter;
import com.evilkingmediabeta.model.MoviesModel;

public class SeriesActivityCatServer4 extends AppCompatActivity {
    private ImageView ivNext, ivPrev, ivUp, ivDown;
    private EditText etMoviename;
    private LinearLayout llsearch;
    private Button btnMoviename;
    private LinearLayout linearCategory;
    private Button btnhome, btncategory;
    private BindListSeries4SubAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<MoviesModel> movieList = new ArrayList<MoviesModel>();
    private List<MoviesModel> serieList = new ArrayList<MoviesModel>();
    String Episodeurl, newepisodeurl;
    private ProgressDialog mProgressDialog;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    int corePoolSize = 60;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_series_server4);
        Bundle bundle = getIntent().getExtras();
        movieList = (List<MoviesModel>) bundle.getSerializable("data");
        linearCategory = findViewById(R.id.categories);
        recyclerView = findViewById(R.id.recyclerview);
        btnhome = findViewById(R.id.btnhome);
        btncategory = findViewById(R.id.btncategory);
        llsearch = findViewById(R.id.llsearch);
        Episodeurl = getIntent().getStringExtra("url");
        newepisodeurl = getIntent().getStringExtra("episodeurl");

        if (newepisodeurl != null) {
            new prepareEpisodeData().execute();
        } else {
            mAdapter = new BindListSeries4SubAdapter(movieList, SeriesActivityCatServer4.this, Episodeurl);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer4.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ivNext.setVisibility(View.GONE);
        ivPrev.setVisibility(View.GONE);
        //linearCategory.setVisibility(View.GONE);
        llsearch.setVisibility(View.GONE);

       /* MoviesModel moviesModel = new MoviesModel();
        moviesModel.setUrl(Episodeurl);
        movieList.add(moviesModel);*/

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeriesActivityCatServer4.this, SeriesActivityServer4.class);
                startActivity(i);
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeriesActivityCatServer4.this, SeriesActivityCategoryServer4.class);
                startActivity(intent);
            }
        });


    }

    private class prepareEpisodeData extends AsyncTask<Void, Void, Void> {
        String desc;
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityCatServer4.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Document doc = Jsoup.connect(newepisodeurl).timeout(10000).maxBodySize(0).get();
                Elements main = doc.select("nav[class=navbar navbar-fixed-top navbar-default second_nav]");
                Elements ul = main.select("ul[class=nav navbar-nav]");
                Elements li = ul.select("li");

                for (int i = 0; i < li.size(); i++) {
                    try {

                        url = li.get(i).select("a").attr("href");

                        String episode = li.get(i).getElementsByTag("a").text();
                        MoviesModel movie = new MoviesModel();

                        movie.setUrl(url);
                        movie.setTitle(episode);
                        serieList.add(movie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           /*  Intent webIntent = new Intent(context, MusicWebViewActivity.class);
            webIntent.putExtra("url", videoPath);
            context.startActivity(webIntent);*/
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mAdapter = new BindListSeries4SubAdapter(serieList, SeriesActivityCatServer4.this, Episodeurl);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer4.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }
    }
    }
