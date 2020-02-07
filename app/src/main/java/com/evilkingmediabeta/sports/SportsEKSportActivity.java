package com.evilkingmediabeta.sports;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.EKSportAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsEKSportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListView titleView;
    private List<String> titleList = new ArrayList<>();
    private List<String> aTagList = new ArrayList<>();
    private List<String> aTagListFiltered = new ArrayList<>();
    private List<String> aTagListTemp = new ArrayList<>();
    private List<SportsModel> models = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_sports_eksport);

        recyclerView = findViewById(R.id.urlRecyclerView);
        titleView = findViewById(R.id.titleList);
        etSearch = findViewById(R.id.etSearch);

        titleView.requestFocus();
        titleView.requestFocus();

        titleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayContents(position);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsEKSportActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsEKSportActivity.this, etSearch.getWindowToken());
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
                TitleListAdapter adapter = new TitleListAdapter(SportsEKSportActivity.this, 0, filterTitle);
                titleView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new prepareSportsData().execute();

        CheckXml.checkXml(this);

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
            String url = tagElements.get(i).attr("href");
            if(tagElements.get(i).text().contains("http")){
                urlTitle = "link " + (i + 1);
            } else {
                urlTitle = tagElements.get(i).text();
            }
            SportsModel urls = new SportsModel();
            urls.setTitle(urlTitle);
            urls.setUrl(url);
            models.add(urls);
        }

        EKSportAdapter sportAdapter = new EKSportAdapter(models, SportsEKSportActivity.this);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(SportsEKSportActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
        recyclerView.setAdapter(sportAdapter);
        sportAdapter.notifyDataSetChanged();
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsEKSportActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(Constant.SPORTS_EK_SPORT_URL).timeout(100000).get();
                Elements body = doc.select("div[class=entry-content clearfix]").select("p");
                for (int i = 0; i < body.size(); i++){
                    if (body.get(i).childNodeSize() != 1){
                        String title = body.get(i).ownText();
                        String aTags = body.get(i).select("a").toString();
                        titleList.add(title);
                        aTagList.add(aTags);
                    }
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
            TitleListAdapter adapter = new TitleListAdapter(SportsEKSportActivity.this, 0, titleList);
            titleView.setAdapter(adapter);
            //titleView.setBackgroundResource(R.drawable.section_selection_background_whitetext);

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
