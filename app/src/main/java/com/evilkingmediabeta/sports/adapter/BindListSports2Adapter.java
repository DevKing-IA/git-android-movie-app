package com.evilkingmediabeta.sports.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsStreamingActivity;
import com.evilkingmediabeta.model.SportsModel;

public class BindListSports2Adapter extends RecyclerView.Adapter<BindListSports2Adapter.myview> implements Filterable {
private List<SportsModel> sportsModelUrlList;
private List<SportsModel> sportsModelList;
        Context context;
        String videoPath;
private int itemposition;

    public class myview extends RecyclerView.ViewHolder {

    private CardView card_view;
    private TextView txtTeam1, txtTeam2, txtTime;


    public myview(View view) {
        super(view);
        card_view = view.findViewById(R.id.card_view);
        txtTeam1 = view.findViewById(R.id.txtTeam1);
        txtTeam2 = view.findViewById(R.id.txtTeam2);
        txtTime = view.findViewById(R.id.txtSportsTime);
    }
}

    public BindListSports2Adapter(List<SportsModel> sportsModelList, Context context, List<SportsModel> urlList) {
        this.sportsModelList = sportsModelList;
        this.context = context;
        this.sportsModelUrlList = sportsModelList;
    }

    @NonNull
    @Override
    public BindListSports2Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sports_2, parent, false);

        return new BindListSports2Adapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindListSports2Adapter.myview holder, final int position) {

        final SportsModel sportsModel = sportsModelList.get(position);

        holder.txtTime.setText(sportsModel.getTime());
        holder.txtTeam1.setText(sportsModel.getTeam1());
        holder.txtTeam2.setText(sportsModel.getTeam2());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;
                new prepareSportsUrl(sportsModelList.get(position).getUrl()).execute();
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
            for(int i = 0 ;i<table.size();i++){
                if(i==table.size() - 1){
                    Elements td = table.get(i).select("td[class=event-watch]");
                    Log.e("column data", td + "");
                    String a = td.select("a").attr("href");
                    streaming = true;
                    if(a == null || a.isEmpty())
                    {
                        streaming = false;
                        urldata = table.get(i).select("td").text();
                    }
                }

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
                            if (row.getTeam1().toLowerCase().contains(charString.toLowerCase()) || row.getTeam2().toLowerCase().contains(charString.toLowerCase())) {
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
