package com.evilkingmediabeta.chat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.evilkingmediabeta.R;


public class MessageAdapter extends BaseAdapter {

    private List<Message> messages = new ArrayList<Message>();
    private Context context;
    private ClipboardManager clipboard;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("message", message.getText());
                    clipboard.setPrimaryClip(clip);
                    return true;
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message.getText().startsWith("http:") || message.getText().startsWith("https:")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getText()));
                        context.startActivity(intent);
                    }
                }
            });

        } else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.avatar = convertView.findViewById(R.id.avatar_textview);
            holder.profile_image_view = convertView.findViewById(R.id.imageview);
            holder.messageBody = convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.messageBody.setText(message.getText());
            if (message.getMemberData().getImage().trim().isEmpty()){
                holder.avatar.setVisibility(View.VISIBLE);
                holder.profile_image_view.setVisibility(View.GONE);
                String firstOfName = message.getMemberData().getName().substring(0, 1);
                holder.avatar.setText(firstOfName);
                GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
                drawable.setColor(message.getMemberData().getColor());
            } else {
                holder.avatar.setVisibility(View.GONE);
                holder.profile_image_view.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getMemberData().getImage()).into(holder.profile_image_view);
            }

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("message", message.getText());
                    clipboard.setPrimaryClip(clip);
                    return true;
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message.getText().startsWith("http:") || message.getText().startsWith("https:")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getText()));
                        context.startActivity(intent);
                    }
                }
            });
        }

        return convertView;
    }

}

class MessageViewHolder {
    public TextView avatar;
    public CircleImageView profile_image_view;
    public TextView messageBody;
}