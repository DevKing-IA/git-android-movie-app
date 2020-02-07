package com.evilkingmediabeta.utilitymenu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.Fabric;

public class EPGProgramDetailActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    String progarmdetails;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_epgprogram_detail);

        tvInfo = findViewById(R.id.tvInfo);
        progarmdetails = getIntent().getStringExtra("pdetails");

        new prepareEPGData().execute();

        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareEPGData extends AsyncTask<String, Void, Void> {
        String displaydata;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(EPGProgramDetailActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            try {

                Document doc = Jsoup.connect(progarmdetails).timeout(10000).userAgent("Mozilla").get();

                //For Categories

                //System.out.print(doc);

                Elements data = doc.getElementsByClass("program-description");

                 displaydata = data.text();
                Log.e("display", displaydata);

                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
            tvInfo.setText(displaydata);


        }

    }
}
