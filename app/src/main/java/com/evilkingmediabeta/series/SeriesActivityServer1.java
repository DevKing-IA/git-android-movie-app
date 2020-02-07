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

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListSeriesCatEpisod1Adapter;
import com.evilkingmediabeta.adapter.BindListSeriesCatSeason1Adapter;
import com.evilkingmediabeta.model.MoviesModel;

public class SeriesActivityServer1 extends AppCompatActivity {
    private LinearLayout linearCategory;
    private RecyclerView recyclerView;
    private BindListSeriesCatSeason1Adapter mAdapter;
    private BindListSeriesCatEpisod1Adapter mAdapter2;
    private List<MoviesModel> movieList = new ArrayList<>();
    private List<MoviesModel> movieurlList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    EditText etMoviename;
    Button btnMoviename;
    ArrayList<String> arrayList = new ArrayList<>();
    private int elementsize;
    Boolean isPrev, isNext, isSearch = false, isNextSearch = false, isMovieita = false;
    int corePoolSize = 60;
    String Currenturl, category;
    SearchView search;
    int i = 0;
    String Category = "", url = null;
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
        setContentView(R.layout.activity_series_server1);
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
        url = getIntent().getStringExtra("url");
        category = getIntent().getStringExtra("category");
        etMoviename = findViewById(R.id.etMoviname);
        btnMoviename = findViewById(R.id.btnMoviname);

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
                new searchdata(Constant.SERIESURL1).execute();

            }
        });

        if (url == null) {
            new prepareMovieData(Constant.SERIESURL1, "").execute();
        } else {
            new prepareMovieData(url, "").execute();
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

                new prepareMovieData(Constant.SERIESURL1, "").execute();
                movieList.clear();
//              mAdapter.notifyDataSetChanged();
                Category = "";
                isNext = false;
                isMovieita = false;
                i = 0;
                etMoviename.setText("");
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SeriesActivityServer1.this, SeriesActivityCatServer1.class);
                startActivity(i);
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
            mProgressDialog = new ProgressDialog(SeriesActivityServer1.this);
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
                 doc = Jsoup.connect(mainurl + "" + movieUrl).ignoreContentType(true).ignoreHttpErrors(true).timeout(10000).get();

                try {
                    MoviesModel moviesurl = new MoviesModel();
                    moviesurl.setCurrenturl(mainurl + "" + movieUrl);
                    movieurlList.add(moviesurl);

                    if (url == null) {
                        Elements link = doc.select("div[class=contenedor]");
                        //     Elements data = link.select("div[class=items]");
                        Elements image = link.select("div[class=imagen]");
                        Elements name = image.select("h2");


                        //Elements imagestr = image.select("img");
                        for (int i = 0; i < image.size(); i++) {
                            MoviesModel movie = new MoviesModel();
                            String img_str = image.get(i).select("img").attr("src");
                            String url = image.get(i).select("a").attr("href");
                            String name_str = name.get(i).text();
                            movie.setImage(img_str);
                            movie.setTitle(name_str);
                            movie.setUrl(url);
                            movieList.add(movie);

                        }

                    } else if (url != null && category != null) {
                        Elements link = doc.select("div[class=contenedor]");
                        //     Elements data = link.select("div[class=items]");
                        Elements image = link.select("div[class=imagen]");
                        Elements name = image.select("h2");


                        //Elements imagestr = image.select("img");
                        for (int i = 0; i < image.size(); i++) {
                            MoviesModel movie = new MoviesModel();
                            String img_str = image.get(i).select("img").attr("src");
                            String url = image.get(i).select("a").attr("href");
                            String name_str = name.get(i).text();
                            movie.setImage(img_str);
                            movie.setTitle(name_str);
                            movie.setUrl(url);
                            movieList.add(movie);

                        }

                    } else {

                   /* Elements link = doc.select("div[class=contenedor]");
                    //     Elements data = link.select("div[class=items]");
                    Elements image = link.select("div[class=imagen]");
                    Elements name = image.select("h2");


                    //Elements imagestr = image.select("img");
                    for (int i = 0; i < image.size(); i++) {
                        MoviesModel movie = new MoviesModel();
                        String img_str = image.get(i).select("img").attr("src");
                        String url = image.get(i).select("a").attr("href");
                        String name_str = name.get(i).text();
                        movie.setImage(img_str);
                        movie.setTitle(name_str);
                        movie.setUrl(url);
                        movieList.add(movie);

                    }*/


                        Elements seasondata = doc.select("#series").select("li");

                        for (int i = 0; i < seasondata.size(); i++) {

                            String title = seasondata.get(i).getElementsByTag("a").text();
                            String url = seasondata.get(i).getElementsByTag("a").attr("href");
                            MoviesModel movie = new MoviesModel();
                            movie.setUrl(url);
                            movie.setTitle(title);
                            movieList.add(movie);
                        }

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

            Elements pagination= doc.getElementsByClass("wp-pagenavi");
            if(pagination.size() != 0){
                ivNext.setVisibility(View.VISIBLE);
            }
            else{
                ivNext.setVisibility(View.GONE);
            }

            if (url == null) {
                mAdapter = new BindListSeriesCatSeason1Adapter(movieList, SeriesActivityServer1.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
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
            else if (url != null && category != null){

                mAdapter = new BindListSeriesCatSeason1Adapter(movieList, SeriesActivityServer1.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
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

            else {
                mAdapter2 = new BindListSeriesCatEpisod1Adapter(movieList, SeriesActivityServer1.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter2);
                //ivNext.setVisibility(View.VISIBLE);


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


    private class NextPagedata extends AsyncTask<String, Void, Void> {
        String NextPageUrl = null;
        String newurl = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer1.this);
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
                Document doc = null;
                for (int i = 0; i < movieurlList.size(); i++) {

                    newurl = movieurlList.get(i).getCurrenturl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                movieurlList.clear();
                for (Element urls : doc.getElementsByClass("wp-pagenavi")) {
                    //perform your data extractions here.
                    for (Element urlss : urls.getElementsByTag("a")) {
                        result = urlss != null ? urlss.absUrl("href") : null;
                        Log.d("Urls", String.valueOf(urlss));
                        NextPageUrl = urlss.attr("href");
                        Log.d("Urls", NextPageUrl);
                        arrayList.add(NextPageUrl);

                    }
                    Log.e("array", arrayList + "");

                    for (int i = 0; i < arrayList.size() - 1; i++) {
                        NextPageUrl = arrayList.get(i);
                        MoviesModel movieurl = new MoviesModel();
                        movieurl.setCurrenturl(NextPageUrl);
                        movieurlList.add(movieurl);
                    }
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
            movieList.clear();
            ivPrev.setVisibility(View.VISIBLE);
            if (isSearch == true) {
                /*String SpilString = NextPageUrl;
                String[] separated = NextPageUrl.split("\\?");
                for (String item : separated) {
                    System.out.println("item = " + item);
                }
                NextPageUrl = separated[0];*/
                isNextSearch = true;
                new searchdata(NextPageUrl).execute();

            } else if (isMovieita == true) {
                new prepareMovieDataita(NextPageUrl, "").execute();
            } else {
                new prepareMovieData(NextPageUrl, "").execute();
            }

        }

        ;
    }


    private class PreviousPagedata extends AsyncTask<String, Void, Void> {
        String PrevPageUrl = null;
        String newurl = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer1.this);
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
                Document doc = null;
                for (int i = 0; i < movieurlList.size(); i++) {

                    newurl = movieurlList.get(i).getCurrenturl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                movieurlList.clear();
                for (Element urls : doc.getElementsByClass("wp-pagenavi")) {
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
            if (isSearch == true) {
               /* String[] separated = PrevPageUrl.split("\\?");
                for (String item : separated) {
                    System.out.println("item = " + item);
                }
                PrevPageUrl = separated[0];*/
                new searchdata(PrevPageUrl).execute();
            } else if (isMovieita == true) {
                new prepareMovieDataita(PrevPageUrl, "").execute();
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
            mProgressDialog = new ProgressDialog(SeriesActivityServer1.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {

                if (isNextSearch == false) {
                    Connection.Response loginPageResponse =
                            Jsoup.connect(mainurl)
                                    .referrer(Constant.SERIESURL1)
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
                            .execute();

                    MoviesModel moviesurl = new MoviesModel();
                    String searchquery = etMoviename.getText().toString().trim();
                    moviesurl.setCurrenturl(mainurl + "?s=" + searchquery);
                    movieurlList.add(moviesurl);

                    System.out.println("HTTP Status Code: " + responsePostLogin.statusCode());

                    //parse the document from response
                    document = responsePostLogin.parse();


                    movieList.clear();

                    Elements link = document.select("div[class=contenedor]");
                    //     Elements data = link.select("div[class=items]");
                    Elements image = link.select("div[class=imagen]");
                    Elements name = image.select("h2");
                    //Elements imagestr = image.select("img");
                    for (int i = 0; i < image.size(); i++) {
                        MoviesModel movie = new MoviesModel();
                        String img_str = image.get(i).select("img").attr("src");
                        String url = image.get(i).select("a").attr("href");
                        String name_str = name.get(i).text();
                        movie.setImage(img_str);
                        movie.setTitle(name_str);
                        movie.setUrl(url);
                        movieList.add(movie);

                    }

                    Map<String, String> mapLoggedInCookies = responsePostLogin.cookies();
                } else {

                     document = Jsoup.connect(mainurl).ignoreContentType(true).ignoreHttpErrors(true).timeout(10000).get();

                    movieList.clear();

                    Elements data = document.getElementsByClass("result-item");

                    for (int i = 0; i < data.size(); i++) {
                        String image = data.get(i).getElementsByClass("thumbnail animation-2").first().select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
                        String title = data.get(i).getElementsByClass("thumbnail animation-2").first().select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("alt");
                        String url = data.get(i).getElementsByTag("a").attr("href");
                        MoviesModel movie = new MoviesModel();
                        movie.setImage(image);
                        movie.setUrl(url);
                        movie.setTitle(title);
                        movieList.add(movie);
                    }
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

            Elements pagination= document.getElementsByClass("wp-pagenavi");
            if(pagination.size() != 0){
                ivNext.setVisibility(View.VISIBLE);
            }
            else{
                ivNext.setVisibility(View.GONE);
            }

            mAdapter = new BindListSeriesCatSeason1Adapter(movieList, SeriesActivityServer1.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);

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

    private class prepareMovieDataita extends AsyncTask<String, Void, Void> {
        String movieUrl;
        String mainurl;
        Document doc;

        public prepareMovieDataita(String mainurl, String movieurl1Cinema) {
            this.movieUrl = movieurl1Cinema;
            this.mainurl = mainurl;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SeriesActivityServer1.this);
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
                 doc = Jsoup.connect(mainurl + "" + movieUrl).ignoreContentType(true).ignoreHttpErrors(true).timeout(10000).get();

                MoviesModel moviesurl = new MoviesModel();
                moviesurl.setCurrenturl(mainurl + "" + movieUrl);
                movieurlList.add(moviesurl);

                Element data = doc.getElementById("dt_contenedor");
                Element data1 = data.getElementById("contenedor");


                Elements data2 = data1.getElementsByClass("items").first().select("article");
                Log.d("data size", data2.size() + "");

                for (int i = 0; i < data2.size(); i++) {
                    Elements imageurl = data2.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
                    String imagedata = imageurl.get(i).attr("src");
                    String title = imageurl.get(i).attr("alt");
                    String urldata = data2.get(i).getElementsByClass("poster").first().getElementsByTag("a").attr("href");
                    MoviesModel movie = new MoviesModel();
                    movie.setImage(imagedata);
                    movie.setUrl(urldata);
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
            // Set description into TextView
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if (!movieUrl.isEmpty()) {
                mAdapter = new BindListSeriesCatSeason1Adapter(movieList, SeriesActivityServer1.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
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
                mAdapter = new BindListSeriesCatSeason1Adapter(movieList, SeriesActivityServer1.this);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SeriesActivityServer1.this, 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
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


}


