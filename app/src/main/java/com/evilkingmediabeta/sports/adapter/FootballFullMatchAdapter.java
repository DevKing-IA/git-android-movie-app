package com.evilkingmediabeta.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;
import com.evilkingmediabeta.sports.FootballFullMatchActivity;
import com.evilkingmediabeta.sports.FootballFullMatchDetailActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class FootballFullMatchAdapter extends RecyclerView.Adapter<FootballFullMatchAdapter.myview> {
    private List<SportsModel> sportsModels;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView title, time;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            title = view.findViewById(R.id.sportDate);
            time = view.findViewById(R.id.sportTime);
        }
    }

    public FootballFullMatchAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public FootballFullMatchAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new FootballFullMatchAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FootballFullMatchAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(model.getLinkNodeString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    private void showDetail(String childNodes){
        Document doc = Jsoup.parse(childNodes);
        if (doc.select("li").size() == 0){
            Intent intent = new Intent(context, FootballFullMatchDetailActivity.class);
            intent.putExtra("url", doc.select("a").attr("href"));
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, FootballFullMatchActivity.class);
            intent.putExtra("childNodes", childNodes);
            context.startActivity(intent);
        }
    }

}

