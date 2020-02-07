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

import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsCricfreeCategoryDetailActivity;
import com.evilkingmediabeta.model.SportsModel;

public class CricfreeCategoryDetailAdapter extends RecyclerView.Adapter<CricfreeCategoryDetailAdapter.myview>  {
    private List<SportsModel> sportsModels;
    private Context context;
    private ProgressDialog mProgressDialog;
    private String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTitle, txtEmpty;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTitle = view.findViewById(R.id.sportDate);
            txtEmpty = view.findViewById(R.id.sportTime);
        }
    }

    public CricfreeCategoryDetailAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public CricfreeCategoryDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new CricfreeCategoryDetailAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CricfreeCategoryDetailAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtEmpty.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportsCricfreeCategoryDetailActivity.class);
                intent.putExtra("url", model.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

}

