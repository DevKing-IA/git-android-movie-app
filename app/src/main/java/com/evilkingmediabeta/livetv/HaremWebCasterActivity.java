package com.evilkingmediabeta.livetv;

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

public class HaremWebCasterActivity extends AppCompatActivity{

//    Button btnFilm, btnSerieTV;
    RecyclerView recyclerView;
    EditText etSearch;
    ProgressDialog mProgressDialog;

//    List<SportCastModel> filmModelList = new ArrayList<>();
//    List<SportCastModel> serieModelList = new ArrayList<>();
    List<HaremModel> haremModelList = new ArrayList<>();

    HaremWebCasterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harem_web_caster);

//        btnFilm = findViewById(R.id.btnFilm);
//        btnSerieTV = findViewById(R.id.btnSerieTV);
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    CustomKeyboardHandler.showKeyboard(HaremWebCasterActivity.this);
                else
                    CustomKeyboardHandler.hiddenKeyboard(HaremWebCasterActivity.this, etSearch.getWindowToken());
            }
        });

//        btnFilm.setOnClickListener(this);
//        btnSerieTV.setOnClickListener(this);

        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(HaremWebCasterActivity.this, 3);
        } else {
            layoutManager = new GridLayoutManager(HaremWebCasterActivity.this, 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new prepareData(Constant.LIVETV_HAREM_WEBCASTER_URL).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnFilm:
//                mAdapter = new SportsWebCasterAdapter(filmModelList, SportsWebCasterActivity.this);
//                recyclerView.setAdapter(mAdapter);
//                break;
//            case R.id.btnSerieTV:
//                mAdapter = new SportsWebCasterAdapter(serieModelList, SportsWebCasterActivity.this);
//                recyclerView.setAdapter(mAdapter);
//                break;
//        }
//    }

    private class prepareData extends AsyncTask<String, Void, Void> {
        String mainurl;

        prepareData(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(HaremWebCasterActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements body = doc.select("div[class=entry-content clearfix]").get(0).select("p");
                for(Element element : body){
//                    if(element.ownText().toLowerCase().equals("film")){
//                        Elements filmUrlNodes = element.select("a");
//                        for(Element aTag : filmUrlNodes){
//                            SportCastModel model = new SportCastModel();
//                            model.setTitle(aTag.text());
//                            model.setUrl(aTag.attr("href"));
//                            filmModelList.add(model);
//                        }
//                    } else {
                        Elements serieTVUrlNodes = element.select("a");
                        for (Element aTag : serieTVUrlNodes){
                            HaremModel model = new HaremModel();
                            model.setTitle(aTag.text());
                            model.setUrl(aTag.attr("href"));
                            haremModelList.add(model);
                        }
//                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(mProgressDialog!=null) {
                mProgressDialog.dismiss();
            }

            mAdapter = new HaremWebCasterAdapter(haremModelList, HaremWebCasterActivity.this);
            recyclerView.setAdapter(mAdapter);

        }
    }
}
