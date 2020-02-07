package com.evilkingmediabeta.sports;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.adapter.MyP2PAdapter;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class SportsMyp2pActivity extends AppCompatActivity {

    private List<SportsModel> models = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    ImageView ivUp, ivDown;
    Button btnHome, btnSoccer, btnTennis, btnBasketball, btnBaseball, btnVolleyball, btnBoxing, btnMotoGP, btnUSFootball, btnHandball, btnRugby, btnHockey, btnOther;
    EditText etSearch;
    MyP2PAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_myp2p);
        recyclerView = findViewById(R.id.recyclerview);
        btnHome = findViewById(R.id.btnHome);
        btnSoccer = findViewById(R.id.btnSoccer);
        btnTennis = findViewById(R.id.btnTennis);
        btnBasketball = findViewById(R.id.btnBasketball);
        btnBaseball = findViewById(R.id.btnBaseball);
        btnVolleyball = findViewById(R.id.btnVolleyball);
        btnBoxing = findViewById(R.id.btnBoxing);
        btnMotoGP = findViewById(R.id.btnMotoGP);
        btnUSFootball = findViewById(R.id.btnUSFootball);
        btnHandball = findViewById(R.id.btnHandball);
        btnRugby = findViewById(R.id.btnRugby);
        btnHockey = findViewById(R.id.btnHockey);
        btnOther = findViewById(R.id.btnOther);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        etSearch = findViewById(R.id.etSearch);

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

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(SportsMyp2pActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(SportsMyp2pActivity.this, etSearch.getWindowToken());
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "").execute();
            }
        });

        btnSoccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "soccer").execute();
            }
        });

        btnTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "tennis").execute();
            }
        });

        btnBasketball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "basketball").execute();
            }
        });

        btnBaseball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "baseball").execute();
            }
        });

        btnVolleyball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "volleyball").execute();
            }
        });

        btnBoxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "boxing").execute();
            }
        });

        btnMotoGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "moto_gp").execute();
            }
        });

        btnUSFootball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "football").execute();
            }
        });

        btnHandball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "handball").execute();
            }
        });

        btnRugby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "rugby").execute();
            }
        });

        btnHockey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "hockey").execute();
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new prepareSportsData(Constant.SPORTS_MYP2P_URL, "others").execute();
            }
        });

        new prepareSportsData(Constant.SPORTS_MYP2P_URL, "").execute();

        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private class prepareSportsData extends AsyncTask<String, Void, Void> {
        String url;
        String category;

        private prepareSportsData(String url, String category) {
            this.url = url;
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SportsMyp2pActivity.this);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                models.clear();
                Document doc = Jsoup.connect(url + category).timeout(20000).get();
                Elements body = doc.select("table[class=links]").select("tbody").select("tr");
                for (int i = 1; i < body.size(); i += 2){
                    String team = body.get(i).select("td[class=link]").text();
                    String time = body.get(i).select("td[class=time]").text();
                    Node node = body.get(i);
                    String urlNodeStr = node.nextSibling().toString();
                    SportsModel myp2pModel = new SportsModel();
                    myp2pModel.setTeam1(team);
                    myp2pModel.setTime(time);
                    myp2pModel.setLinkNodeString(urlNodeStr);
                    models.add(myp2pModel);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            adapter = new MyP2PAdapter(models, SportsMyp2pActivity.this);
            RecyclerView.LayoutManager mLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(SportsMyp2pActivity.this, 2);
            } else {
                mLayoutManager = new GridLayoutManager(SportsMyp2pActivity.this, 1);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.invalidate();
            recyclerView.setAdapter(adapter);

        }

    }
}