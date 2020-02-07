package com.evilkingmediabeta.cartoon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.VideoPlayerActivity;
import com.evilkingmediabeta.model.SportsModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AnimeUnityDetailAdapter extends RecyclerView.Adapter<AnimeUnityDetailAdapter.myview> implements Filterable {
    private List<SportsModel> movielistFiltered;
    private List<SportsModel> moviesList;
    Context context;
    private ProgressDialog mProgressDialog;
    private String videoPath;
    private int itemposition;




    public class myview extends RecyclerView.ViewHolder {

        private CardView itemCardView;
        private TextView filmTitle, filmTime;

        public myview(View view) {
            super(view);
            itemCardView = view.findViewById(R.id.sport_card_view);
            filmTitle = view.findViewById(R.id.sportDate);
            filmTime = view.findViewById(R.id.sportTime);
        }
    }

    public AnimeUnityDetailAdapter(List<SportsModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        this.movielistFiltered = moviesList;
    }

    @NonNull
    @Override
    public AnimeUnityDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sport_item, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeUnityDetailAdapter.myview holder, final int position) {

        SportsModel movie = moviesList.get(position);
        holder.filmTitle.setText(movie.getTitle());
        holder.filmTime.setText("");
        holder.filmTime.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.filmTime.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.filmTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.filmTitle.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.itemCardView.setOnClickListener(v -> {
            itemposition = position;
            new prepareMovieData().execute();
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private class prepareMovieData extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(moviesList.get(itemposition).getUrl()).timeout(10000).get();
                videoPath = doc.select("video").select("source").attr("src");

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

//            Constant.openWVCapp(context, videoPath);
            Log.e("AnimeUnityDetailAdapter", videoPath);

            if(!videoPath.equals("")) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("CHANNEL_URL", videoPath);
                context.startActivity(intent);
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
                    moviesList = movielistFiltered;
                } else {
                    List<SportsModel> filteredList = new ArrayList<>();
                    for (SportsModel row : movielistFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    moviesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = moviesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                moviesList = (ArrayList<SportsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
