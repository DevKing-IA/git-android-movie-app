package com.evilkingmediabeta.videoteca;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

public class DocumentariVideoListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etSearch;
    ProgressDialog mProgressDialog;
    DocumentariVideoListAdapter mAdapter;
    List<MoviesModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentari_video_list);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(DocumentariVideoListActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(DocumentariVideoListActivity.this, etSearch.getWindowToken());
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new prepareData(getIntent().getStringExtra("url")).execute();

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareData extends AsyncTask<String, Void, Void> {
        String mainurl;

        private prepareData(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DocumentariVideoListActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements numbers = doc.select("ul[class=page-numbers]").select("li");
                String number = numbers.get(numbers.size()-2).select("a").text();
                int page_count_num = Integer.parseInt(number);
                String siteUrl = mainurl.split("com")[0] + "com";
                String searchType = mainurl.split("com")[1];
                for (int i = 1; i <= page_count_num; i++){
                    String page_url = siteUrl + "/page/" + i + searchType;
                    Document document = Jsoup.connect(page_url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                            .timeout(30000)
                            .get();
                    Elements body = document.select("div[class=moview-common-layout]").select("div[class=row margin-bottom]");
                    for (int j = 0; j < body.size(); j++){
                        Elements body1 = body.get(j).select("div[class=item col-sm-3]");
                        for (int h = 0; h < body1.size(); h++){
                            String image = body1.get(h).select("div[class=movie-poster]").select("img").attr("src");
                            String title = body1.get(h).select("div[class=movie-details]").select("h4").select("a").text();
                            String url = body1.get(h).select("div[class=movie-details]").select("h4").select("a").attr("href");
                            MoviesModel model = new MoviesModel();
                            model.setTitle(title);
                            model.setImage(image);
                            model.setUrl(url);
                            models.add(model);
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

            if(mProgressDialog!=null) {
                mProgressDialog.dismiss();
            }

            mAdapter = new DocumentariVideoListAdapter(models, DocumentariVideoListActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(DocumentariVideoListActivity.this, 4);
            } else {
                mLayoutManager = new GridLayoutManager(DocumentariVideoListActivity.this, 2);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
