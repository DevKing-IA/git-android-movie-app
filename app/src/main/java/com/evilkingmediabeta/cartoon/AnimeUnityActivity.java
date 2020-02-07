package com.evilkingmediabeta.cartoon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.VideoPlayerActivity;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

public class AnimeUnityActivity extends AppCompatActivity {

    Button homeBtn, archiveBtn, genreBtn;
    RecyclerView homeRecyclerView;
    ListView orderListView, genreListView, genreDetailListView;
    RecyclerView orderDetailListView;
    EditText etHomeSearch;
    LinearLayout homeLy, archiveLy, genreLy;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    private ProgressDialog mProgressDialog;
    String btnFlag = "home";
    int page_num = 1;
    AnimeUnityAdapter mAdapter;

    private List<MoviesModel> cartoonList = new ArrayList<>();
    private List<MoviesModel> orderDetailList = new ArrayList<>();
    private List<String> elementStr = new ArrayList<>();
    private List<String> orders = new ArrayList<>();
    private List<String> orderDetails = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<String> genreTitles = new ArrayList<>();
    private List<String> genreTitleTags = new ArrayList<>();
    private List<String> genreTitleUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_unity);

        homeBtn = findViewById(R.id.btnHome);
        archiveBtn = findViewById(R.id.btnArchive);
        genreBtn = findViewById(R.id.btnGenre);

        homeLy = findViewById(R.id.homeLy);
        archiveLy = findViewById(R.id.archiveLy);
        genreLy = findViewById(R.id.genreLy);

        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        orderListView = findViewById(R.id.orderList);
        orderDetailListView = findViewById(R.id.orderDetailList);
        genreListView = findViewById(R.id.genreList);
        genreDetailListView = findViewById(R.id.genreDetailList);

        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);

        ivPrev.setVisibility(View.GONE);

        orderDetailListView.setLayoutManager(new LinearLayoutManager(this));
        orderDetailListView.setItemAnimator(new DefaultItemAnimator());

        etHomeSearch = findViewById(R.id.etHomeSearch);

        etHomeSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(AnimeUnityActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(AnimeUnityActivity.this, etHomeSearch.getWindowToken());
                }
            }
        });

        etHomeSearch.addTextChangedListener(new TextWatcher() {
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

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFlag = "home";
                homeLy.setVisibility(View.VISIBLE);
                archiveLy.setVisibility(View.GONE);
                genreLy.setVisibility(View.GONE);
                page_num = 1;
                new prepareData(Constant.ANIMEUNITY_SITE_URL, "index.php", page_num).execute();
            }
        });

        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFlag = "archive";
                archiveLy.setVisibility(View.VISIBLE);
                homeLy.setVisibility(View.GONE);
                genreLy.setVisibility(View.GONE);
                new prepareData(Constant.ANIMEUNITY_SITE_URL, "anime.php?c=archive", 0).execute();
            }
        });

        genreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFlag = "genre";
                genreLy.setVisibility(View.VISIBLE);
                homeLy.setVisibility(View.GONE);
                archiveLy.setVisibility(View.GONE);
                new prepareData(Constant.ANIMEUNITY_SITE_URL, "anime.php?c=genres", 0).execute();
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num ++;
                new prepareData(Constant.ANIMEUNITY_SITE_URL, "index.php", page_num).execute();
            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num --;
                new prepareData(Constant.ANIMEUNITY_SITE_URL, "index.php", page_num).execute();
            }
        });
        ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeRecyclerView.smoothScrollBy(0, -200);
            }
        });

        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeRecyclerView.smoothScrollBy(0, 200);
            }
        });

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new showOrderDetail(orderDetails.get(position)).execute();
            }
        });

        genreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showGenreTitleList(position);
            }
        });

        genreDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(AnimeUnityActivity.this, AnimeUnityDetailActivity.class);
