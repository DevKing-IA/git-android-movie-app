package com.evilkingmediabeta.videoteca;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.LiveStreamViewerActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoTecaEmbedAdapter extends RecyclerView.Adapter<VideoTecaEmbedAdapter.myview> implements Filterable {
    private List<VideoEmbedModel> videoEmbedModels;
    private List<VideoEmbedModel> videoEmbedModelsFiltered;
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

    public VideoTecaEmbedAdapter(List<VideoEmbedModel> videoEmbedModels, Context context) {
        this.context = context;
        this.videoEmbedModels = videoEmbedModels;
        this.videoEmbedModelsFiltered = videoEmbedModels;
    }

    @NonNull
    @Override
    public VideoTecaEmbedAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livetv_list, parent, false);

        return new VideoTecaEmbedAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoTecaEmbedAdapter.myview holder, final int position) {

        VideoEmbedModel model = videoEmbedModels.get(position);
        holder.title.setText(model.getTitle());
        holder.card_view.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        holder.title.setTextColor(ContextCompat.getColor(context, R.color.white));
        //holder.title.setBackgroundResource(R.drawable.section_selection_background_whitetext);
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
        return videoEmbedModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    videoEmbedModels = videoEmbedModelsFiltered;
                } else {
                    List<VideoEmbedModel> filteredList = new ArrayList<>();
                    for (VideoEmbedModel row : videoEmbedModelsFiltered) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    videoEmbedModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = videoEmbedModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                videoEmbedModels = (ArrayList<VideoEmbedModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

