
package com.evilkingmediabeta.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.MeteoModel;


public class MeteoAdapter extends RecyclerView.Adapter<MeteoAdapter.myview> implements Filterable {

    private List<String> moviesList;
    Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;
    private int itemposition;
    BindListAdapter adapter;
    private List<MeteoModel> meteoList;
    private List<MeteoModel> meteofilterList;

    public MeteoAdapter(List<MeteoModel> meteolist, Context meteoActivity) {
        this.meteoList=meteolist;
        this.context=meteoActivity;
        this.meteofilterList = meteolist;
    }


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private ImageView imgcontent;
        private TextView txtMovieTitle, txtMovieRating, txtMovieYear, txtMovieDuration;
        private View view1, view2;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.card_view);
            imgcontent = view.findViewById(R.id.imgcontent);
            txtMovieTitle = view.findViewById(R.id.txtMovieTitle);
            txtMovieRating = view.findViewById(R.id.txtMovieRating);
            txtMovieYear = view.findViewById(R.id.txtMovieYear);
            txtMovieDuration = view.findViewById(R.id.txtMovieDuration);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            txtMovieDuration.setVisibility(View.GONE);
            txtMovieRating.setVisibility(View.GONE);
            txtMovieYear.setVisibility(View.GONE);
        }
    }


    @NonNull
    @Override
    public MeteoAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeteoAdapter.myview holder, final int position) {

        final MeteoModel meteo = meteoList.get(position);


        //Picasso.with(context).load(movie.getImage()).into(holder.imgcontent);
        holder.txtMovieTitle.setText(meteoList.get(position).getTitle());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;

                if(!appInstalledOrNot("co.wuffy.player")){
                    Toast.makeText(context, "Installed Wuffy Player To Play This Video",
                            Toast.LENGTH_LONG).show();
                }else{
                    Bundle bnd = new Bundle();
                    bnd.putString("path",meteoList.get(position).getUrl() );
                    bnd.putString("name", meteoList.get(position).getTitle());
                    Intent intent = new Intent();
                    intent.setClassName("co.wuffy.player", "org.wuffy.videoplayer.WuffyPlayer");
                    intent.putExtras(bnd);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return meteoList.size();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    meteoList = meteofilterList;
                } else {
                    List<MeteoModel> filteredList = new ArrayList<>();
                    for (MeteoModel row : meteofilterList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    meteoList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = moviesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                meteoList = (ArrayList<MeteoModel>) filterResults.values;
                notifyDataSetChanged();


            }
        };
    }


}
