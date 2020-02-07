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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;
import com.evilkingmediabeta.sports.FootballOnDemandDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FootballAdapter extends RecyclerView.Adapter<FootballAdapter.myview> implements Filterable {
    private List<MoviesModel> moviesModels;
    private List<MoviesModel> moviesModelsFiltered;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView title;
        private ImageView imageView;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.itemCardView);
            title = view.findViewById(R.id.filmTitle);
            imageView = view.findViewById(R.id.filmImage);
        }
    }

    public FootballAdapter(List<MoviesModel> moviesModels, Context context) {
        this.context = context;
        this.moviesModels = moviesModels;
        this.moviesModelsFiltered = moviesModels;
    }

    @NonNull
    @Override
    public FootballAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);

        return new FootballAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FootballAdapter.myview holder, final int position) {

        MoviesModel model = moviesModels.get(position);
        Picasso.get().load(model.getImage()).into(holder.imageView);
        holder.title.setText(model.getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FootballOnDemandDetailActivity.class);
                intent.putExtra("url", model.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    moviesModels = moviesModelsFiltered;
                } else {
                    List<MoviesModel> filteredList = new ArrayList<>();
                    for (MoviesModel row : moviesModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    moviesModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = moviesModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                moviesModels = (ArrayList<MoviesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

