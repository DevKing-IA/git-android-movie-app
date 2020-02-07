package com.evilkingmediabeta.livetv;

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

public class TvWebCasterAdapter extends RecyclerView.Adapter<TvWebCasterAdapter.myview> implements Filterable {
    private List<TvModel> cartoonModels;
    private List<TvModel> cartoonModelsFiltered;
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

    public TvWebCasterAdapter(List<TvModel> cartoonModels, Context context) {
        this.context = context;
        this.cartoonModels = cartoonModels;
        this.cartoonModelsFiltered = cartoonModels;
    }

    @NonNull
    @Override
    public TvWebCasterAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new TvWebCasterAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvWebCasterAdapter.myview holder, final int position) {

        TvModel model = cartoonModels.get(position);
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
        return cartoonModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    cartoonModels = cartoonModelsFiltered;
                } else {
                    List<TvModel> filteredList = new ArrayList<>();
                    for (TvModel row : cartoonModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cartoonModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cartoonModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cartoonModels = (ArrayList<TvModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

