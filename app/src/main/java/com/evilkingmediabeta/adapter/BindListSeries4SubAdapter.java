package com.evilkingmediabeta.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.series.SeriesActivityCatServer4;
import com.evilkingmediabeta.webview.FixiFrameView;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class BindListSeries4SubAdapter extends RecyclerView.Adapter<BindListSeries4SubAdapter.myview> {
    private List<MoviesModel> movielistFiltered;
    private List<MoviesModel> moviesList;
    Context context;
    Base64 base64;
    private ProgressDialog mProgressDialog;
    String videoPath, episodeurl;
    private int itemposition;
    BindListAdapter adapter;
    private List<MoviesModel> seriesList = new ArrayList<>();

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

    public BindListSeries4SubAdapter(List<MoviesModel> moviesList, Context context, String episodeurl) {
        this.moviesList = moviesList;
        this.context = context;
        this.movielistFiltered = moviesList;
        this.episodeurl = episodeurl;
    }

    @NonNull
    @Override
    public BindListSeries4SubAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new BindListSeries4SubAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BindListSeries4SubAdapter.myview holder, final int position) {

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

                if(episodeurl != null){
                    Intent i = new Intent(context, SeriesActivityCatServer4.class);
                    i.putExtra("episodeurl", episodeurl);
                    context.startActivity(i);
                }
                else{
                    new prepareMovieData().execute();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private class prepareMovieData extends AsyncTask<Void, Void, String> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {


                Document doc = Jsoup.connect(moviesList.get(itemposition).getUrl()).timeout(10000).maxBodySize(0).get();

                Elements iframe = doc.getElementsByClass("container").first().getElementsByTag("iframe");
                String src = iframe.attr("src");
                String fixiframe = src.replace("https://protectlink.stream/embed.php?data=","http://api.supermyspace.xyz/FIX/fixiframe.php?data=");

                if (!TextUtils.isEmpty(src)) {
                    Log.e("body", fixiframe);
                    videoPath = fixiframe;
                    return videoPath;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                Intent webIntent = new Intent(context, FixiFrameView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", videoPath);
                webIntent.putExtras(bundle);
                context.startActivity(webIntent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }

        }

    }


}
