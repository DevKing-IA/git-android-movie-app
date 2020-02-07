package com.evilkingmediabeta.sports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;

import java.util.ArrayList;
import java.util.List;

public class SportsWebCasterAdapter extends RecyclerView.Adapter<SportsWebCasterAdapter.myview> implements Filterable {
    private List<SportCastModel> sportCastModels;
    private List<SportCastModel> sportCastModelsFiltered;
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

    public SportsWebCasterAdapter(List<SportCastModel> sportCastModels, Context context) {
        this.context = context;
        this.sportCastModels = sportCastModels;
        this.sportCastModelsFiltered = sportCastModels;
    }

    @NonNull
    @Override
    public SportsWebCasterAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new SportsWebCasterAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SportsWebCasterAdapter.myview holder, final int position) {

        SportCastModel model = sportCastModels.get(position);
        holder.title.setText(model.getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(context, model.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportCastModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    sportCastModels = sportCastModelsFiltered;
                } else {
                    List<SportCastModel> filteredList = new ArrayList<>();
                    for (SportCastModel row : sportCastModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sportCastModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sportCastModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sportCastModels = (ArrayList<SportCastModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

