package com.evilkingmediabeta.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class FootballDetailAdapter extends RecyclerView.Adapter<FootballDetailAdapter.myview> {
    private List<SportsModel> sportsModels;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView title, time;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            title = view.findViewById(R.id.sportDate);
            time = view.findViewById(R.id.sportTime);
        }
    }

    public FootballDetailAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public FootballDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new FootballDetailAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FootballDetailAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showVideo(model.getUrl()).execute();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    private class showVideo extends AsyncTask<String, Void, Void> {

        String url;

        private showVideo(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Connection.Response response = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("http://www.google.com")
                        .method(Connection.Method.POST)//or Method.POST
                        .followRedirects(true)
                        .execute();

                String table = response.body();

                Document doc = Jsoup.parse(table);
                String str = doc.select("div[class=tab-content]").select("iframe").attr("src");
                if (str.contains("http")){
                    videoPath = str;
                } else {
                    videoPath = "http:" + str;
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
            Constant.openWVCapp(context, videoPath);
        }

    }

}

