package com.evilkingmediabeta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.cartoon.CartoonCategoryActivity;
import com.evilkingmediabeta.chat.ContactActivity;
import com.evilkingmediabeta.chat.User;
import com.evilkingmediabeta.demand.FilmCategoryActivity;
import com.evilkingmediabeta.libri.LibriActivity;
import com.evilkingmediabeta.livetv.LiveTvCategoryActivity;
import com.evilkingmediabeta.musica.MusicMainActivity;
import com.evilkingmediabeta.mywebcaster.MyWebCasterActivity;
import com.evilkingmediabeta.radio.uamp.MainActivity;
import com.evilkingmediabeta.sports.SportsCategoryActivity;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.Option;
import com.evilkingmediabeta.utilitymenu.UtilityMenuActivity;
import com.evilkingmediabeta.webview.NewsWebView;
import com.evilkingmediabeta.webview.UpdateView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.network.HttpRequest;

public class CategoryActivity extends AppCompatActivity {

    TextView txtBottom, txt_dt_time, txtlogout;
    TextView dropdownview;
    LinearLayout rlMovies, rlCartoon, rlSports, rlMusic, rlMeteo, rlepg, rllive, rllibri, rlmywebcaster, rlCercaGlobale;
    private ImageView download,wvc, dns, email, vpn, txtsetting, chat, bot, cache, news;
    private CircleImageView profile_imageview;
    private Uri profile_image_uri = null;
    PopupWindow dropdown_listview;
    DatabaseReference drUserInfo;
    FirebaseAuth mAuth;
    Random rndm;
    StorageReference srImage;
    final String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    private String m_Text = "";
    TextView txt_version;

    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isCarUiMode(this)){
            Intent i3 = new Intent(CategoryActivity.this, MainActivity.class);
            startActivity(i3);
        }


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flag = true;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);

            email = findViewById(R.id.txtemail);
            txtsetting = findViewById(R.id.txtsetting);
            download = findViewById(R.id.download);
            news = findViewById(R.id.news);
            wvc = findViewById(R.id.wvc);
            dns = findViewById(R.id.dns);
            vpn = findViewById(R.id.vpn);
            cache = findViewById(R.id.cache);
            chat = findViewById(R.id.chat);
            bot = findViewById(R.id.bot);

            wvc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WVC_playstore)));
                    showWBC();

                }
            });

            dns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.playDNSChanger(CategoryActivity.this);
                }
            });

            vpn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.playVPNChanger(CategoryActivity.this);
                }
            });

            cache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogConfirm();
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryActivity.this, UpdateView.class);
                    //intent.putExtra("videoUrl", "https://www.evilkingmedia.com/download-ekm/");
                    startActivity(intent);
                }
            });

            news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryActivity.this, NewsWebView.class);
                    //intent.putExtra("videoUrl", Constant.NEWS_URL);
                    startActivity(intent);
                }
            });

            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","evilkodi@libero.it", null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });

            txtsetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.wuffy.player")));
                    showWuffyDialog();
                }
            });

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToChat();
                }
            });

            bot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TELEGRAM_BOT_URL)));
                }
            });

            setFocusEvent();

        } else {
            setContentView(R.layout.activity_main_portrait);
            dropdownview = findViewById(R.id.dropdownview);
            List<Option> options = new ArrayList<>();
            options.add(new Option("Chat", R.drawable.chat));
            options.add(new Option("Bot Assistenza", R.drawable.telegram));
            options.add(new Option("News", R.drawable.news));
            options.add(new Option("Update", R.drawable.download));
            options.add(new Option("E-mail", R.drawable.email));
            options.add(new Option("Wuffy", R.drawable.ic_play));
            options.add(new Option("Web Caster", R.drawable.wvc));
            options.add(new Option("DNS", R.drawable.dns));
            options.add(new Option("VPN", R.drawable.vpn));
            options.add(new Option("Clean Cache", R.drawable.clean));
            dropdownview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDropDownList(v, options);
                }
            });

        }

        drUserInfo = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        srImage = FirebaseStorage.getInstance().getReference();

        txtBottom = findViewById(R.id.txt);

        String styledText = "Sito Ufficiale: evilkingmediabeta.com - Assistenza Web: androidaba.com - Canale Telegram:<font color='blue'> https://t.me/evilkingmedia</font> ";
        txtBottom.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        rlMovies = findViewById(R.id.rlMovies);
        rlCartoon = findViewById(R.id.rlCartoon);
        rlSports = findViewById(R.id.rlSports);
        rlepg = findViewById(R.id.rlepg);
        rlMusic = findViewById(R.id.rlMusic);
        rlMeteo = findViewById(R.id.rlMeteo);
        rllive = findViewById(R.id.rllive);
        rllibri = findViewById(R.id.rllibri);
        rlmywebcaster = findViewById(R.id.rlMyWebCaster);
        txt_dt_time = findViewById(R.id.dt_time);
        txtlogout = findViewById(R.id.txtlogout);
        rlCercaGlobale = findViewById(R.id.rlCercaGlobale);

        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                finish();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                final Date date = new Date();
                final DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                CategoryActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        txt_dt_time.setText(df.format(date));
                    }
                });

            }
        }, 0, 1000);

        rlMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, FilmCategoryActivity.class);
                startActivity(i);
            }
        });

        rlCartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, CartoonCategoryActivity.class);
                startActivity(i);
            }
        });

         rlSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, SportsCategoryActivity.class);
                startActivity(i);
            }
        });

        rlepg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, UtilityMenuActivity.class);
                startActivity(i);
            }
        });

        rlMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoryActivity.this, MusicMainActivity.class);
                startActivity(intent);
            }
        });

       rlMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Constant.playInWuffyOrXmtv(CategoryActivity.this,Constant.METEOURL );
                Constant.openExternalBrowser(CategoryActivity.this, Constant.METEOURL);


            }
        });

        rllive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, LiveTvCategoryActivity.class));

            }
        });


        rllibri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CategoryActivity.this, LibriActivity.class));
            }
        });

        rlmywebcaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, MyWebCasterActivity.class));
            }
        });


        rlCercaGlobale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(CategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=ovunque&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(CategoryActivity.this, "xmtv://"+tmp);
                        Log.d("[Cerca Globale]", "Cerca Avviato!");
                        Log.d("[Cerca Globale]", "Stringa Cercata: " + m_Text);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // SEEEEEEEEEEEEEE
                builder.show();
            }
        });

        rlCercaGlobale.startAnimation(getBlinkAnimation());


        Constant.setFocusEvent(this, rlMovies, rlCartoon, rlSports, rlMusic, rlMeteo, rlepg, rllive, rllibri, rlCercaGlobale, rlmywebcaster);
        txtlogout.setFocusable(true);
        final boolean[] flag_txtlogout = {true};
        txtlogout.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtlogout[0]){
                    txtlogout.setBackgroundColor(Color.RED);
                    flag_txtlogout[0] = false;
                } else {
                    txtlogout.setBackgroundColor(0x0000FF00 );
                    txtlogout.invalidate();
                    txtlogout.setTextColor(Color.parseColor("#FFFFFF"));
                    flag_txtlogout[0] = true;
                }
            }
        });


        txt_version = (TextView) findViewById(R.id.version);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txt_version.setText("v"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }




    public Animation getBlinkAnimation(){
        Animation animation = new AlphaAnimation(0, 1);         // Change alpha from fully visible to invisible
        animation.setDuration(600);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(2);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }

    private void showDropDownList(View view, List<Option> options) {

        // initialize a pop up window type
        dropdown_listview = new PopupWindow(this);

        // the drop down list is a list view
        ListView listView = new ListView(this);

        // set our adapter and pass our pop up window contents
        listView.setAdapter(new DropDownListAdapter(CategoryActivity.this, 0, options));
        listView.setSelector(R.drawable.dropdown_item_background);

        // some other visual settings
        dropdown_listview.setFocusable(true);
        dropdown_listview.setWidth(650);
        dropdown_listview.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        dropdown_listview.setContentView(listView);
        dropdown_listview.showAsDropDown(view, -5, 0);
    }

    private void goToChat(){
        SharedPreferences sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().size() == 0){
            showLoginDialog();
        } else {
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");
            assert email != null;
            assert password != null;
            if (email.isEmpty() && password.isEmpty()){
                showLoginDialog();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final boolean[] flag = {false};
                        drUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    User user = ds.getValue(User.class);
                                    assert user != null;
                                    if (user.getUserid().equals(mAuth.getUid())){
                                        flag[0] = true;
                                    }
                                }
                                if (flag[0]){
                                    progressDialog.dismiss();
                                    SharedPreferences.Editor editor = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit();
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.apply();
                                    Intent intent = new Intent(CategoryActivity.this, ContactActivity.class);
                                    startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "You was deleted by admin. You will can't use this email and password more!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        }
    }

    private void showLoginDialog() {
        Dialog login_dialog = new Dialog(this);
        login_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        login_dialog.setContentView(R.layout.activity_login);

        EditText et_email, et_password;
        TextView login_button, create_account_button;
        et_email = login_dialog.findViewById(R.id.login_email);
        et_password = login_dialog.findViewById(R.id.login_password);
        login_button = login_dialog.findViewById(R.id.btn_login);
        create_account_button = login_dialog.findViewById(R.id.create_account);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "You must enter email!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!email.matches(Expn)){
                    Toast.makeText(CategoryActivity.this, "Email Address is incorrect!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "You must enter valid password!", Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(CategoryActivity.this, "Don't exist this user. You must register!", Toast.LENGTH_LONG).show();
                        }else{
                            gotoChatPage(progressDialog, login_dialog, email, password);
                        }
                    }
                });
            }
        });

        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        login_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        login_dialog.getWindow().setLayout(getResources().getDimensionPixelOffset(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
        login_dialog.show();
    }

    private void gotoChatPage(ProgressDialog dialog, Dialog login_dialog, String email, String password) {
        final boolean[] flag = {false};
        drUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    assert user != null;
                    if (user.getUserid().equals(mAuth.getUid())){
                        flag[0] = true;
                    }
                }
                if (flag[0]){
                    dialog.dismiss();
                    login_dialog.dismiss();
                    SharedPreferences.Editor editor = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                    Intent intent = new Intent(CategoryActivity.this, ContactActivity.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "This user was deleted by admin. You will can't use this email and password more!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRegisterDialog(){
        profile_image_uri = null;
        Dialog register_dialog = new Dialog(this);
        register_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        register_dialog.setContentView(R.layout.activity_register);

        EditText et_username, et_email, et_password, et_confirmpassword;
        TextView register_button, go_loginpage_button;
        FloatingActionButton photo_capture_button;

        et_username = register_dialog.findViewById(R.id.register_username);
        et_email = register_dialog.findViewById(R.id.register_email);
        et_password = register_dialog.findViewById(R.id.register_password);
        et_confirmpassword = register_dialog.findViewById(R.id.register_confirmpassword);
        register_button = register_dialog.findViewById(R.id.btn_register);
        go_loginpage_button = register_dialog.findViewById(R.id.goLogin);
        photo_capture_button = register_dialog.findViewById(R.id.capture_btn);
        profile_imageview = register_dialog.findViewById(R.id.user_profile_imageview);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String confirm_password = et_confirmpassword.getText().toString().trim();
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "Fill all fields!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!email.matches(Expn)){
                    Toast.makeText(CategoryActivity.this, "Please enter a valid email address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirm_password)){
                    Toast.makeText(CategoryActivity.this, "Password is not match!", Toast.LENGTH_LONG).show();
                    return;
                }

                doRegister(username, email, password, register_dialog);
            }
        });

        go_loginpage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_dialog.dismiss();
            }
        });

        photo_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(CategoryActivity.this);
            }
        });

        register_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        register_dialog.getWindow().setLayout(getResources().getDimensionPixelOffset(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
        register_dialog.show();
    }

    private void doRegister(String username, String email, String password, Dialog register_dialog){
        final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "This email and password are deleted by admin!", Toast.LENGTH_LONG).show();
                } else {
                    final FirebaseUser newUser = task.getResult().getUser();
                    newUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()){
                                String token = task.getResult().getToken();
                                final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addUsertoDB(newUser.getUid(), token,  username, newUser.getEmail(), progressDialog, register_dialog);
                                        } else {
                                            Toast.makeText(CategoryActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void addUsertoDB(String uid, String usertoken, String username, String email, ProgressDialog progressDialog, Dialog register_dialog) {

        if(profile_image_uri != null){
            final StorageReference imagePath = srImage.child(uid);

            imagePath.putFile(profile_image_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        User user = new User();
                        user.setUserid(uid);
                        user.setUsername(username);
                        user.setUseremail(email);
                        user.setUserimage(user_profile_image_path);
                        user.setUsertype("customer");
                        user.setUsertoken(usertoken);
                        user.setBlock(false);
                        drUserInfo.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "Register failed! Please try again.", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDialog.dismiss();
                                    register_dialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "Register successful! Please log in.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        else{
            User user = new User();
            user.setUserid(uid);
            user.setUsername(username);
            user.setUseremail(email);
            user.setUserimage("");
            user.setUsertype("customer");
            user.setUsertoken(usertoken);
            user.setBlock(false);
            drUserInfo.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryActivity.this, "Register failed! Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        register_dialog.dismiss();
                        Toast.makeText(CategoryActivity.this, "Register successful! Please log in.", Toast.LENGTH_LONG).show();
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
                profile_image_uri = result.getUri();
                File file = new File(profile_image_uri.toString());
                String image_path = file.getPath();
                Picasso.get().load(image_path).into(profile_imageview);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                CheckXml.checkXml(CategoryActivity.this);
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();
        rlMovies.requestFocus();
    }

    private void setFocusEvent(){

        // telegram bot
        bot.setFocusable(true);
        final boolean[] flag_bot = {true};
        bot.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_bot[0]){
                    bot.setBackgroundColor(Color.RED);
                    flag_bot[0] = false;
                } else {
                    bot.setBackgroundColor(0x0000FF00 );
                    bot.invalidate();
                    flag_bot[0] = true;
                }
            }
        });

        // download
        download.setFocusable(true);
        final boolean[] flag_download = {true};
        download.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_download[0]){
                    download.setBackgroundColor(Color.RED);
                    flag_download[0] = false;
                } else {
                    download.setBackgroundColor(0x0000FF00 );
                    download.invalidate();
                    flag_download[0] = true;
                }
            }
        });

        //email
        email.setFocusable(true);
        final boolean[] flag_txtemail = {true};
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtemail[0]){
                    email.setBackgroundColor(Color.RED);
                    flag_txtemail[0] = false;
                } else {
                    email.setBackgroundColor(0x0000FF00 );
                    email.invalidate();
                    flag_txtemail[0] = true;
                }
            }
        });

        //txtsetting
        txtsetting.setFocusable(true);
        final boolean[] flag_txtsetting = {true};
        txtsetting.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtsetting[0]){
                    txtsetting.setBackgroundColor(Color.RED);
                    flag_txtsetting[0] = false;
                } else {
                    txtsetting.setBackgroundColor(0x0000FF00 );
                    txtsetting.invalidate();
                    flag_txtsetting[0] = true;
                }
            }
        });

        //wvc
        wvc.setFocusable(true);
        final boolean[] flag_wvc = {true};
        wvc.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_wvc[0]){
                    wvc.setBackgroundColor(Color.RED);
                    flag_wvc[0] = false;
                } else {
                    wvc.setBackgroundColor(0x0000FF00 );
                    wvc.invalidate();
                    flag_wvc[0] = true;
                }
            }
        });

        //dns
        dns.setFocusable(true);
        final boolean[] flag_dns = {true};
        dns.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_dns[0]){
                    dns.setBackgroundColor(Color.RED);
                    flag_dns[0] = false;
                } else {
                    dns.setBackgroundColor(0x0000FF00 );
                    dns.invalidate();
                    flag_dns[0] = true;
                }
            }
        });

        //vpn
        vpn.setFocusable(true);
        final boolean[] flag_vpn = {true};
        vpn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_vpn[0]){
                    vpn.setBackgroundColor(Color.RED);
                    flag_vpn[0] = false;
                } else {
                    vpn.setBackgroundColor(0x0000FF00 );
                    vpn.invalidate();
                    flag_vpn[0] = true;
                }
            }
        });
        //cache
        cache.setFocusable(true);
        final boolean[] flag_cache= {true};
        cache.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_cache[0]){
                    cache.setBackgroundColor(Color.RED);
                    flag_cache[0] = false;
                } else {
                    cache.setBackgroundColor(0x0000FF00 );
                    cache.invalidate();
                    flag_cache[0] = true;
                }
            }
        });

        //news
        news.setFocusable(true);
        final boolean[] flag_news = {true};
        news.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_news[0]){
                    news.setBackgroundColor(Color.RED);
                    flag_news[0] = false;
                } else {
                    news.setBackgroundColor(0x0000FF00 );
                    news.invalidate();
                    flag_news[0] = true;
                }
            }
        });

        //chat
        chat.setFocusable(true);
        final boolean[] flag_chat = {true};
        chat.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_chat[0]){
                    chat.setBackgroundColor(Color.RED);
                    flag_chat[0] = false;
                } else {
                    chat.setBackgroundColor(0x0000FF00 );
                    chat.invalidate();
                    flag_chat[0] = true;
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit this app?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class DropDownListAdapter extends ArrayAdapter<Option> {

        private DropDownListAdapter(@NonNull Context context, int resource, @NonNull List<Option> options) {
            super(context, resource, options);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(R.layout.dropdownlist_item, parent, false);
            }
            Option option = getItem(position);
            LinearLayout item_view = v.findViewById(R.id.item_view);
            TextView title_view = v.findViewById(R.id.title);
            ImageView icon_view = v.findViewById(R.id.icon);
            assert option != null;
            title_view.setText(option.getTitle());
            icon_view.setBackgroundResource(option.getIcon());

            item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                    fadeInAnimation.setDuration(10);
                    v.startAnimation(fadeInAnimation);

                    switch (option.getTitle()){
                        case "Chat":
                            goToChat();
                            break;
                        case "Update":
                            Intent updateIntent = new Intent(CategoryActivity.this, UpdateView.class);
                            //updateIntent.putExtra("videoUrl", "https://www.evilkingmedia.com/download-ekm/");
                            startActivity(updateIntent);
                            break;
                        case "News":
                            Intent newsIntent = new Intent(CategoryActivity.this, NewsWebView.class);
                            //newsIntent.putExtra("videoUrl", Constant.NEWS_URL);
                            startActivity(newsIntent);
                            break;
                        case "E-mail":
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","evilkodi@libero.it", null));
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            break;
                        case "Wuffy":
                            showWuffyDialog();
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.wuffy.player")));
                            break;
                        case "Web Caster":
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WVC_playstore)));
                            showWBC();
                            break;
                        case "DNS":
                            Constant.playDNSChanger(CategoryActivity.this);
                            break;
                        case "VPN":
                            Constant.playVPNChanger(CategoryActivity.this);
                            break;
                        case "Clean Cache":
                            showDialogConfirm();
                            break;
                        case "Bot Assistenza":
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TELEGRAM_BOT_URL)));
                            break;
                    }
                    dropdown_listview.dismiss();
                }
            });

            return v;
        }
    }




    private void showWBC() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wbc);
        dialog.setCancelable(true);

        Button btn_ace = dialog.findViewById(R.id.btn_ace);
        Button btn_xmtv = dialog.findViewById(R.id.btn_xmtv);

        btn_ace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.evilkingmedia.com/download-ekm/")));
                dialog.dismiss();
            }
        });
        //btn_ace
        btn_ace.setFocusable(true);
        final boolean[] flag_btn_ace = {true};
        btn_ace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_ace[0]){
                    btn_ace.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_ace[0] = false;
                } else {
                    btn_ace.setBackgroundResource(R.drawable.section_selection_background);
                    btn_ace.invalidate();
                    flag_btn_ace[0] = true;
                }
            }
        });
        btn_xmtv.setOnClickListener(new View.OnClickListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.instantbits.cast.webvideo")));
                dialog.dismiss();
            }
        });
        //btn_xmtv
        btn_xmtv.setFocusable(true);
        final boolean[] flag_btn_xmtv = {true};
        btn_xmtv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_xmtv[0]){
                    btn_xmtv.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_xmtv[0] = false;
                } else {
                    btn_xmtv.setBackgroundResource(R.drawable.section_selection_background);
                    btn_xmtv.invalidate();
                    flag_btn_xmtv[0] = true;
                }
            }
        });
        dialog.show();
    }




    private void showWuffyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wuffy);
        dialog.setCancelable(true);

        Button btn_ace = dialog.findViewById(R.id.btn_ace);
        Button btn_xmtv = dialog.findViewById(R.id.btn_xmtv);

        btn_ace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wuffyplayer.com/download")));
                dialog.dismiss();
            }
        });
        //btn_ace
        btn_ace.setFocusable(true);
        final boolean[] flag_btn_ace = {true};
        btn_ace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_ace[0]){
                    btn_ace.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_ace[0] = false;
                } else {
                    btn_ace.setBackgroundResource(R.drawable.section_selection_background);
                    btn_ace.invalidate();
                    flag_btn_ace[0] = true;
                }
            }
        });
        btn_xmtv.setOnClickListener(new View.OnClickListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.wuffy.player")));
                dialog.dismiss();
            }
        });
        //btn_xmtv
        btn_xmtv.setFocusable(true);
        final boolean[] flag_btn_xmtv = {true};
        btn_xmtv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_xmtv[0]){
                    btn_xmtv.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_xmtv[0] = false;
                } else {
                    btn_xmtv.setBackgroundResource(R.drawable.section_selection_background);
                    btn_xmtv.invalidate();
                    flag_btn_xmtv[0] = true;
                }
            }
        });
        dialog.show();
    }




    private void showDialogConfirm() {
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_cache_confirm);
//        dialog.setCancelable(true);
//
//        Button btn_ekm = (Button) dialog.findViewById(R.id.btn_ekm);
//        Button btn_wuffy = (Button) dialog.findViewById(R.id.btn_wuffy);
//
//        btn_ekm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

        new AlertDialog.Builder(this)
                .setTitle("Pulire la cache comporterà la chisura dell'applicazione. Continuare?")
                .setPositiveButton("Pulisci Cache", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cleaned Cache \n Restarting EKM", Toast.LENGTH_SHORT).show();

                        new Timer().schedule(new TimerTask(){
                            public void run() {
                                CategoryActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        startActivity(new Intent(CategoryActivity.this, SplashScreenActivity.class));
                                    }
                                });
                            }
                        }, 2000);
                        File dir = getApplicationContext().getCacheDir();
                        if(deleteCacheDir(dir))
                            finish();
                        clearAppData();
                        clearApplicationData();
                        startActivity(new Intent(CategoryActivity.this, SplashScreenActivity.class));
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
//                dialog.dismiss();
//            }
//        });
//        //btn_ekm
//        btn_ekm.setFocusable(true);
//        final boolean[] flag_btn_ekm = {true};
//        btn_ekm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (flag_btn_ekm[0]){
//                    btn_ekm.setBackgroundColor(Color.RED);
//                    flag_btn_ekm[0] = false;
//                } else {
//                    btn_ekm.setBackgroundColor(0x0000FF00 );
//                    btn_ekm.invalidate();
//                    flag_btn_ekm[0] = true;
//                }
//            }
//        });
//        //btn_wuffy
//        btn_wuffy.setFocusable(true);
//        final boolean[] flag_btn_wuffy = {true};
//        btn_wuffy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (flag_btn_wuffy[0]){
//                    btn_wuffy.setBackgroundColor(Color.RED);
//                    flag_btn_wuffy[0] = false;
//                } else {
//                    btn_wuffy.setBackgroundColor(0x0000FF00 );
//                    btn_wuffy.invalidate();
//                    flag_btn_wuffy[0] = true;
//                }
//            }
//        });
//        btn_wuffy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PackageManager  pm = getPackageManager();
//                // Get all methods on the PackageManager
//                Method[] methods = pm.getClass().getDeclaredMethods();
//                for (Method m : methods) {
//                    if (m.getName().equals("freeStorage")) {
//                        try {
//                            long desiredFreeStorage = 8 * 1024 * 1024 * 1024; // Request for 8GB of free space
//                            m.invoke(pm, desiredFreeStorage , null);
//                        } catch (Exception e) {
//                        }
//                        break;
//                    }
//                }
//                try {
//                    File dir = getApplicationContext().getCacheDir();
//                    if(deleteCacheDir(dir)) Toast.makeText(getApplicationContext(), "Cleaned Wuffy Cache", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {}
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }





    private void clearAppData() {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear " + getApplicationContext().getPackageName() + " HERE");
            startActivity(new Intent(CategoryActivity.this, SplashScreenActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String decrypt(@NotNull String input, @NotNull String key){
        byte[] bytes = Base64.decodeBase64(input);
        if(bytes.length < 17) {
            return null;
        }

        byte[] ivBytes = Arrays.copyOfRange(bytes, 0, 16);
        byte[] contentBytes = Arrays.copyOfRange(bytes, 16, bytes.length);


        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes,0, ciper.getBlockSize());

            ciper.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(ciper.doFinal(contentBytes));
        } catch (
                NoSuchAlgorithmException |
                        NoSuchPaddingException |
                        UnsupportedEncodingException |
                        InvalidAlgorithmParameterException |
                        InvalidKeyException |
                        IllegalBlockSizeException |
                        BadPaddingException ignored
        ) {
        }

        return null;
    }


    public static boolean deleteCacheDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteCacheDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    public static boolean isCarUiMode(Context c) {
        UiModeManager uiModeManager = (UiModeManager) c.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR) {
            return true;
        } else {
            return false;
        }
    }
}
