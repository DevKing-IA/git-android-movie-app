package com.evilkingmediabeta.musica;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.utility.CheckXml;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import io.fabric.sdk.android.services.network.HttpRequest;

public class MusicMainActivity extends AppCompatActivity {


    private LinearLayout rlListeIPTV, rlAscolta, rlShazam, rlCercaMusica;
    String m_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_music_main);
        } else {
            setContentView(R.layout.activity_music_main_portrait);
        }

        init();

        rlListeIPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(MusicMainActivity.this, Constant.MUSICA_LISTEIPTV_URL);
            }
        });

        rlAscolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MusicMainActivity.this, MusicWebViewActivity.class);
                //startActivity(intent);
                showMusicDialog();
            }
        });


        rlCercaMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(MusicMainActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(MusicMainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Musica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+ HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=musica&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(MusicMainActivity.this, "xmtv://"+tmp);
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



        rlShazam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MusicMainActivity.this)
                        .withPermissions(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS
                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Intent intent = new Intent(MusicMainActivity.this, WebViewActivity.class);
                        intent.putExtra("videoUrl", Constant.MUSICA_SHAZAM_URL);
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
                }).check();
            }
        });

        Constant.setFocusEvent(this, rlListeIPTV, rlAscolta, rlShazam, rlCercaMusica, rlCercaMusica);
        CheckXml.checkXml(this);

    }


    private void showMusicDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_musica_scarica);
        dialog.setCancelable(true);

        Button btn_1 = dialog.findViewById(R.id.btn_ace);
        Button btn_2 = dialog.findViewById(R.id.btn_xmtv);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MusicMainActivity.this, MusicWebViewActivity.class));
                dialog.dismiss();
            }
        });


        //btn_1
        btn_1.setFocusable(true);
        final boolean[] flag_btn_1 = {true};
        btn_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_1[0]){
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_1[0] = false;
                } else {
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    btn_1.invalidate();
                    flag_btn_1[0] = true;
                }
            }
        });


        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MusicMainActivity.this, MusicWebViewActivity.class));
                dialog.dismiss();
            }
        });


        //btn_1
        btn_2.setFocusable(true);
        final boolean[] flag_btn_2 = {true};
        btn_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_2[0]){
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_2[0] = false;
                } else {
                    //btn_2.setBackground(gd2);
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    btn_2.invalidate();
                    flag_btn_2[0] = true;
                }
            }
        });


        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MusicMainActivity.this, MusicWebViewActivity2.class));
                dialog.dismiss();
            }
        });

        btn_2.setFocusable(true);
        /*
        final boolean[] flag_btn_2 = {true};*/

        /*btn_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};
            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_2[0]){
                    btn_1.setBackground(gd);
                    flag_btn_2[0] = false;
                } else {
                    btn_1.setBackground(gd2);
                    btn_1.invalidate();
                    flag_btn_2[0] = true;
                }
            }
        });*/
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void init() {
        rlListeIPTV = findViewById(R.id.rlListeIPTV);
        rlAscolta = findViewById(R.id.rlAscolta);
        rlShazam = findViewById(R.id.rlShazam);
        rlCercaMusica = findViewById(R.id.rlCercaMusica);
    }
}
