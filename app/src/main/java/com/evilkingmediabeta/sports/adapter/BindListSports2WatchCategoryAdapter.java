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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsStreamingActivity;
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.model.SportsModel;

public class BindListSports2WatchCategoryAdapter extends RecyclerView.Adapter<BindListSports2WatchCategoryAdapter.myview>  {
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
            txtMovieDuration = view.findViewById(R.id.txtMovieDuration);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
    }

    public BindListSports2WatchCategoryAdapter(List<SportsModel> sportsModelList, Context context, List<SportsModel> urlList) {
        this.sportsModelList = sportsModelList;
        this.context = context;
        this.sportsModelUrlList = urlList;
    }

    @NonNull
    @Override
    public BindListSports2WatchCategoryAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new BindListSports2WatchCategoryAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindListSports2WatchCategoryAdapter.myview holder, final int position) {

        final SportsModel sportsModel = sportsModelList.get(position);

        holder.txtMovieTitle.setText(sportsModel.getTitle());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;

                String url = sportsModelList.get(position).getUrl();
                String videoUrl = url.contains(",") ? url.split(",")[0] : url;

                if (!TextUtils.isEmpty(videoUrl)) {
                    Intent webIntent = new Intent(context, WebViewActivity.class);

                    webIntent.putExtra("videoUrl", videoUrl);
                    context.startActivity(webIntent);
                } else {
                    Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
                }
                //new prepareSportsUrl(sportsModelList.get(position).getUrl()).execute();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModelList.size();
    }

    private class prepareSportsUrl extends AsyncTask<String, Void, Void> {
        String mainurl;
        String urldata;
        String url;
        boolean streaming = false;
        public prepareSportsUrl(String mainurl) {

            this.mainurl = mainurl;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {

            //Movie1
            StringBuilder myName = new StringBuilder(mainurl);
            myName.setCharAt(0, ' ');
            url = Constant.SPORTSURL2 + myName;
            Document doc = null;
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //For Categories
            Elements container = doc.select("div[class=container mtb]");
            Elements table = container.select("table[class=table table-striped]");
            Elements td = table.select("td[class=event-watch]");
            String a = td.select("a").attr("href");
            streaming = true;
            if(a == null || a.isEmpty())
            {
                streaming = false;
                urldata = table.select("td").text();
            }
          /*  else
            {
                streaming = true;
                Elements mElementUrl = td.select("a");
                String url_str = mElementUrl.attr("href");
                String url1[] = url_str.split("javascript:window.open\\(");
                String url2[] = url1[1].split("\\)");
                String data = url2[0].replace("'","");
                if(data.contains("youtube")) {
                    urldata = data.replace("http", "https");
                }
                else
                {
                    urldata = data;
                }*/
            //}


            return null;
        }
        @Override
        protected void onPostExecute (Void result){
            // Set description into TextView

            if (streaming == false) {
                Toast.makeText(context, urldata, Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(context, SportsStreamingActivity.class);
                i.putExtra("url", url);
                context.startActivity(i);
            }

        /*if(streaming) {
            Intent webIntent = new Intent(context, MusicWebViewActivity.class);
            String SpilString = urldata;
            String[] separated = urldata.split(",");
            for (String item : separated) {
                System.out.println("item = " + item);
            }
            urldata = separated[0];
            webIntent.putExtra("url", urldata);
            context.startActivity(webIntent);
        }
        else
        {
            Toast.makeText(context,urldata,Toast.LENGTH_LONG).show();
        }*/





        }

    }
}

