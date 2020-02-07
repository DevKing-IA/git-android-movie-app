package com.evilkingmediabeta.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.series.SeriesActivityCatServer4;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BindListSeries4Adapter extends RecyclerView.Adapter<BindListSeries4Adapter.myview> {
    private List<MoviesModel> movielistFiltered;
    private List<MoviesModel> moviesList;
    Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;
    private int itemposition;
    BindListAdapter adapter;
    private ArrayList<MoviesModel> seriesList = new ArrayList<MoviesModel>();

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private ImageView imgcontent;
        private TextView txtMovieTitle, txtMovieRating, txtMovieYear, txtMovieDuration;
        View view1, view2;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.card_view);
            imgcontent = view.findViewById(R.id.imgcontent);
            txtMovieTitle = view.findViewById(R.id.txtMovieTitle);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            txtMovieRating = view.findViewById(R.id.txtMovieRating);
            txtMovieYear = view.findViewById(R.id.txtMovieYear);
            txtMovieDuration = view.findViewById(R.id.txtMovieDuration);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            txtMovieRating.setVisibility(View.GONE);
            txtMovieYear.setVisibility(View.GONE);
            txtMovieDuration.setVisibility(View.GONE);

        }
    }

    public BindListSeries4Adapter(List<MoviesModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        this.movielistFiltered = moviesList;
    }

    @NonNull
    @Override
    public BindListSeries4Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BindListSeries4Adapter.myview holder, final int position) {

        final MoviesModel movie = moviesList.get(position);

        if (movie.getImage() == "") {
            holder.imgcontent.setImageResource(R.color.colorWhite);
        } else {
            Picasso.get().load(movie.getImage()).into(holder.imgcontent);
        }

        holder.txtMovieTitle.setText(movie.getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;
                new prepareMovieData().execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private class prepareMovieData extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Document doc = Jsoup.connect(moviesList.get(itemposition).getUrl()).timeout(10000).maxBodySize(0).get();

                Elements iframe = doc.getElementsByClass("float-left").first().getElementsByTag("iframe");
                String src = iframe.attr("src");

                Log.e("body", src);

                videoPath =  src;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
             /*Intent webIntent = new Intent(context, MusicWebViewActivity.class);
            webIntent.putExtra("url", videoPath);
            context.startActivity(webIntent);*/
             new prepareSeriesData().execute();

        }
    }


    private class prepareSeriesData extends AsyncTask<Void, Void, Void> {
        String desc;
        String url;
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


                Document doc = Jsoup.connect(videoPath).timeout(10000).maxBodySize(0).get();
                Elements main = doc.select("nav[class=navbar navbar-fixed-top navbar-default nav1]");
                Elements ul = main.select("ul[class=nav navbar-nav]");
                Elements li = ul.select("li");
                seriesList.clear();
                for(int i=0;i<li.size();i++)
                {

                     url = li.get(i).select("a").attr("href");

                    String episode = li.get(i).getElementsByTag("a").text();
                    MoviesModel movie = new MoviesModel();

                    movie.setUrl(url);
                    movie.setTitle(episode);
                    seriesList.add(movie);
                }
              /*  Elements iframe = doc.getElementsByClass("container").first().getElementsByTag("iframe");
                String src = iframe.attr("src");



                videoPath =  src;
*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           /*  Intent webIntent = new Intent(context, MusicWebViewActivity.class);
            webIntent.putExtra("url", videoPath);
            context.startActivity(webIntent);*/
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            Intent sub = new Intent(context, SeriesActivityCatServer4.class);
            sub.putExtra("url", url);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) seriesList);
            sub.putExtras(bundle);
            context.startActivity(sub);


        }
    }
}
