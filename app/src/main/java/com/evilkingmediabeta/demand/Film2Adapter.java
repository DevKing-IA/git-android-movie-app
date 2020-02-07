package com.evilkingmediabeta.demand;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.LiveStreamViewerActivity;
import com.evilkingmediabeta.share.ShareVideoActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Film2Adapter extends RecyclerView.Adapter<Film2Adapter.myview> implements Filterable {
    private List<Film2Model> sportEmbedScrapeModels;
    private List<Film2Model> sportEmbedScrapeModelsFiltered;
    private Context context;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView title;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.txtMovieTitle);
        }
    }

    public Film2Adapter(List<Film2Model> sportEmbedScrapeModels, Context context) {
        this.context = context;
        this.sportEmbedScrapeModels = sportEmbedScrapeModels;
        this.sportEmbedScrapeModelsFiltered = sportEmbedScrapeModels;
    }

    @NonNull
    @Override
    public Film2Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new Film2Adapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Film2Adapter.myview holder, final int position) {

        Film2Model model = sportEmbedScrapeModels.get(position);
        holder.title.setText(model.getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, model.getUrl(), Toast.LENGTH_SHORT).show();
                if(model.getUrl().endsWith(".m3u8")){
//                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    Intent intent = new Intent(context, ShareVideoActivity.class);
                    intent.putExtra("CHANNEL_NAME", model.getTitle());
                    intent.putExtra("CHANNEL_LOGO", "");
                    intent.putExtra("STREAM_TYPE", "live");
                    intent.putExtra("CHANNEL_URL", model.getUrl());
                    context.startActivity(intent);
                } else {
                    new prepareURI(Constant.EMBED_VIDEO_PREFIX_LINK + model.getUrl()).execute();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportEmbedScrapeModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    sportEmbedScrapeModels = sportEmbedScrapeModelsFiltered;
                } else {
                    List<Film2Model> filteredList = new ArrayList<>();
                    for (Film2Model row : sportEmbedScrapeModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sportEmbedScrapeModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sportEmbedScrapeModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sportEmbedScrapeModels = (ArrayList<Film2Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class prepareURI extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        String main_url;
        String videoUrl;

        prepareURI(String main_url) {
            this.main_url = main_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                Document doc = Jsoup.connect(main_url).timeout(30000).get();
                String url = doc.select("iframe").get(0).attr("src");
                if (!TextUtils.isEmpty(url.trim())) {
                    videoUrl = url;
                    return videoUrl;
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
                Intent intent = new Intent(context, LiveStreamViewerActivity.class);
                intent.putExtra("videoUrl", videoUrl);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

