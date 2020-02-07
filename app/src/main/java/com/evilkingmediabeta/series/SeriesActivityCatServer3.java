package com.evilkingmediabeta.series;

import android.app.ProgressDialog;
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
import com.evilkingmediabeta.adapter.BindListSeriesEpisod3Adapter;
import com.evilkingmediabeta.model.MoviesModel;

public class SeriesActivityCatServer3 extends AppCompatActivity {

    private LinearLayout linearCategory;
    private RecyclerView recyclerView;
    private BindListSeries3SubAdapter mAdapter;
    private BindListSeriesEpisod3Adapter mAdapter2;
    private List<MoviesModel> movieList = new ArrayList<>();
    private List<MoviesModel> movieurlList = new ArrayList<>();
    private List<MoviesModel> movieTitleList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    EditText etMoviename;
    Button btnMoviename;
    String Pageurl, url = null;
    ArrayList<String> arrayList = new ArrayList<>();
    private int elementsize;
    Boolean isPrev, isNext, isSearch = false, isNextSearch = false, isMovieita = false;
    int corePoolSize = 60;
    String Currenturl;
    SearchView search;
    int i = 0;
    String Category = "";
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    private ArrayList<String> yearArrayList = new ArrayList<>();
    private ArrayList<String> durationArrayList = new ArrayList<>();
    private Button btnhome, btncategory;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_series_server3);
        //  setContentView(R.layout.gridview_list);
        linearCategory = findViewById(R.id.categories);
        recyclerView = findViewById(R.id.recyclerview);
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ivNext.setVisibility(View.GONE);
        ivPrev.setVisibility(View.GONE);
        isNext = false;
        url = getIntent().getStringExtra("url");
        etMoviename = findViewById(R.id.etMoviname);
        btnMoviename = findViewById(R.id.btnMoviname);
        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey("episode")) {
            movieurlList = (List<MoviesModel>) bundle.getSerializable("episode");
        }

        if(bundle.containsKey("episode"))
        {
            mAdapter = new BindListSeries3SubAdapter(movieurlList, SeriesActivityCatServer3.this,movieList);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer3.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);
            ivNext.setVisibility(View.VISIBLE);
        }
        else {
            new prepareMovieData(url, "").execute();
        }

        ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollBy(0, -200);
            }
        });

        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollBy(0, 200);

            }
        });

    }

    private class prepareMovieData extends AsyncTask<String, Void, Void> {
        String movieUrl;
        String mainurl;
        Document doc;

        public prepareMovieData(String mainurl, String movieurl1Cinema) {
            this.movieUrl = movieurl1Cinema;
            this.mainurl = mainurl;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityCatServer3.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            if(mainurl!=null) {
                try {
                    movieurlList.clear();
                    movieTitleList.clear();
                    // Connect to the web site
                    doc = Jsoup.connect(mainurl + "" + movieUrl).timeout(10000).get();
                    MoviesModel moviesurl = new MoviesModel();
                    moviesurl.setCurrenturl(mainurl + "" + movieUrl);
                    movieurlList.add(moviesurl);


                        String title, title1;
                        Elements data = doc.select("div[class=container-fluid]");
                        Elements data1 = data.select("div[class=span12 filmbox]").tagName("table").select("div[class=sp-wrap sp-wrap-default]");


                        Log.d("data size", data1.size() + "");

                        for (int i = 0; i < data1.size(); i++) {

                            title = data1.get(i).select("div[class=sp-head unfolded]").text();
                            title1 = null;
                            if (title.isEmpty()) {
                                title1 = data1.get(i).select("div[class=sp-head]").text();
                            }

                            MoviesModel movie = new MoviesModel();

                                if (title.isEmpty()) {

                                        movie.setTitle(title1);
                                        movie.setUrl(mainurl);
                                        movieTitleList.add(movie);

                                } else {


                                        movie.setTitle(title);
                                        movie.setUrl(mainurl);
                                        movieTitleList.add(movie);

                                }

                            Elements data2 = data1.get(i).select("div[class=sp-body]").select("strong");

                                for(int j =0 ; j< data2.size();j++)
                                {
                                    MoviesModel list = new MoviesModel();

                                    String title_episode = data2.get(j).text();
                                    String url_episode = data2.get(j).select("a").get(1).attr("href");
                                    list.setUrl(url_episode);
                                    list.setTitle(title_episode);
                                    movieList.add(list);
                                }

                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView

            mProgressDialog.dismiss();

            if(doc!=null) {
                Elements pagination = doc.getElementsByClass("pagination");
                if (pagination.size() != 0) {
                    ivNext.setVisibility(View.VISIBLE);
                } else {
                    ivNext.setVisibility(View.GONE);
                }
            }


                mAdapter = new BindListSeries3SubAdapter(movieTitleList, SeriesActivityCatServer3.this,movieList);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityCatServer3.this, 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.invalidate();
                recyclerView.setAdapter(mAdapter);
                ivNext.setVisibility(View.VISIBLE);


                try {
                    if (isNext == true) {
                        ivPrev.setVisibility(View.VISIBLE);
                    } else {
                        ivPrev.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    ivPrev.setVisibility(View.GONE);
                }
                if (i != 0) {

                    ivPrev.setVisibility(View.VISIBLE);
                } else {
                    ivPrev.setVisibility(View.GONE);
                }


        }

    }
}
