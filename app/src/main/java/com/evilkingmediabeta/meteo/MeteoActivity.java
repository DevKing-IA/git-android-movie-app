package com.evilkingmediabeta.meteo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.MeteoAdapter;
import com.evilkingmediabeta.model.MeteoModel;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class MeteoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeteoAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    ImageView ivUp, ivDown;
    int corePoolSize = 60;
    String Currenturl;
    int i = 0;
    String Category = "";
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ArrayList<String> allURls = new ArrayList<String>();
    ArrayList<String> allURl = new ArrayList<String>();
    ArrayList<String> alltitle = new ArrayList<String>();
    private List<MeteoModel> meteolist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meteo);

        mProgressDialog = new ProgressDialog(MeteoActivity.this);
        //  setContentView(R.layout.gridview_list);
        recyclerView = findViewById(R.id.recyclerview);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);

        Constant.playInWuffyOrXmtv(MeteoActivity.this,Constant.MUSICURL4);
       // new prepareMovieData(Constant.METEOURL).execute();

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

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareMovieData extends AsyncTask<String, Void, Void> {
        String titles;
        String title;
        String title1;
        String ftitle;
        String mainurl;
        String videourl;
        String ptitle;

        public prepareMovieData(String mainurl) {
            this.mainurl = mainurl;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                URL urls = new URL(mainurl);
                BufferedReader in = new BufferedReader(new InputStreamReader(urls
                        .openStream()));
                String str;
                while ((str = in.readLine()) != null) {

                    allURls.add(str);


                }
                String url = null;
                for (int i = 0; i < allURls.size(); i++) {
                    url = allURls.get(i).toString();

                    if (allURls.get(i).toString().contains(".mp4") || allURls.get(i).toString().contains(".m4v")) {

                        allURl.add(allURls.get(i).toString());
                    }
                }

                for (int i = 0; i < allURls.size(); i++) {

                        if(i<=7){
                            if (allURls.get(i+1).toString().contains(".mp4") || allURls.get(i+1).toString().contains(".m4v")) {

                                Log.e("string", "not title");
                            }
                            else{
                                String[] separated = allURls.get(i+1).toString().split(",");
                                for (String item : separated) {
                                    System.out.println("item = " + item);
                                }
                                title = separated[1];

                                String[] separated1 = title.split("]");
                                for (String item : separated1) {
                                    System.out.println("item = " + item);
                                }
                                title1 = separated1[1];

                                String[] separated2 = title1.split("\\[");
                                for (String item : separated2) {
                                    System.out.println("item = " + item);
                                }
                                ftitle = separated2[0];
                                alltitle.add(ftitle);
                            }

                        }
                        else{
                            if (allURls.get(i).toString().contains(".mp4") || allURls.get(i).toString().contains(".m4v")) {

                                Log.e("string", "not title");
                            }
                            else{
                                String[] separated = allURls.get(i).toString().split(",");
                                for (String item : separated) {
                                    System.out.println("item = " + item);
                                }
                                title = separated[1];
                            }

                        }






                    }

                for (int i = 0; i < alltitle.size(); i++) {
                    MeteoModel meteo = new MeteoModel();
                    ptitle = alltitle.get(i).toString();
                    meteo.setTitle(ptitle);
                    meteo.setUrl(allURl.get(i));
                    meteolist.add(meteo);
                }

                //meteo.setUrl(videourl);

                Log.e("data", allURls + "");
                in.close();

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

            mAdapter = new MeteoAdapter(meteolist, MeteoActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MeteoActivity.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


