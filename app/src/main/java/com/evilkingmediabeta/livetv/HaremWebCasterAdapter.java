package com.evilkingmediabeta.livetv;

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

public class HaremWebCasterAdapter extends RecyclerView.Adapter<HaremWebCasterAdapter.myview> implements Filterable {
    private List<HaremModel> haremModels;
    private List<HaremModel> haremModelsFiltered;
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

    public HaremWebCasterAdapter(List<HaremModel> haremModels, Context context) {
        this.context = context;
        this.haremModels = haremModels;
        this.haremModelsFiltered = haremModels;
    }

    @NonNull
    @Override
    public HaremWebCasterAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new HaremWebCasterAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HaremWebCasterAdapter.myview holder, final int position) {

        HaremModel model = haremModels.get(position);
        holder.title.setText(model.getTitle());
        holder.card_view.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.title.setTextColor(Color.parseColor("#FFFFFF"));
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(context, model.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return haremModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    haremModels = haremModelsFiltered;
                } else {
                    List<HaremModel> filteredList = new ArrayList<>();
                    for (HaremModel row : haremModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    haremModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = haremModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                haremModels = (ArrayList<HaremModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

