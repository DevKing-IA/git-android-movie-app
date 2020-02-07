package com.evilkingmediabeta.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

import com.evilkingmediabeta.R;

public class ContactActivity extends AppCompatActivity {

    TextView currentuser_name_circleview, currentuser_fullname_view;
    CircleImageView currentuser_profile_imageview, update_profile_imageview;
    TextView empty_contact_view;
    TextView allTab, unreadTab;
    RecyclerView contact_listview;
    SpeedDialView speedDialView;
    EditText filter_name;

    String mCurrentUserId;

    private List<User> users = new ArrayList<>();

    DatabaseReference drUserInfo;
    StorageReference srImage;
    FirebaseAuth mAuth;
    ContactAdapter adapter;

    public static String current_userid, current_username, current_userimage, current_usertype, current_useremail;
    Uri update_profile_imageuri;
    public static boolean isCurrentUserBlock = false;
    String tab_flag = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_contact);

        drUserInfo = FirebaseDatabase.getInstance().getReference();
        srImage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        currentuser_name_circleview = findViewById(R.id.currentuser_name_circleview);
        currentuser_fullname_view = findViewById(R.id.currentuser_nameview);
        currentuser_profile_imageview = findViewById(R.id.currentuser_profile_imageview);
        speedDialView = findViewById(R.id.speedDialView);
        filter_name = findViewById(R.id.filter_name);
        allTab = findViewById(R.id.allTab);
        unreadTab = findViewById(R.id.unreadTab);
        empty_contact_view = findViewById(R.id.empty_contact_view);
        contact_listview = findViewById(R.id.contact_list);
        contact_listview.setLayoutManager(new LinearLayoutManager(this));
        contact_listview.setItemAnimator(new DefaultItemAnimator());

        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.ic_exit_to_app_black_24dp);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fb_logout, drawable)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), android.R.color.holo_blue_dark, getTheme()))
                .setLabel("Log out")
                .setLabelBackgroundColor(Color.TRANSPARENT)
                .create());

        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_profile_24dp);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fb_profile, drawable)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), android.R.color.holo_blue_dark, getTheme()))
                .setLabel("View profile")
                .setLabelBackgroundColor(Color.TRANSPARENT)
                .create());

        filter_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (users.size() > 1){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        allTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTab.setBackground(getResources().getDrawable(R.drawable.tab_indicator));
                unreadTab.setBackground(null);
                tab_flag = "all";
                showAllMessages();
            }
        });

        unreadTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unreadTab.setBackground(getResources().getDrawable(R.drawable.tab_indicator));
                allTab.setBackground(null);
                tab_flag = "unread";
                showUnreadMessages();
            }
        });

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fb_logout:
                        speedDialView.close();
                        new AlertDialog.Builder(ContactActivity.this)
                                .setMessage("Will you log out really?")
                                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit();
                                        editor.putString("email", "");
                                        editor.putString("password", "");
                                        editor.apply();
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        break;
                    case R.id.fb_profile:
                        speedDialView.close();
                        showProfileDialog();
                        break;
                }
                return true;
            }
        });

        mCurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        drUserInfo.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User current_user = dataSnapshot.getValue(User.class);
                assert current_user != null;
                displayCurrentUserInfo(current_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showAllMessages();
    }

    private void showAllMessages(){
        drUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    User client = data.getValue(User.class);
                    assert client != null;
                    if (!client.getUserid().equals(mCurrentUserId)){
                        users.add(client);
                    }
                }
                if (users.size() == 0){
                    empty_contact_view.setVisibility(View.VISIBLE);
                    contact_listview.setVisibility(View.GONE);
                } else {
                    empty_contact_view.setVisibility(View.GONE);
                    contact_listview.setVisibility(View.VISIBLE);
                    adapter = new ContactAdapter(users, ContactActivity.this);
                    contact_listview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUnreadMessages() {
        drUserInfo.child(mCurrentUserId).child("senders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String key = data.getKey();
                    String value = data.getValue().toString();
                    if (!value.equals("0")){
                        assert key != null;
                        drUserInfo.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                users.add(user);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (users.size() == 0){
                            empty_contact_view.setVisibility(View.VISIBLE);
                            contact_listview.setVisibility(View.GONE);
                        } else {
                            empty_contact_view.setVisibility(View.GONE);
                            contact_listview.setVisibility(View.VISIBLE);
                            adapter = new ContactAdapter(users, ContactActivity.this);
                            contact_listview.setAdapter(adapter);
                        }
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProfileDialog() {
        Dialog profile_dialog = new Dialog(this);
        profile_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profile_dialog.setContentView(R.layout.profile_popup);

        update_profile_imageview = profile_dialog.findViewById(R.id.user_profile_imageview);
        FloatingActionButton capture = profile_dialog.findViewById(R.id.capture_btn);
        EditText et_username = profile_dialog.findViewById(R.id.username);
        TextView update_btn = profile_dialog.findViewById(R.id.btn_update);

        if (!current_userimage.trim().isEmpty()){
            Picasso.get().load(current_userimage).into(update_profile_imageview);
        }

        et_username.setText(current_username);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ContactActivity.this);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().trim().isEmpty()){
                    Toast.makeText(ContactActivity.this, "Please enter a name what you want to update.", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(ContactActivity.this);
                    progressDialog.setMessage("Updating your profile..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    updateProfile(et_username.getText().toString(), progressDialog, profile_dialog);
                }
            }
        });

        profile_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profile_dialog.getWindow().setLayout(getResources().getDimensionPixelOffset(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
        profile_dialog.show();
    }

    private void updateProfile(String updated_name, ProgressDialog progressDialog, Dialog profile_dialog) {
        if(update_profile_imageuri != null){
            final StorageReference imagePath = srImage.child(current_userid);

            imagePath.putFile(update_profile_imageuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imagePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downUri = task.getResult();
                        String user_profile_image_path = downUri.toString();
                        Map<String, Object> update_datas = new HashMap<>();
                        update_datas.put("username", updated_name);
                        update_datas.put("userimage", user_profile_image_path);
                        drUserInfo.child(current_userid).updateChildren(update_datas).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ContactActivity.this, "Update failed! Please try again.", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDialog.dismiss();
                                    profile_dialog.dismiss();
                                    Toast.makeText(ContactActivity.this, "Update successful!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        else{
            Map<String, Object> update_datas = new HashMap<>();
            update_datas.put("username", updated_name);

            drUserInfo.child(current_userid).updateChildren(update_datas).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(ContactActivity.this, "Update failed! Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        profile_dialog.dismiss();
                        Toast.makeText(ContactActivity.this, "Update successful!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                update_profile_imageuri = result.getUri();
                File file = new File(update_profile_imageuri.toString());
                String image_path = file.getPath();
                Picasso.get().load(image_path).into(update_profile_imageview);
            }
        }
    }

    private void displayCurrentUserInfo(User current_user) {
        current_userid = current_user.getUserid();
        current_username = current_user.getUsername();
        current_userimage = current_user.getUserimage();
        current_usertype = current_user.getUsertype();
        current_useremail = current_user.getUseremail();
        if (current_userimage.trim().isEmpty()){
            currentuser_name_circleview.setVisibility(View.VISIBLE);
            currentuser_profile_imageview.setVisibility(View.GONE);
            String firstOfName = current_username.substring(0 ,1);
            currentuser_name_circleview.setText(firstOfName.toUpperCase());
        } else {
            currentuser_name_circleview.setVisibility(View.GONE);
            currentuser_profile_imageview.setVisibility(View.VISIBLE);
            Picasso.get().load(current_userimage).into(currentuser_profile_imageview);
        }
        currentuser_fullname_view.setText(current_username);
        isCurrentUserBlock = current_user.isBlock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tab_flag.equals("all")){
            showAllMessages();
        } else {
            showUnreadMessages();
        }
    }
}
