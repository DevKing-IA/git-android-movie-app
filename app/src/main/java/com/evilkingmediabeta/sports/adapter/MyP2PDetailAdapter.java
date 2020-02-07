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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;

public class MyP2PDetailAdapter extends RecyclerView.Adapter<MyP2PDetailAdapter.myview>  {
    private List<SportsModel> sportsModels;
    private Context context;
    private ProgressDialog mProgressDialog;
    private String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTitle;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTitle = view.findViewById(R.id.sportDate);
        }
    }

    public MyP2PDetailAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public MyP2PDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new MyP2PDetailAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyP2PDetailAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getUrl().contains("https://") || model.getUrl().contains("http://")){
                    new viewChannel(model.getUrl()).execute();
                } else {
                    new viewChannel(Constant.SPORTS_MYP2P_URL + model.getUrl()).execute();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    private class viewChannel extends AsyncTask<String, Void, Void> {
        String url;

        private viewChannel(String url){
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

                Document doc = Jsoup.connect(url).timeout(10000).get();
                videoPath = doc.select("div[class=all]").select("center").get(0).select("iframe").get(1).attr("src");

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

            if(videoPath != null){
                Constant.openWVCapp(context, videoPath);
            } else {
                Toast.makeText(context, "This link is off. Please try again later.", Toast.LENGTH_LONG).show();
            }

        }

    }

}