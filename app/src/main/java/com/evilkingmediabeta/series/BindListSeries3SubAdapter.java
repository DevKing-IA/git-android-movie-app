package com.evilkingmediabeta.series;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.adapter.BindListAdapter;
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.model.MoviesModel;

public class BindListSeries3SubAdapter extends RecyclerView.Adapter<BindListSeries3SubAdapter.myview> {
    private List<MoviesModel> episode_list;
    private List<MoviesModel> moviesList;
    Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;
    private int itemposition;
    BindListAdapter adapter;


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

    public BindListSeries3SubAdapter(List<MoviesModel> moviesList, Context context,List<MoviesModel> episode_list) {
        this.moviesList = moviesList;
        this.context = context;
        this.episode_list = moviesList;
    }

    @NonNull
    @Override
    public BindListSeries3SubAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_series3, parent, false);

        return new BindListSeries3SubAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BindListSeries3SubAdapter.myview holder, final int position) {

        final MoviesModel movie = moviesList.get(position);

        if (movie.getImage() == "") {
            holder.imgcontent.setImageResource(R.color.colorWhite);
            //Picasso.with(context).load(R.drawable.ic_image).into(holder.imgcontent);
        } else {
            Picasso.get().load(movie.getImage()).into(holder.imgcontent);
        }

        holder.txtMovieTitle.setText(movie.getTitle());
       /* holder.txtMovieRating.setText(movie.getRating());
        holder.txtMovieYear.setText(movie.getYear());
        holder.txtMovieDuration.setText(movie.getDuration());*/

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*  Intent webIntent = new Intent(context, MusicWebViewActivity.class);
                webIntent.putExtra("url",  moviesList.get(position).getUrl());
                context.startActivity(webIntent);
                itemposition = position;
                Intent i = new Intent(context, SeriesActivityCatServer3.class);
                Bundle b = new Bundle();
                b.putSerializable("episode", (Serializable) episode_list);
                i.putExtras(b);
                context.startActivity(i);*/
                new prepareMovieData().execute();

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

              /*  Elements data = doc.select("div[class=container-fluid]").select("div[class=nomargin]");
                Elements em = data.tagName("li").select("em");*/
                String url = doc.select("div[class=container]").select("p").select("a").attr("href");
                /*Elements data1 = data.select("div[class=span12 filmbox]").tagName("table").select("div[class=sp-wrap sp-wrap-default]");
                Elements data2 = data1.get(itemposition).select("div[class=sp-body]").select("strong");

                moviesList.clear();
                Log.d("data size", data1.size() + "");

                for (int i = 0; i <data2.size(); i++) {

                    String title = data1.get(i).select("div[class=sp-head unfolded]").text();
                    String title1 = null;
                    if (title.isEmpty()) {
                        title1 = data1.get(i).select("div[class=sp-head]").text();
                    }

                    MoviesModel movie = new MoviesModel();
                    if(!title.contains("SUB")) {
                        if (title.isEmpty()) {
                            movie.setTitle(title1);
                        } else {
                            movie.setTitle(title);
                        }
                    }

                    moviesList.add(movie);
                }*/

             /*   for(int i=0;i<em.size();i++)
                {

                    if(em.get(i).select("a").size()>0)
                    {
                        videoPath= em.get(i).select("a").attr("href");
                        break;
                    }
                }*/

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
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                Intent webIntent = new Intent(context, WebViewActivity.class);
                webIntent.putExtra("url", videoPath);
                context.startActivity(webIntent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }
          /*  Intent i = new Intent(context, SeriesActivityCatServer3.class);
            i.putExtra("url", videoPath);
            context.startActivity(i);*/
        }
    }

}






