package com.evilkingmediabeta.sports.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.SportsMyp2pDetailActivity;
import com.evilkingmediabeta.model.SportsModel;

public class MyP2PAdapter extends RecyclerView.Adapter<MyP2PAdapter.myview> implements Filterable {
    private List<SportsModel> sportsModels;
    private List<SportsModel> sportsModelsFiltered;
    private Context context;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTeam, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTeam = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public MyP2PAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
        this.sportsModelsFiltered = sportsModels;
    }

    @NonNull
    @Override
    public MyP2PAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new MyP2PAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyP2PAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTeam.setText(model.getTeam1());
        holder.txtTime.setText(model.getTime());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportsMyp2pDetailActivity.class);
                intent.putExtra("links", model.getLinkNodeString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    sportsModels = sportsModelsFiltered;
                } else {
                    List<SportsModel> filteredList = new ArrayList<>();
                    for (SportsModel row : sportsModelsFiltered) {
                        if (row.getTeam1().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sportsModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sportsModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sportsModels = (ArrayList<SportsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}