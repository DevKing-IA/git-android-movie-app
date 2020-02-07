package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class SportsWebCasterActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<SportCastModel> sportCastModelList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private EditText etSearch;

    SportsWebCasterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_web_caster);

        recyclerView = findViewById(R.id.urlRecyclerView);
        etSearch = findViewById(R.id.etSearch);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsWebCasterActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsWebCasterActivity.this, etSearch.getWindowToken());
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RecyclerView.LayoutManager mLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new GridLayoutManager(SportsWebCasterActivity.this, 3);
        } else {
            mLayoutManager = new GridLayoutManager(SportsWebCasterActivity.this, 2);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();

        new prepareData(Constant.SPORTS_WEB_CASTER_URL).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareData extends AsyncTask<String, Void, Void> {

        private String main_url;

        prepareData(String main_url) {
            this.main_url = main_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsWebCasterActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(main_url).timeout(30000).get();
                Elements body = doc.select("div[class=entry-content clearfix]").select("p");
                for (Element element : body){
                    Elements aTags = element.select("a");
                    for(Element aTag : aTags){
                        SportCastModel urls = new SportCastModel();
                        urls.setTitle(aTag.text());
                        urls.setUrl(aTag.attr("href"));
                        sportCastModelList.add(urls);
                    }
                }

                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute (Void result){

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mAdapter = new SportsWebCasterAdapter(sportCastModelList, SportsWebCasterActivity.this);

            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    }
}
