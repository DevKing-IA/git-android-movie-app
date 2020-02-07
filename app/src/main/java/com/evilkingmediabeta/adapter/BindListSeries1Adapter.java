package com.evilkingmediabeta.adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.series.SeriesActivityServer1;


public class BindListSeries1Adapter extends RecyclerView.Adapter<BindListSeries1Adapter.myview> {
    private List<String> movielistFiltered;
    Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;
    private int itemposition;
    BindListAdapter adapter;
    private ArrayList<String> arrayListString;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtMovieTitle;
        private View view1, view2;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.card_view);
            txtMovieTitle = view.findViewById(R.id.txtMovieTitle);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);

        }
    }

    public BindListSeries1Adapter(ArrayList<String> arrayList, Context context) {

        this.context = context;
        this.arrayListString = arrayList;
    }

    @NonNull
    @Override
    public BindListSeries1Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_list, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BindListSeries1Adapter.myview holder, final int position) {


        holder.txtMovieTitle.setText(arrayListString.get(position));


        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemposition = position;
                String url = arrayListString.get(itemposition).toString();
                String sub_url;
                switch (itemposition)
                {
                    case 0: url="";
                        break;
                    case 1: url="serie-tv-americane";
                        break;
                    case 2: url="serie-tv-italiane";
                        break;
                    case 15: url="legal-drama";
                        break;
                    case 17: url="medical-drama";
                        break;
                    case 21: url="prison-drama";
                        break;
                    case 24: url="teen-drama";
                        break;

                }
                if (arrayListString.get(itemposition).equalsIgnoreCase("western")) {
                    sub_url = "http://www.seriehd.me/" + url ;

                } else {
                    sub_url  =Constant.SERIESURL1_SUB + url ;

                }

                Intent subCategory = new Intent(context, SeriesActivityServer1.class);
                subCategory.putExtra("url",sub_url);
                subCategory.putExtra("category", "true");
                context.startActivity(subCategory);


            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListString.size();
    }

}
