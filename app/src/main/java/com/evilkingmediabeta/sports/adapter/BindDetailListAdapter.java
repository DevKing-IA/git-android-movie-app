package com.evilkingmediabeta.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.model.SportsModel;

public class BindDetailListAdapter extends RecyclerView.Adapter<BindDetailListAdapter.myview> implements Filterable {
    private List<SportsModel> details;
    private List<SportsModel> detailsFiltered;
    Context context;
    String videoPath;
    private int itemposition;
    ProgressDialog mProgressDialog;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtDate, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtDate = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public BindDetailListAdapter(Context context, List<SportsModel> details) {
        this.details = details;
        this.detailsFiltered = details;
        this.context = context;
    }

    @NonNull
    @Override
    public BindDetailListAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new BindDetailListAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindDetailListAdapter.myview holder, final int position) {
        final SportsModel sportsModel = details.get(position);
        holder.txtDate.setText(sportsModel.getTeam1());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new playSports(sportsModel.getUrl()).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    private class playSports extends AsyncTask<String, Void, String> {
        String videoUrl;

        private playSports(String videoUrl) {
            this.videoUrl = videoUrl;
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

                Document doc = Jsoup.connect(videoUrl).timeout(10000).get();
                String url = doc.select("iframe").attr("src");

                if (!TextUtils.isEmpty(url.trim())) {
                    videoPath = url;
                    return videoPath;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute (String result){

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if (!TextUtils.isEmpty(result)) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("videoUrl", videoPath);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    details = detailsFiltered;
                } else {
                    List<SportsModel> filteredList = new ArrayList<>();
                    for (SportsModel row : detailsFiltered) {
                        if (row.getTeam1().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    details = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = details;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                details = (ArrayList<SportsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

