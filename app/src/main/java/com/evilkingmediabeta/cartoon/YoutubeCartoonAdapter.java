package com.evilkingmediabeta.cartoon;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.LiveStreamViewerActivity;

import java.util.List;

public class YoutubeCartoonAdapter extends RecyclerView.Adapter<YoutubeCartoonAdapter.myview>  {
    private List<CartoonYoutubeModel> cartoonYoutubeModels;
    private Context context;


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

    public YoutubeCartoonAdapter(List<CartoonYoutubeModel> cartoonYoutubeModels, Context context) {
        this.context = context;
        this.cartoonYoutubeModels = cartoonYoutubeModels;
    }

    @NonNull
    @Override
    public YoutubeCartoonAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new YoutubeCartoonAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeCartoonAdapter.myview holder, final int position) {

        CartoonYoutubeModel model = cartoonYoutubeModels.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtTime.setText("");
        holder.txtTime.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtTime.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.txtTitle.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(model.getUrl())) {
                    Intent intent = new Intent(context, LiveStreamViewerActivity.class);
                    intent.putExtra("videoUrl", Constant.EMBED_VIDEO_PREFIX_LINK + model.getUrl());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "This video Url is empty. Please try later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartoonYoutubeModels.size();
    }

}

