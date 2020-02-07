package com.evilkingmediabeta.sports;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.BindListSports2Adapter;
import com.evilkingmediabeta.sports.adapter.BindListSports2WatchCategoryAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class SportsStreamingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BindListSports2Adapter mAdapter;
    private BindListSports2WatchCategoryAdapter mAdapter2;
    private List<SportsModel> sportsModelList = new ArrayList<>();
    private List<SportsModel> sportsModelUrlList = new ArrayList<>();
    private List<SportsModel> movieurlList = new ArrayList<>();
    private ArrayList<String> dateArrayList = new ArrayList<>();
    private ArrayList<String> timeArrayList = new ArrayList<>();
    private Map<String, String> urlStringMap = new HashMap<String, String>();
    private ProgressDialog mProgressDialog;

    private ImageView ivNext,ivPrev, ivUp, ivDown;
    private LinearLayout ll_search,ll_categories;
    private Button btnhome, btncategory;
    Boolean isNext, streaming;
    int i = 0;
    private String nexturl, urldata,category;
    SearchView searchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_server2);
        recyclerView = findViewById(R.id.recyclerview);
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ll_search = findViewById(R.id.ll_search);
        ll_categories = findViewById(R.id.categories);
        btnhome = findViewById(R.id.btnhome);
        btncategory = findViewById(R.id.btncategory);
        isNext = false;
        ivNext.setVisibility(View.GONE);
        ivPrev.setVisibility(View.GONE);

        category = getIntent().getStringExtra("category");
        nexturl = getIntent().getStringExtra("url");
        searchview = findViewById(R.id.searchView);

        searchview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                searchview.setFocusable(true);
                searchview.setFocusableInTouchMode(true);
                return false;
            }
        });

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
        if (nexturl == null) {
            new prepareSportsData(Constant.SPORTSURL2).execute();
        } else {
            new prepareSportsData(nexturl).execute();
        }


        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new prepareSportsData(Constant.SPORTSURL2).execute();
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SportsStreamingActivity.this, SportsCategoryActivityServer2.class);
                startActivity(i);
            }
        });

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {
        Document doc = null;
        String url;
        public prepareSportsData(String nextPageUrl) {
            this.url = nextPageUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsStreamingActivity.this);
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
                doc = Jsoup.connect(url).timeout(10000).get();

                try {

                    SportsModel moviesurl = new SportsModel();
                    moviesurl.setCurrentUrl(url);
                    movieurlList.add(moviesurl);

                    if (nexturl == null) {
                        sportsModelList.clear();
                        //For Categories
                        Elements container = doc.select("div[class=container mtb]");
                        // Elements content = container.select("div[class=col-lg-9]");
                        Elements tbody = container.select("tbody");
                        Elements tr = tbody.select("tr");


                        for (int i = 0; i < tr.size(); i++) {
                            String url = tr.get(i).attr("data-href").toString();
                            String td_time = tr.get(i).select("td[class=event-time]").text();
                            String td_team1 = tr.get(i).select("td[class=event-home]").text();
                            String td_team2 = tr.get(i).select("td[class=event-away]").text();
                            SportsModel sportsModel = new SportsModel();
                            sportsModel.setTime(td_time);
                            sportsModel.setTeam1(td_team1);
                            sportsModel.setTeam2(td_team2);
                            sportsModel.setUrl(url);
                            sportsModelList.add(sportsModel);
                        }
                    } else if (nexturl != null && category != null) {
                        sportsModelList.clear();
                        Elements container = doc.select("div[class=container mtb]");
                        // Elements content = container.select("div[class=col-lg-9]");
                        Elements tbody = container.select("tbody");
                        Elements tr = tbody.select("tr");


                        for (int i = 0; i < tr.size(); i++) {
                            String url = tr.get(i).attr("data-href").toString();
                            String td_time = tr.get(i).select("td[class=event-time]").text();
                            String td_team1 = tr.get(i).select("td[class=event-home]").text();
                            String td_team2 = tr.get(i).select("td[class=event-away]").text();
                            SportsModel sportsModel = new SportsModel();
                            sportsModel.setTime(td_time);
                            sportsModel.setTeam1(td_team1);
                            sportsModel.setTeam2(td_team2);
                            sportsModel.setUrl(url);
                            sportsModelList.add(sportsModel);
                        }
                    } else {
                        sportsModelList.clear();
                        Elements container = doc.select("div[class=container mtb]");
                        Elements table = container.select("table[class=table table-striped]");

                        Elements td = table.select("td[class=event-watch]");
                        String a = td.select("a").attr("href");

                        if (a == null || a.isEmpty()) {
                            streaming = false;
                            urldata = table.select("td").text();
                        } else {
                            streaming = true;
                            Elements mElementUrl = td.select("a");
                            for (int i = 0; i < mElementUrl.size(); i++) {
                                if(mElementUrl.get(i).attr("href").contains("javascript")) {
                                    String url_str = mElementUrl.get(i).attr("href");
                                    String title = mElementUrl.get(i).attr("title");
                                    String url1[] = url_str.split("javascript:window.open\\(");
                                    String url2[] = url1[1].split("\\)");
                                    String data = url2[0].replace("'", "");
                                    if (data.contains("youtube")) {
                                        data = data.replace("http", "https");
                                    } else {
                                        data = data;
                                    }
                                    SportsModel sportsModel = new SportsModel();
                                    sportsModel.setUrl(data);
                                    sportsModel.setTitle(title);
                                    sportsModelList.add(sportsModel);
                                }
                            }

                            Log.e("watchdata", sportsModelList + "");


                       /* if (data.contains("youtube")) {
                            urldata = data.replace("http", "https");
                        } else {
                            urldata = data;
                        }*/
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
        protected void onPostExecute (Void result){
            // Set description into TextView

            mProgressDialog.dismiss();


            if (nexturl == null) {
                mAdapter = new BindListSports2Adapter(sportsModelList, SportsStreamingActivity.this, sportsModelUrlList);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SportsStreamingActivity.this, 2);
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


                Elements main = doc.select("ul[class=pagination]");

                if (main.size() == 0) {
                    ivNext.setVisibility(View.GONE);
                } else {
                    ivNext.setVisibility(View.VISIBLE);
                }


            }else if(nexturl != null && category != null) {

                mAdapter = new BindListSports2Adapter(sportsModelList, SportsStreamingActivity.this, sportsModelUrlList);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SportsStreamingActivity.this, 2);
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


                Elements main = doc.select("ul[class=pagination]");

                if (main.size() == 0) {
                    ivNext.setVisibility(View.GONE);
                } else {
                    ivNext.setVisibility(View.VISIBLE);
                }

            }

            else {

                mAdapter2 = new BindListSports2WatchCategoryAdapter(sportsModelList, SportsStreamingActivity.this, sportsModelUrlList);
                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SportsStreamingActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.invalidate();
                recyclerView.setAdapter(mAdapter2);
                //ivNext.setVisibility(View.VISIBLE);


            }
        }


    }

    private class NextPagedata extends AsyncTask<String, Void, Void> {
        String NextPageUrl = null;
        String newurl = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsStreamingActivity.this);
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
                //  isNext = true;
                String result;
                Document doc = null;
                for (int i = 0; i < movieurlList.size(); i++) {

                    newurl = movieurlList.get(i).getCurrentUrl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                Elements main = doc.select("div[class=container mtb]");
                for (Element urls : main.select("ul[class=pagination]")) {
                    //perform your data extractions here.
                    for (Element urlss : urls.getElementsByTag("li")) {
                        for (Element nexturl : urlss.getElementsByTag("a")) {
                            result = urlss != null ? urlss.absUrl("href") : null;
                            Log.d("Urls", String.valueOf(nexturl));
                            NextPageUrl = nexturl.attr("href");
                            Log.d("Urls", NextPageUrl);
                            SportsModel sportsModel = new SportsModel();
                            sportsModel.setUrl(NextPageUrl);
                            sportsModelList.add(sportsModel);
                        }
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
            sportsModelList.clear();
            ivPrev.setVisibility(View.VISIBLE);
            NextPageUrl = "https://streamingsports.me/" + NextPageUrl;
            new  prepareSportsData(NextPageUrl).execute();
        }

        ;
    }


    private class PreviousPagedata extends AsyncTask<String, Void, Void> {
        String PrevPageUrl = null;
        String newurl = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsStreamingActivity.this);
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

                    newurl = movieurlList.get(i).getCurrentUrl();
                }
                doc = Jsoup.connect(newurl).timeout(10000).get();
                movieurlList.clear();
                for (Element urls : doc.getElementsByClass("page_nav")) {
                    //perform your data extractions here.
                    for (Element urlss : urls.getElementsByTag("i")) {
                        for (Element nexturl : urlss.getElementsByTag("a")) {
                            result = urlss != null ? urlss.absUrl("href") : null;
                            Log.d("Urls", String.valueOf(nexturl));
                            PrevPageUrl = nexturl.attr("href");
                            Log.d("Urls", PrevPageUrl);
                            SportsModel movieurl = new SportsModel();
                            movieurl.setCurrentUrl(PrevPageUrl);
                            movieurlList.add(movieurl);
                            break;
                        }
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
            sportsModelList.clear();
            ivPrev.setVisibility(View.VISIBLE);
            PrevPageUrl = "https://streamingsports.me/" + PrevPageUrl;
            new prepareSportsData(PrevPageUrl).execute();
        }

        ;
    }
}
