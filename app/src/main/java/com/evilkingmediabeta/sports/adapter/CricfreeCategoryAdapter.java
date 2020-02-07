package com.evilkingmediabeta.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsCricfreeCategoryDetailActivity;
import com.evilkingmediabeta.sports.SportsCricfreeDetailActivity;
import com.evilkingmediabeta.model.SportsModel;

public class CricfreeCategoryAdapter extends RecyclerView.Adapter<CricfreeCategoryAdapter.myview> implements Filterable {
    private List<SportsModel> sportsModels;
    private List<SportsModel> sportsModelsFiltered;
    private Context context;
    private ProgressDialog mProgressDialog;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtSportType, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtSportType = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public CricfreeCategoryAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
        this.sportsModelsFiltered = sportsModels;
    }

    @NonNull
    @Override
    public CricfreeCategoryAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new CricfreeCategoryAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CricfreeCategoryAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtSportType.setText(model.getTitle().toUpperCase());
        holder.txtTime.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetailOrPlay(model.getLinkNodeString());
            }
        });
    }

    private void goDetailOrPlay(String node){
        Document document = Jsoup.parse(node);
        Elements aTagElements = document.select("a");
        if(aTagElements.size() > 1){
            Intent intent = new Intent(context, SportsCricfreeDetailActivity.class);
            intent.putExtra("tags", node);
            context.startActivity(intent);
        } else {
            new goDetail(aTagElements.attr("href")).execute();
        }
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

    private class goDetail extends AsyncTask<String, Void, Void> {
        String url;
        String flag;

        private goDetail(String url) {
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
                Elements element = doc.select("div[class=match_list]");
                if(element.size() == 0){
                    flag = "empty";
                } else {
                    flag = "not empty";
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

            if(flag.equals("empty")){
                Toast.makeText(context, "This video is not available at the moment, but will be up soon.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(context, SportsCricfreeCategoryDetailActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }

        }

    }

}

