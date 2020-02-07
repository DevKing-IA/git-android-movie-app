package com.evilkingmediabeta.cartoon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.VideoPlayerActivity;
import com.evilkingmediabeta.model.MoviesModel;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.myview> implements Filterable {
    private List<MoviesModel> movielistFiltered;
    private List<MoviesModel> moviesList;
    private List<String> episodes = new ArrayList<>();
    Context context;
    private ProgressDialog mProgressDialog;

    public class myview extends RecyclerView.ViewHolder {

        private CardView itemCardView;
        private ImageView filmImage;
        private TextView filmTitle;

        public myview(View view) {
            super(view);
            itemCardView = view.findViewById(R.id.itemCardView);
            filmImage = view.findViewById(R.id.filmImage);
            filmTitle = view.findViewById(R.id.filmTitle);
        }
    }

    public OrderDetailAdapter(List<MoviesModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        this.movielistFiltered = moviesList;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.film_item, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.myview holder, final int position) {

        final MoviesModel movie = moviesList.get(position);

        Picasso.get().load(movie.getImage()).into(holder.filmImage);
        holder.filmTitle.setText(movie.getTitle());

        holder.itemCardView.setOnClickListener(v -> new showEpisodes(movie.getUrl()).execute());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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
                    List<MoviesModel> filteredList = new ArrayList<>();
                    for (MoviesModel row : movielistFiltered) {
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
                moviesList = (ArrayList<MoviesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class showEpisodes extends AsyncTask<String, Void, Void> {
        String mainurl;

        public showEpisodes(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                episodes.clear();
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                Elements body = doc.select("div[class=tab-content bg-light-gray]").get(1).select("div[class=ep-box col-lg-1 col-sm-1]");
                for (int i = 0; i < body.size(); i++){
                    String title = body.get(i).select("a").text();
                    String url = Constant.ANIMEUNITY_SITE_URL + body.get(i).select("a").attr("href");
                    episodes.add(title + "###" + url);
                }

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

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.list_dialog_view);

            ListView dialog_list_view = dialog.findViewById(R.id.dialog_listview);
            ListAdapter adapter = new ListAdapter(context, 0, episodes);
            dialog_list_view.setAdapter(adapter);

            dialog_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    new playVideo(episodes.get(i).split("###")[1]).execute();
                }
            });

            dialog.getWindow().setLayout(context.getResources().getDimensionPixelSize(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

        }
    }

    private class playVideo extends AsyncTask<String, Void, Void> {
        String mainurl;
        String video_path;

        public playVideo(String mainurl) {
            this.mainurl = mainurl;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Document doc = Jsoup.connect(mainurl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106")
                        .timeout(30000)
                        .get();
                video_path = doc.select("video").select("source").attr("src");

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

            Log.e("OrderDetailAdapter", video_path);

            if(!video_path.equals("")) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("CHANNEL_URL", video_path);
                context.startActivity(intent);
            }
//            Constant.openWVCapp(context, video_path);

        }
    }

    private class ListAdapter extends ArrayAdapter<String> {

        private ListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            String string = getItem(position);

            TextView titleTextView = v.findViewById(android.R.id.text1);
            titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if(string != null) {
                titleTextView.setText(string.split("###")[0]);
                //titleTextView.setOnClickListener(v1 -> new playVideo(string.split("###")[1]).execute());
            }


            return v;
        }
    }

}
