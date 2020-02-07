package com.evilkingmediabeta.sports.ekmsportsembed.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.ekmsportsembed.model.CommonModels;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;




    public EpisodeAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;

    }


    @Override
    public EpisodeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpisodeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_episode, parent, false);
        vh = new EpisodeAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EpisodeAdapter.OriginalViewHolder holder, final int position) {

        CommonModels obj = items.get(position);
        holder.name.setText(obj.getTitle());

        Log.e("Season Name::",obj.getTitle());




        DirectorApater directorApater=new DirectorApater(ctx,obj.getListEpi(),obj.getTitle());
        Log.e("List", String.valueOf(obj.getTitle()));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(directorApater);
        holder.recyclerView.setBackgroundResource(R.drawable.anditcouldnotbedone_lmao);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RecyclerView recyclerView;


        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            recyclerView=v.findViewById(R.id.recyclerView);
            recyclerView.setBackgroundResource(R.drawable.anditcouldnotbedone_lmao);

        }
    }

}