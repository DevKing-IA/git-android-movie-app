package com.evilkingmediabeta.series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListAdapter4UrlAdapter;
import com.evilkingmediabeta.adapter.BindListSeries4Adapter;
import com.evilkingmediabeta.model.MoviesModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SeriesActivityServer4 extends AppCompatActivity {
    private LinearLayout linearCategory;
    private RecyclerView recyclerView;
    private BindListSeries4Adapter mAdapter;
    private BindListAdapter4UrlAdapter mAdapter1;
    private List<MoviesModel> movieList = new ArrayList<>();
    private List<MoviesModel> movieurlList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    EditText etMoviename;
    Button btnMoviename;
    String Pageurl;
    ArrayList<String> arrayList = new ArrayList<>();
    private int elementsize;
    Boolean isPrev, isNext, isSearch = false, isNextSearch = false, isMovieita = false;
    int corePoolSize = 60;
    String Currenturl;
    Boolean pageavl = false;
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
        setContentView(R.layout.activity_series_server4);

        linearCategory = findViewById(R.id.categories);
        recyclerView = findViewById(R.id.recyclerview);
        btnhome = findViewById(R.id.btnhome);
        btncategory = findViewById(R.id.btncategory);
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ivNext.setVisibility(View.GONE);
        ivPrev.setVisibility(View.GONE);
        isNext = false;

        etMoviename = findViewById(R.id.etMoviname);
        btnMoviename = findViewById(R.id.btnMoviname);
        Pageurl = getIntent().getStringExtra("URL");
        etMoviename.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etMoviename.setFocusableInTouchMode(true);
                etMoviename.setFocusable(true);
                return false;
            }
        });

        btnMoviename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = true;
                new searchdata(Constant.SERIESURL4_search).execute();

            }
        });

        if (Pageurl == null) {
            new prepareMovieData(Constant.SERIESURL4, "").execute();
        } else {
            new prepareMovieData(Pageurl, "").execute();
        }


        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                new NextPagedata().execute();

            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                new PreviousPagedata().execute();
            }
        });
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

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new prepareMovieData(Constant.SERIESURL4, "").execute();
                isNext = false;
                isMovieita = false;
                i = 0;
                etMoviename.setText("");
                Constant.isCategory = false;
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeriesActivityServer4.this, SeriesActivityCategoryServer4.class);
                startActivity(intent);
                isNext = false;
                isMovieita = false;
                i = 0;
                etMoviename.setText("");
                Constant.isCategory = true;
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
            mProgressDialog = new ProgressDialog(SeriesActivityServer4.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {
                movieList.clear();
                // Connect to the web site
                doc = Jsoup.connect(mainurl + "" + movieUrl).ignoreContentType(true).ignoreHttpErrors(true).timeout(10*1000).get();
                try {
                    MoviesModel moviesurl = new MoviesModel();
                    moviesurl.setCurrenturl(mainurl + "" + movieUrl);
                    movieurlList.add(moviesurl);

                    String title = null;
                    Elements maincol = doc.select("#main_col").first().getElementsByClass("mediaWrap mediaWrapAlt");

                    for (int i = 0; i < maincol.size(); i++) {

                        String image = maincol.get(i).getElementsByTag("a").select("img").attr("src");
                        String url = maincol.get(i).getElementsByTag("a").attr("href");
                        if (Pageurl == null) {
                            title = maincol.get(i).getElementsByTag("a").attr("title");
                        } else {
                            title = maincol.get(i).getElementsByClass("title-film").first().getElementsByTag("a").first().getElementsByTag("p").text();
                        }

                        Log.e("image", image);
                        MoviesModel movie = new MoviesModel();
                        movie.setImage(image);
                        movie.setUrl(url);
                        movie.setTitle(title);
                        movieList.add(movie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if (pageavl == false) {

                Elements pagination = doc.getElementsByClass("nextpostslink");
                try {
                    if (pagination.size() != 0) {
                        ivNext.setVisibility(View.VISIBLE);
                    } else {
                        ivNext.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!movieUrl.isEmpty()) {
                    mAdapter = new BindListSeries4Adapter(movieList, SeriesActivityServer4.this);
                    // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer4.this, 3);
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
                } else {
                    if (Constant.isCategory) {
                        mAdapter1 = new BindListAdapter4UrlAdapter(movieList, SeriesActivityServer4.this);
                        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer4.this, 2);
                        recyclerView.setLayoutManager(mLayoutManager);
                        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter1);
                        //ivNext.setVisibility(View.VISIBLE);

                    } else {
                        mAdapter = new BindListSeries4Adapter(movieList, SeriesActivityServer4.this);
                        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer4.this, 3);
                        recyclerView.setLayoutManager(mLayoutManager);
                        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                    }
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

    }


    private class NextPagedata extends AsyncTask<String, Void, Void> {
        String NextPageUrl = null;
        String newurl = null;
        Document doc = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer4.this);
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
                isNext = true;
                String result;

                for (int i = 0; i < movieurlList.size(); i++) {

                    newurl = movieurlList.get(i).getCurrenturl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                movieurlList.clear();
                for (Element urls : doc.getElementsByClass("nextpostslink")) {
                    //perform your data extractions here.
                    for (Element urlss : urls.getElementsByTag("a")) {
                        result = urlss != null ? urlss.absUrl("href") : null;
                        Log.d("Urls", String.valueOf(urlss));
                        NextPageUrl = urlss.attr("href");
                        Log.e("page no", urls.getElementsByTag("a").text());
                        Log.d("Urls", NextPageUrl);
                        MoviesModel movieurl = new MoviesModel();
                        movieurl.setCurrenturl(NextPageUrl);
                        movieurlList.add(movieurl);

                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                doc = Jsoup.connect(NextPageUrl).timeout(10000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView


            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            movieList.clear();
            ivPrev.setVisibility(View.VISIBLE);


            Elements nextpage = doc.getElementsByClass("nextpostslink");

            if (nextpage.size() != 0) {
                ivNext.setVisibility(View.VISIBLE);
                pageavl = true;
            } else {
                ivNext.setVisibility(View.GONE);
                pageavl = true;
            }

            if (isSearch == true) {
                /*String SpilString = NextPageUrl;
                String[] separated = NextPageUrl.split("\\?");
                for (String item : separated) {
                    System.out.println("item = " + item);
                }
                NextPageUrl = separated[0];*/
                isNextSearch = true;
                new searchdata(NextPageUrl).execute();

            } else {
                new prepareMovieData(NextPageUrl, "").execute();
            }

        }

        ;
    }


    private class PreviousPagedata extends AsyncTask<String, Void, Void> {
        String PrevPageUrl = null;
        String newurl = null;
        Document doc = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer4.this);
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
                isNext = true;
                String result;

                for (int i = 0; i < movieurlList.size(); i++) {

                    newurl = movieurlList.get(i).getCurrenturl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                movieurlList.clear();
                for (Element urls : doc.getElementsByClass("previouspostslink")) {
                    //perform your data extractions here.
                    for (Element urlss : urls.getElementsByTag("a")) {
                        result = urlss != null ? urlss.absUrl("href") : null;
                        Log.d("Urls", String.valueOf(urlss));
                        PrevPageUrl = urlss.attr("href");
                        Log.d("Urls", PrevPageUrl);
                        MoviesModel movieurl = new MoviesModel();
                        movieurl.setCurrenturl(PrevPageUrl);
                        movieurlList.add(movieurl);

                        break;
                    }
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                doc = Jsoup.connect(PrevPageUrl).timeout(10000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            // Set description into TextView
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            movieList.clear();

            Elements prevpage = doc.getElementsByClass("previouspostslink");

            if (prevpage.size() != 0) {
                ivPrev.setVisibility(View.VISIBLE);
            } else {
                ivPrev.setVisibility(View.GONE);
            }

            Elements nextpage = doc.getElementsByClass("nextpostslink");

            if (nextpage.size() != 0) {
                ivNext.setVisibility(View.VISIBLE);
                pageavl = true;
            } else {
                ivNext.setVisibility(View.GONE);
                pageavl = true;
            }

            if (isSearch == true) {
               /* String[] separated = PrevPageUrl.split("\\?");
                for (String item : separated) {
                    System.out.println("item = " + item);
                }
                PrevPageUrl = separated[0];*/
                new searchdata(PrevPageUrl).execute();
            } else {
                new prepareMovieData(PrevPageUrl, "").execute();
            }

        }

        ;
    }

    private class searchdata extends AsyncTask<String, Void, Void> {
        String mainurl = null;
        String newurl = null;
        Document document;

        public searchdata(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer4.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {


                Connection.Response loginPageResponse =
                        Jsoup.connect(mainurl)
                                .referrer(Constant.SERIESURL4_search)
                                .userAgent("Mozilla/5.0")
                                .timeout(10 * 1000)
                                .followRedirects(true)
                                .execute();

                System.out.println("Fetched login page");

                //get the cookies from the response, which we will post to the action URL
                Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();

                //lets make data map containing all the parameters and its values found in the form
                Map<String, String> mapParams = new HashMap<String, String>();
                mapParams.put("s", etMoviename.getText().toString().trim());


                //URL found in form's action attribute
                String strActionURL = mainurl;

                Connection.Response responsePostLogin = Jsoup.connect(strActionURL)
                        //referrer will be the login page's URL
                        .referrer(mainurl)
                        //user agent
                        .userAgent("Mozilla/5.0")
                        //connect and read time out
                        .timeout(10 * 1000)
                        //post parameters
                        .data(mapParams)
                        //cookies received from login page
                        .cookies(mapLoginPageCookies)
                        //many websites redirects the user after login, so follow them
                        .followRedirects(true)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .execute();

                MoviesModel moviesurl = new MoviesModel();
                String searchquery = etMoviename.getText().toString().trim();
                moviesurl.setCurrenturl(mainurl + "?s=" + searchquery);
                movieurlList.add(moviesurl);

                System.out.println("HTTP Status Code: " + responsePostLogin.statusCode());

                //parse the document from response
                document = responsePostLogin.parse();


                movieList.clear();

                Elements data = document.getElementsByClass("image-film image-film-2");

                for (int i = 0; i < data.size(); i++) {

                    String image = data.get(i).getElementsByTag("img").attr("src");
                    String url = data.get(i).getElementsByTag("a").attr("href");
                    String title = data.get(i).getElementsByTag("a").attr("title");
                    MoviesModel movie = new MoviesModel();
                    movie.setImage(image);
                    movie.setUrl(url);

                    movie.setTitle(title);
                    movieList.add(movie);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            mAdapter = new BindListSeries4Adapter(movieList, SeriesActivityServer4.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer4.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);

            Elements pagination = document.getElementsByClass("wp-pagenavi");
            if (pagination.size() != 0) {
                ivNext.setVisibility(View.VISIBLE);
            } else {
                ivNext.setVisibility(View.GONE);
            }

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

    @Override
    public void onBackPressed() {
        Constant.isCategory = false;
        super.onBackPressed();
    }


}


