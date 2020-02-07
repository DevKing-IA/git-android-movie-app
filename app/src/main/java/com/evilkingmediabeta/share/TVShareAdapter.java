package com.evilkingmediabeta.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.evilkingmediabeta.R;

import java.util.ArrayList;

public class TVShareAdapter extends ArrayAdapter<ShareItemData> {

    private int resourceLayout;
    private Context mContext;
    private int selected_pos = -1;
    private int selected_page = 0;

    public TVShareAdapter(Context context, int resource, ArrayList<ShareItemData> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        ShareItemData p = getItem(position);

        if (p != null) {

            LinearLayout item_layout = (LinearLayout) v.findViewById(R.id.item_layout);

            if (selected_pos == position){
                item_layout.setBackgroundResource(R.drawable.border_share);
            }
            else{
                item_layout.setBackgroundResource(0);
            }

            ImageView tv_logo = v.findViewById(R.id.item_logo);
            Glide.with(mContext).asBitmap()
                .load(p.getLogo())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BitmapDrawable ob = new BitmapDrawable(mContext.getResources(), resource);
                        tv_logo.setImageDrawable(ob);
                    }
                });

            TextView tv_name = (TextView) v.findViewById(R.id.item_name);
            tv_name.setText(p.getName());

            TextView tv_num = (TextView) v.findViewById(R.id.item_num);
            tv_num.setText(String.valueOf(selected_page*20 + position+1));
        }

        return v;
    }

    public void selectPos(int pos){
        selected_pos = pos;
    }

    public void selectPage(int pos){
        selected_page = pos;
    }

}