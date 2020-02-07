package com.evilkingmediabeta.sports.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsCricfreeActivity;
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.model.SportsModel;

public class BindListSports3Adapter extends RecyclerView.Adapter<BindListSports3Adapter.myview> implements Filterable {
    private List<SportsModel> sportsModelUrlList;
    private List<SportsModel> sportsModelList;
    Context context;
    String videoPath;
    private int itemposition;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private ImageView imgcontent;
        private TextView txtMovieTitle, txtMovieRating, txtMovieYear, txtMovieDuration;
        private View view1, view2;


        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.card_view);
            imgcontent = view.findViewById(R.id.imgcontent);
            txtMovieTitle = view.findViewById(R.id.txtMovieTitle);
            txtMovieRating = view.findViewById(R.id.txtMovieRating);
            txtMovieYear = view.findViewById(R.id.txtMovieYear);
            txtMovieYear.setTextSize(14);
            txtMovieDuration = view.findViewById(R.id.txtMovieDuration);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            txtMovieRating.setVisibility(View.GONE);
            txtMovieDuration.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
    }

    public BindListSports3Adapter(List<SportsModel> sportsModelList, Context context, List<SportsModel> urlList) {
        this.sportsModelList = sportsModelList;
        this.context = context;
        this.sportsModelUrlList = sportsModelList;
    }

    @NonNull
    @Override
    public BindListSports3Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new BindListSports3Adapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindListSports3Adapter.myview holder, final int position) {

        final SportsModel sportsModel = sportsModelList.get(position);

        holder.txtMovieTitle.setText(sportsModel.getTime());
        holder.txtMovieYear.setText(sportsModel.getTitle());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;
                Intent i = new Intent(context, SportsCricfreeActivity.class);
                i.putExtra("url", sportsModelList.get(position).getUrl());
                i.putExtra("categoryid", sportsModelList.get(position).getId());
                i.putExtra("position", itemposition );
                context.startActivity(i);
                //new prepareSportsData().execute();
            }
        });

    }

    private class prepareSportsData extends AsyncTask<Void, Void, String> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String url = Constant.SPORTSURL3 + "/" + sportsModelList.get(itemposition).getUrl();

                Document doc = Jsoup.connect(url).timeout(10000).maxBodySize(0).get();

                Elements data = doc.select("tr[class=sectiontableentry2]").first().getElementsByTag("td").select("a");

                String videoUrl = data.attr("href");
                if (!TextUtils.isEmpty(videoUrl)) {
                    videoPath = videoUrl;
                    return videoPath;
                }
                // System.out.print(data);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                Intent webIntent = new Intent(context, WebViewActivity.class);
                webIntent.putExtra("videoUrl", videoPath);
                context.startActivity(webIntent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return sportsModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    sportsModelList = sportsModelUrlList;
                } else {
                    List<SportsModel> filteredList = new ArrayList<>();
                    for (SportsModel row : sportsModelList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sportsModelList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sportsModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sportsModelList = (ArrayList<SportsModel>) filterResults.values;
                notifyDataSetChanged();


            }
        };
    }

}
