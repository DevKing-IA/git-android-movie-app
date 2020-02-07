package com.evilkingmediabeta.utilitymenu;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.evilkingmediabeta.model.MoviesModel;

public class BindListDetailsEPGAdpater extends RecyclerView.Adapter<BindListDetailsEPGAdpater.myview> implements Filterable {
    private List<MoviesModel> movielistFiltered;
    private List<MoviesModel> moviesList;
    Context context;

    public class myview extends RecyclerView.ViewHolder {


        private TextView txtTitle, txtCategory, txtTime;
        private CardView rlcard;

        public myview(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtCategory = view.findViewById(R.id.txtCategory);
            txtTime = view.findViewById(R.id.txtTime);
            rlcard = view.findViewById(R.id.card_view);
        }
    }

    public BindListDetailsEPGAdpater(List<MoviesModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        this.movielistFiltered = moviesList;

    }

    @NonNull
    @Override
    public BindListDetailsEPGAdpater.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.epg_details_item, parent, false);

        return new BindListDetailsEPGAdpater.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BindListDetailsEPGAdpater.myview holder, final int position) {

        final MoviesModel movie = moviesList.get(position);


        holder.txtTitle.setText(movie.getTitle());
        holder.txtCategory.setText(movie.getYear());
        holder.txtTime.setText(movie.getDuration());
        holder.txtTime.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtCategory.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.white));

        holder.rlcard.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.rlcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, EPGProgramDetailActivity.class);
                i.putExtra("pdetails", moviesList.get(position).getUrl());
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    moviesList = movielistFiltered;
                } else {
                    List<MoviesModel> filteredList = new ArrayList<>();
                    for (MoviesModel row : movielistFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    moviesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = moviesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                moviesList = (ArrayList<MoviesModel>) filterResults.values;
                notifyDataSetChanged();


            }
        };
    }



}

