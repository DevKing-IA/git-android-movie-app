package com.evilkingmediabeta.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.LiveStreamViewerActivity;
import com.evilkingmediabeta.model.SportsModel;

public class SoccerSecondAdapter extends RecyclerView.Adapter<SoccerSecondAdapter.myview> implements Filterable {
    private List<SportsModel> sportsModels;
    private List<SportsModel> sportsModelsFiltered;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTitle, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTitle = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public SoccerSecondAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
        this.sportsModelsFiltered = sportsModels;
    }

    @NonNull
    @Override
    public SoccerSecondAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new SoccerSecondAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SoccerSecondAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtTime.setText(model.getTime());
        holder.txtTime.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtTime.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtTitle.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new viewMatches(model.getUrl()).execute();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    sportsModels = sportsModelsFiltered;
                } else {
                    List<SportsModel> filteredList = new ArrayList<>();
                    for (SportsModel row : sportsModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sportsModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sportsModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sportsModels = (ArrayList<SportsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class viewMatches extends AsyncTask<String, Void, String> {
        String url;

        private viewMatches(String url) {
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
        protected String doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(url).timeout(60000).get();
                String temp = doc.select("div[class=td-post-content tagdiv-type]").select("div[class=acp_content]").select("iframe").attr("src");
                if (!TextUtils.isEmpty(temp.trim())) {
                    if (temp.contains("http")) {
                        videoPath = temp;
                    } else {
                        videoPath = "https:" + temp;
                    }

                    return videoPath;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if (!TextUtils.isEmpty(result)) {
                Intent intent = new Intent(context, LiveStreamViewerActivity.class);
                intent.putExtra("videoUrl", videoPath);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

