package com.evilkingmediabeta.mywebcaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.Map;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.demand.Utils;

import static android.content.Context.MODE_PRIVATE;
import static com.evilkingmediabeta.mywebcaster.MyWebCasterActivity.listView;


public class MyWebCasterAdapter extends ArrayAdapter<Utils>{

    private List<Utils> mylist;
    private Context context;

    public MyWebCasterAdapter(Context _context, List<Utils> _mylist) {
        super(_context, R.layout.link_item, _mylist);

        this.mylist = _mylist;
        this.context = _context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
        convertView = vi.inflate(R.layout.link_item, parent, false);

        Utils utils = getItem(position);

        TextView txtTitle = convertView.findViewById(R.id.linkTitle);
        txtTitle.setText(utils.getTitle());
        ImageView img = convertView.findViewById(R.id.linkImage);
        img.setImageResource(utils.getImg_resource());
        CardView cardView = convertView.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(context, mylist.get(position).getTitle());
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Are you really remove this link?")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = context.getSharedPreferences("VisitedLinks", MODE_PRIVATE);
                                Map<String, ?> allEntries = sharedPreferences.getAll();
                                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                                    if(entry.getValue().toString().equals(mylist.get(position).getTitle())){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove(entry.getKey());
                                        editor.apply();
                                    }
                                }
                                mylist.remove(position);
                                MyWebCasterAdapter adapter = new MyWebCasterAdapter(context, mylist);
                                listView.setAdapter(adapter);
                                listView.deferNotifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Not sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });
        return convertView;
    }


}
