package com.evilkingmediabeta.demand;

import android.content.Context;
import android.graphics.Color;
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

public class CinetecaWebCasterAdapter extends RecyclerView.Adapter<CinetecaWebCasterAdapter.myview> implements Filterable {
    private List<CinetecaModel> cinetecaModels;
    private List<CinetecaModel> cinetecaModelsFiltered;
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

    public CinetecaWebCasterAdapter(List<CinetecaModel> cinetecaModels, Context context) {
        this.context = context;
        this.cinetecaModels = cinetecaModels;
        this.cinetecaModelsFiltered = cinetecaModels;
    }

    @NonNull
    @Override
    public CinetecaWebCasterAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new CinetecaWebCasterAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CinetecaWebCasterAdapter.myview holder, final int position) {

        CinetecaModel model = cinetecaModels.get(position);
        holder.title.setText(model.getTitle());
        holder.title.setTextColor(Color.parseColor("#FFFFFF"));
        holder.card_view.setBackgroundResource(R.drawable.section_selection_background_whitetext);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(context, model.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cinetecaModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    cinetecaModels = cinetecaModelsFiltered;
                } else {
                    List<CinetecaModel> filteredList = new ArrayList<>();
                    for (CinetecaModel row : cinetecaModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cinetecaModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cinetecaModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cinetecaModels = (ArrayList<CinetecaModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

