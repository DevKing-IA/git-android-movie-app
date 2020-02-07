package com.evilkingmediabeta.videoteca;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class DocumentariVideoLinkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog mProgressDialog;
    DocumentariVideoLinkAdapter mAdapter;
    List<MoviesModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentari_video_link);

        recyclerView = findViewById(R.id.recyclerView);

        new showLinks(getIntent().getStringExtra("url")).execute();

        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class showLinks extends AsyncTask<String, Void, Void> {
        String mainurl;

        private showLinks(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DocumentariVideoLinkActivity.this);
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
                Elements body = doc.select("div[class=moview-details-text]").select("a");
                int num = 1;
                for (int i = 0; i < body.size(); i ++){
                    if(body.get(i).select("a").text().equals("openload")){
                        String url = body.get(i).select("a").attr("href");
                        MoviesModel model = new MoviesModel();
                        model.setTitle("Link " + num);
                        model.setUrl(url);
                        models.add(model);
                        num ++;
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

            mAdapter = new DocumentariVideoLinkAdapter(models, DocumentariVideoLinkActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(DocumentariVideoLinkActivity.this, 4);
            } else {
                mLayoutManager = new GridLayoutManager(DocumentariVideoLinkActivity.this, 2);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
