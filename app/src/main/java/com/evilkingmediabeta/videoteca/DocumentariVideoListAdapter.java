package com.evilkingmediabeta.videoteca;

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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;

public class DocumentariVideoListAdapter extends RecyclerView.Adapter<DocumentariVideoListAdapter.myview> implements Filterable {
    private List<MoviesModel> models;
    private List<MoviesModel> modelsFiltered;
    private Context context;


    public class myview extends RecyclerView.ViewHolder {

        private CardView itemCardView;
        private ImageView filmImage;
        private TextView filmTitle;

        public myview(View view) {
            super(view);
            itemCardView = view.findViewById(R.id.itemCardView);
            filmImage = view.findViewById(R.id.filmImage);
            filmTitle = view.findViewById(R.id.filmTitle);
        }
    }

    public DocumentariVideoListAdapter(List<MoviesModel> models, Context context) {
        this.context = context;
        this.models = models;
        this.modelsFiltered = models;
    }

    @NonNull
    @Override
    public DocumentariVideoListAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);

        return new DocumentariVideoListAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentariVideoListAdapter.myview holder, final int position) {

        MoviesModel movie = models.get(position);

        Picasso.get().load(movie.getImage()).into(holder.filmImage);
        holder.filmTitle.setText(movie.getTitle());

        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentariVideoLinkActivity.class);
                intent.putExtra("url", movie.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    models = modelsFiltered;
                } else {
                    List<MoviesModel> filteredList = new ArrayList<>();
                    for (MoviesModel row : modelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    models = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = models;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                models = (ArrayList<MoviesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

