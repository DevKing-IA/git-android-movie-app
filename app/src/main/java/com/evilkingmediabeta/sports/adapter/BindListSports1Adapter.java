package com.evilkingmediabeta.sports.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import com.evilkingmediabeta.R;
import com.evilkingmediabeta.model.SportsModel;

import static com.evilkingmediabeta.sports.SportsHdStreamsActivity.etSearch;
import static com.evilkingmediabeta.sports.SportsHdStreamsActivity.sportScheduleDetailRecyclerView;

public class BindListSports1Adapter extends RecyclerView.Adapter<BindListSports1Adapter.myview> {
    private List<String> scheduleList;
    private List<String> detailList;
    private List<SportsModel> detailModelList = new ArrayList<>();
    Context context;
    String videoPath;
    private int itemposition;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtDate, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtDate = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public BindListSports1Adapter(List<String> scheduleList, Context context, List<String> detailList) {
        this.scheduleList = scheduleList;
        this.context = context;
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public BindListSports1Adapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new BindListSports1Adapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindListSports1Adapter.myview holder, final int position) {

        String schedule = scheduleList.get(position);
        String[] scheduleArray = schedule.split(",");
        holder.txtDate.setText(scheduleArray[0]);
        holder.txtTime.setText(scheduleArray[1]);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetail(detailList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    private void viewDetail(String detail){
        detailModelList.clear();
        SportsModel model;
        Document detailDoc = Jsoup.parse(detail);
        if(detailDoc.select("p").get(0).childNodeSize() > 5){
            String[] detailArray = detail.split("<br>");
            for (int i = 0; i < detailArray.length; i++){
                model = new SportsModel();
                Document doc = Jsoup.parse(detailArray[i]);
                model.setTeam1(doc.select("span").get(0).nextSibling().toString());
                model.setUrl(doc.select("a").attr("href"));
                detailModelList.add(model);
            }
        } else {
            model = new SportsModel();
            model.setTeam1(detailDoc.select("span").get(0).nextSibling().toString());
            model.setUrl(detailDoc.select("a").attr("href"));
            detailModelList.add(model);
        }
        BindDetailListAdapter adapter = new BindDetailListAdapter(context, detailModelList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        sportScheduleDetailRecyclerView.setLayoutManager(mLayoutManager);
        sportScheduleDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sportScheduleDetailRecyclerView.invalidate();
        sportScheduleDetailRecyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