//                intent.putExtra("url", genreTitleUrls.get(position));
//                startActivity(intent);
                new showEpisodes(genreTitleUrls.get(position)).execute();
            }
        });

        new prepareData(Constant.ANIMEUNITY_SITE_URL, "index.php", page_num).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    private void showOrderList(){
        orders.clear();
        orderDetails.clear();
        for (int i = 0; i < elementStr.size(); i++){
            Document document = Jsoup.parse(elementStr.get(i));
            String order = document.select("a").text();
            String orderUrl = Constant.ANIMEUNITY_SITE_URL + document.select("a").attr("href");
            orders.add(order);
            orderDetails.add(orderUrl);
        }
        OrderListAdapter orderListAdapter = new OrderListAdapter(AnimeUnityActivity.this, 0, orders);
        orderListView.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
    }

    private void showGenreList(){
        OrderListAdapter orderListAdapter = new OrderListAdapter(AnimeUnityActivity.this, 0, genres);
        genreListView.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
    }

    private void showGenreTitleList(int pos){
        genreTitles.clear();
        genreTitleUrls.clear();
        Document document = Jsoup.parse(genreTitleTags.get(pos));
        Elements body = document.select("h5");
        for (int i = 0; i < body.size(); i++){
            String title = body.get(i).select("a").text();
            String url = Constant.ANIMEUNITY_SITE_URL + body.get(i).select("a").attr("href");
            genreTitles.add(title);
            genreTitleUrls.add(url);
        }
        OrderListAdapter orderListAdapter = new OrderListAdapter(AnimeUnityActivity.this, 0, genreTitles);
        genreDetailListView.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
    }

    private class prepareData extends AsyncTask<String, Void, Void> {
        String mainurl;
        String category;
        int pNum;

        public prepareData(String mainurl, String category, int pNum) {
            this.mainurl = mainurl;
            this.category = category;
            this.pNum = pNum;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AnimeUnityActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                if(btnFlag.equals("home")){
                    cartoonList.clear();
                    Document doc = Jsoup.connect(mainurl + category + "?page=" + pNum)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                            .timeout(30000)
                            .get();
                    Elements body = doc.select("div[class=row text-center]").get(0).select("div[class=col-lg-3 col-md-6 col-sm-6 col-xs-6 mobile-col]");
                    for (int i = 0; i < body.size(); i++){
                        String title = body.get(i).select("div[class=card anime-card btn-light-gray]").select("div[class=card-footer btn-light-gray small]").select("a").text();
                        String url = mainurl + body.get(i).select("div[class=card anime-card btn-light-gray]").select("a").get(1).attr("href");
                        String image = body.get(i).select("img").attr("src");
                        MoviesModel model = new MoviesModel();
                        model.setTitle(title);
                        model.setImage(image);
                        model.setUrl(url);
                        cartoonList.add(model);
                    }

                } else {
                    Document doc = Jsoup.connect(mainurl + category)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                            .timeout(30000)
                            .get();
                    if(btnFlag.equals("archive")){
                        Elements element = doc.select("div[class=jumbotron bg-dark-gray3 text-white]").select("a");
                        for (int i = 1; i < element.size(); i++){
                            elementStr.add(element.get(i).toString());
                        }
                    } else {
                        genres.clear();
                        Elements body = doc.select("div[class=accordion bg-light-gray]").select("div[class=card btn-dark-gray1 text-white]");
                        for (int i = 0; i < body.size(); i++){
                            String genre = body.get(i).select("div[class=card-header]").select("button").text();
                            String genreTitleTag = body.get(i).select("div[class=card-body]").select("h5").toString();
                            genres.add(genre);
                            genreTitleTags.add(genreTitleTag);
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
            if(btnFlag.equals("home")){
                if(pNum == 1){
                    ivPrev.setVisibility(View.GONE);
                } else {
                    ivPrev.setVisibility(View.VISIBLE);
                }
                mAdapter = new AnimeUnityAdapter(cartoonList, AnimeUnityActivity.this);
                RecyclerView.LayoutManager mLayoutManager;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    mLayoutManager = new GridLayoutManager(AnimeUnityActivity.this, 3);
                } else {
                    mLayoutManager = new GridLayoutManager(AnimeUnityActivity.this, 2);
                }
                homeRecyclerView.setLayoutManager(mLayoutManager);
                homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
                homeRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else if(btnFlag.equals("archive")){
                showOrderList();
            } else {
                showGenreList();
            }
        }
    }

    private class showOrderDetail extends AsyncTask<String, Void, Void> {
        String mainurl;

        public showOrderDetail(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AnimeUnityActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                orderDetailList.clear();
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements body = doc.select("div[class=row text-center]").get(0).select("div[class=col-lg-4 col-md-6 col-sm-12]");
                for (int i = 0; i < body.size(); i++){
                    String title = body.get(i).select("div[class=card-block]").select("b").text();
                    String url = Constant.ANIMEUNITY_SITE_URL + body.get(i).select("div[class=card-img-top archive-card-img]").select("a").attr("href");
                    String image = body.get(i).select("img").attr("src");
                    MoviesModel model = new MoviesModel();
                    model.setTitle(title);
                    model.setImage(image);
                    model.setUrl(url);
                    orderDetailList.add(model);
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

            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderDetailList, AnimeUnityActivity.this);
            orderDetailListView.setAdapter(orderDetailAdapter);
            orderDetailAdapter.notifyDataSetChanged();

        }
    }

    private class showEpisodes extends AsyncTask<String, Void, Void> {
        String mainurl;
        List<String> episodes = new ArrayList<>();

        public showEpisodes(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AnimeUnityActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                episodes.clear();
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements body = doc.select("div[class=tab-content bg-light-gray]").get(1).select("div[class=ep-box col-lg-1 col-sm-1]");
                for (int i = 0; i < body.size(); i++){
                    String title = body.get(i).select("a").text();
                    String url = Constant.ANIMEUNITY_SITE_URL + body.get(i).select("a").attr("href");
                    episodes.add(title + "###" + url);
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

            Dialog dialog = new Dialog(AnimeUnityActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.list_dialog_view);

            ListView dialog_list_view = dialog.findViewById(R.id.dialog_listview);
            GenreListAdapter adapter = new GenreListAdapter(AnimeUnityActivity.this, 0, episodes);
            dialog_list_view.setAdapter(adapter);

            dialog_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    new playVideo(episodes.get(i).split("###")[1]).execute();
                }
            });
            dialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

        }
    }

    private class playVideo extends AsyncTask<String, Void, Void> {
        String mainurl;
        String video_path;

        public playVideo(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AnimeUnityActivity.this);
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
                video_path = doc.select("video").select("source").attr("src");

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

            Log.e("AnimeUnityActivity",video_path);

            if(!video_path.equals("")) {
                Intent intent = new Intent(AnimeUnityActivity.this, VideoPlayerActivity.class);
                intent.putExtra("CHANNEL_URL", video_path);
                startActivity(intent);
            }
//            Constant.openWVCapp(AnimeUnityActivity.this, video_path);

        }
    }

    private class OrderListAdapter extends ArrayAdapter<String> {

        private OrderListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
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

    private class GenreListAdapter extends ArrayAdapter<String> {

        private GenreListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            String string = getItem(position);

            TextView titleTextView = v.findViewById(android.R.id.text1);
            titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if(string != null) {
                titleTextView.setText(string.split("###")[0]);
                /*titleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new playVideo(string.split("###")[1]).execute();
                    }
                });*/
            }


            return v;
        }
    }
}
