package com.evilkingmediabeta.videoteca;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MoviesModel;

public class DocumentariVideoLinkAdapter extends RecyclerView.Adapter<DocumentariVideoLinkAdapter.myview> {
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

    public DocumentariVideoLinkAdapter(List<MoviesModel> models, Context context) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public DocumentariVideoLinkAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new DocumentariVideoLinkAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentariVideoLinkAdapter.myview holder, final int position) {

        MoviesModel model = models.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtTime.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(context, model.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}

