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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;

public class CricfreeAdapter extends RecyclerView.Adapter<CricfreeAdapter.myview> implements Filterable {
    private List<SportsModel> sportsModels;
    private List<SportsModel> sportsModelsFiltered;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTeam, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTeam = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public CricfreeAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
        this.sportsModelsFiltered = sportsModels;
    }

    @NonNull
    @Override
    public CricfreeAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new CricfreeAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CricfreeAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTeam.setText(model.getTeam1());
        holder.txtTime.setText(model.getTime());
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
                        if (row.getTeam1().toLowerCase().contains(charString.toLowerCase())) {
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

    private class showVideo extends AsyncTask<String, Void, Void> {
        String url;

        private showVideo(String url) {
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

                Document doc = Jsoup.connect(url).timeout(20000).get();
                videoPath = doc.select("div[class=video_plyer]").select("iframe").attr("src");

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

//            Intent intent = new Intent(context, WebViewActivity.class);
//            intent.putExtra("videoUrl", videoPath);
//            context.startActivity(intent);
            Constant.openWVCapp(context, videoPath);

        }

    }

}

