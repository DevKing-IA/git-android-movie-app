package com.evilkingmediabeta.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import com.evilkingmediabeta.R;

import static com.evilkingmediabeta.chat.ContactActivity.current_useremail;
import static com.evilkingmediabeta.chat.ContactActivity.current_userid;
import static com.evilkingmediabeta.chat.ContactActivity.current_usertype;
import static com.evilkingmediabeta.chat.ContactActivity.isCurrentUserBlock;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.myview> implements Filterable {
    private List<User> userList;
    private List<User> userListFiltered;
    Context context;
    private DatabaseReference drUserInfo;
    private StorageReference srImage;

    public class myview extends RecyclerView.ViewHolder {

        private RelativeLayout item_view;
        private TextView clientuser_name_circleview, clientuser_fullname_view, lmessage_view, unreceived_count_view;
        private CircleImageView clientuser_profile_imageview;
        private ImageView more_button;

        public myview(View view) {
            super(view);
            item_view = view.findViewById(R.id.contact_item_view);
            clientuser_name_circleview = view.findViewById(R.id.clientuser_name_circleview);
            clientuser_profile_imageview = view.findViewById(R.id.clientuser_profile_imageview);
            clientuser_fullname_view = view.findViewById(R.id.clientuser_name);
            lmessage_view = view.findViewById(R.id.lmessage);
            unreceived_count_view = view.findViewById(R.id.unreceived_count_view);
            more_button = view.findViewById(R.id.more);
        }
    }

    public ContactAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.userListFiltered = userList;
        this.drUserInfo = FirebaseDatabase.getInstance().getReference();
        this.srImage = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ContactAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.myview holder, int position) {
        if (getItemCount() != 0){
            User user = userList.get(position);
            List<String> options = new ArrayList<>();
            options.add("Block");
            options.add("Delete");
            if (!user.getUserid().equals(current_userid)) {

                String username = user.getUsername();
                String userimage = user.getUserimage();
                if (userimage.trim().isEmpty()) {
                    holder.clientuser_name_circleview.setVisibility(View.VISIBLE);
                    holder.clientuser_profile_imageview.setVisibility(View.GONE);
                    String firstOfName = username.substring(0, 1);
                    holder.clientuser_name_circleview.setText(firstOfName.toUpperCase());
                    GradientDrawable drawable = (GradientDrawable) holder.clientuser_name_circleview.getBackground();
                    drawable.setColor(Color.WHITE);
                } else {
                    holder.clientuser_profile_imageview.setVisibility(View.VISIBLE);
                    holder.clientuser_name_circleview.setVisibility(View.GONE);
                    Picasso.get().load(userimage).into(holder.clientuser_profile_imageview);
                }
                holder.clientuser_fullname_view.setText(username);

                try {
                    drUserInfo.child(current_userid).child("senders").child(user.getUserid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot sender) {
                            if (sender.exists()) {
                                try {
                                    String count = Objects.requireNonNull(sender.getValue()).toString();
                                    if (count.equals("0")) {
                                        holder.unreceived_count_view.setVisibility(View.GONE);
                                    } else {
                                        holder.unreceived_count_view.setVisibility(View.VISIBLE);
                                        holder.unreceived_count_view.setText(count);
                                        GradientDrawable drawable = (GradientDrawable) holder.unreceived_count_view.getBackground();
                                        drawable.setColor(Color.RED);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    drUserInfo.child(current_userid).child("lmessage").child(user.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot lmessage) {
                            if (lmessage.exists()) {
                                try {
                                    String message = Objects.requireNonNull(lmessage.getValue()).toString();
                                    if (message.trim().isEmpty()) {
                                        holder.lmessage_view.setText("");
                                    } else {
                                        holder.lmessage_view.setText(message);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (current_usertype.equals("admin")) {
                        holder.more_button.setVisibility(View.VISIBLE);
                    } else {
                        holder.more_button.setVisibility(View.GONE);
                    }
                }


                holder.more_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.isBlock()) {
                            options.set(0, "Unblock");
                        } else {
                            options.set(0, "Block");
                        }
                        showDropDownList(v, options, user, position);
                    }
                });

                holder.item_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isCurrentUserBlock) {
                            Toast.makeText(context, "You were blocked by admin. Please wait until unblock from admin.", Toast.LENGTH_LONG).show();
                        } else if (user.isBlock()) {
                            Toast.makeText(context, "You can't chat with this user!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("my_email", current_useremail);
                            intent.putExtra("my_id", current_userid);
                            intent.putExtra("client_id", user.getUserid());
                            intent.putExtra("client_token", user.getUsertoken());
                            intent.putExtra("client_email", user.getUseremail());
                            intent.putExtra("client_name", username);
                            intent.putExtra("profile_image_path", userimage);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                holder.item_view.removeAllViews();
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.equals("")) {
                    userList = userListFiltered;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User user : userListFiltered) {
                        if (user.getUsername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(user);
                        }
                    }

                    userList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void showDropDownList(View view, List<String> dogsArray, User user, int position) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(context);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String item = getItem(position);
                TextView listItem = new TextView(context);

                listItem.setBackgroundColor(Color.WHITE);
                listItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                listItem.setText(item);
                listItem.setTextSize(15);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.BLACK);

                return listItem;
            }
        });

        // set the item click listener
        listViewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                view.startAnimation(fadeInAnimation);

                String selectedItemText = ((TextView) view).getText().toString();
                if (selectedItemText.equals("Block")){
                    blockUser(user, position);
                } else if (selectedItemText.equals("Unblock")){
                    unblockUser(user, position);
                } else {
                    deleteUser(user, position);
                }
                popupWindow.dismiss();
            }
        });

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(300);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);
        popupWindow.showAsDropDown(view, -5, 0);
    }

    private void blockUser(User user, int position) {

        new AlertDialog.Builder(context)
                .setMessage("Do you want to block this user really?")
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drUserInfo.child(user.getUserid()).child("block").setValue(true);
                        user.setBlock(true);
                        userList.set(position, user);
                        Toast.makeText(context, "Ok! You blocked this user.", Toast.LENGTH_LONG).show();
                        notifyItemChanged(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void unblockUser(User user, int position) {

        new AlertDialog.Builder(context)
                .setMessage("Do you want to unblock this user really?")
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drUserInfo.child(user.getUserid()).child("block").setValue(false);
                        user.setBlock(false);
                        userList.set(position, user);
                        Toast.makeText(context, "Ok! You unblocked this user.", Toast.LENGTH_LONG).show();
                        notifyItemChanged(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUser(User user, int position) {

        new AlertDialog.Builder(context)
                .setMessage("Do you want to delete this user really?")
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drUserInfo.child(user.getUserid()).removeValue();
                        if (!user.getUserimage().trim().isEmpty()){
                            srImage.child(user.getUserimage()).delete();
                        }
                        userList.remove(position);
                        Toast.makeText(context, "Ok! You deleted this user.", Toast.LENGTH_LONG).show();
                        notifyItemChanged(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}

