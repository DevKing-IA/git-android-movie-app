package com.evilkingmediabeta.videoteca;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;

public class DocumentariAdapter extends RecyclerView.Adapter<DocumentariAdapter.myview> {
    private List<MoviesModel> models;
    private Context context;
    private ProgressDialog mProgressDialog;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTitle, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTitle = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public DocumentariAdapter(List<MoviesModel> models, Context context) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public DocumentariAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new DocumentariAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentariAdapter.myview holder, final int position) {

        MoviesModel model = models.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtTime.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentariVideoListActivity.class);
                intent.putExtra("url", model.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}

