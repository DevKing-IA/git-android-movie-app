package com.evilkingmediabeta.demand;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Serie2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListView titleView;
    private List<String> titleList = new ArrayList<>();
    private List<String> aTagList = new ArrayList<>();
    private List<String> aTagListFiltered = new ArrayList<>();
    private List<String> aTagListTemp = new ArrayList<>();
    private List<Serie2Model> models = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_embed_scrape);

        recyclerView = findViewById(R.id.urlRecyclerView);
        titleView = findViewById(R.id.titleList);
        etSearch = findViewById(R.id.etSearch);
        titleView.requestFocus();
        titleView.requestFocus();

        titleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayContents(position);
                titleView.requestFocus();

                ;
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(Serie2Activity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(Serie2Activity.this, etSearch.getWindowToken());
                    titleView.requestFocus();

                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> filterTitle = new ArrayList<>();
                aTagListFiltered.clear();
                aTagListTemp.addAll(aTagList);
                for (int i = 0; i < titleList.size(); i++){
                    if(s.length() == 0){
                        filterTitle = titleList;
                        aTagListFiltered.addAll(aTagListTemp);
                    } else {
                        if(titleList.get(i).toLowerCase().contains(s)){
                            filterTitle.add(titleList.get(i));
                            aTagListFiltered.add(aTagListTemp.get(i));
                        }
                    }
                }
                TitleListAdapter adapter = new TitleListAdapter(Serie2Activity.this, 0, filterTitle);
                titleView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new prepareSportsData().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private void displayContents(int pos){
        models.clear();
        Document document = Jsoup.parse(aTagListFiltered.get(pos));
        Elements tagElements = document.select("a");
        for (int i = 0; i < tagElements.size(); i++){
            String urlTitle;
            if(tagElements.get(i).text().startsWith("http")){
                urlTitle = "LINK " + (i + 1);
            } else {
                urlTitle = tagElements.get(i).text();
            }
            String url = tagElements.get(i).attr("href");
            Serie2Model urls = new Serie2Model();
            urls.setTitle(urlTitle);
            urls.setUrl(url);
            models.add(urls);
        }

        Serie2Adapter adapter = new Serie2Adapter(models, Serie2Activity.this);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(Serie2Activity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Serie2Activity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(Constant.EMBED_SERIE2).timeout(30000).get();
                Elements body = doc.select("div[class=entry-content clearfix]").select("p");
                for (Element element : body){
                    String title = element.ownText();
                    String aTags = element.select("a").toString();
                    titleList.add(title);
                    aTagList.add(aTags);
                }

                aTagListFiltered.addAll(aTagList);

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
            TitleListAdapter adapter = new TitleListAdapter(Serie2Activity.this, 0, titleList);
            titleView.setAdapter(adapter);

        }

    }

    private class TitleListAdapter extends ArrayAdapter<String> {

        private TitleListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(R.layout.simple_list_item, parent, false);
            }
            String title = getItem(position);

            TextView titleTextView = v.findViewById(R.id.text_view);

            if(title != null) {
                titleTextView.setText(title);
            }

            return v;
        }
    }
}
