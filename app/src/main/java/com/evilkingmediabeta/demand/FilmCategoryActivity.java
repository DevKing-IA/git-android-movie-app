package com.evilkingmediabeta.demand;

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
import com.evilkingmediabeta.series.SeriesActivityServer4;
import com.evilkingmediabeta.share.ShareActivity;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.videoteca.VideoTecaEmbedActivity;

import io.fabric.sdk.android.services.network.HttpRequest;

public class FilmCategoryActivity extends AppCompatActivity {

    LinearLayout rlListeIPTV, rlRaiMediaset, rlShare, rlCinetecaWebCaster, rlDocumentariWebCaster, rlVideoYT, rlCercaFilm, rlCercaSerieTV, cinemaembed;
    String m_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_film_category);
        } else {
            setContentView(R.layout.activity_film_category_portrait);
        }

        rlListeIPTV = findViewById(R.id.rlListeIPTV);
        rlRaiMediaset = findViewById(R.id.rlRaiMediaset);
        rlShare = findViewById(R.id.rlShare);
        rlCinetecaWebCaster = findViewById(R.id.rlCinetecaWebCaster);
        rlDocumentariWebCaster = findViewById(R.id.rlDocumentariWebCaster);
        rlVideoYT = findViewById(R.id.rlVideoYT);
        rlCercaFilm = findViewById(R.id.rlCercaFilm);
        rlCercaSerieTV = findViewById(R.id.rlCercaSerieTV);
        cinemaembed = findViewById(R.id.cinemaembed);

        rlShare.setOnClickListener(view -> {
            Intent intent = new Intent(FilmCategoryActivity.this, ShareActivity.class);
//          Intent intent = new Intent(FilmCategoryActivity.this, ShareActivity.class);
            intent.putExtra("type", "vod");
            startActivity(intent);
        });

        rlListeIPTV.setOnClickListener(view -> Constant.openExternalBrowser(FilmCategoryActivity.this,Constant.EVILKINGLISTEITTVURL ));

        rlCinetecaWebCaster.setOnClickListener(view -> startActivity(new Intent(FilmCategoryActivity.this, CinetecaWebCasterActivity.class)));
        cinemaembed.setOnClickListener(view -> showCinemaEmbed());

        rlCercaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(FilmCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Film", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=film&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(FilmCategoryActivity.this, "xmtv://"+tmp);
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


        rlCercaSerieTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(FilmCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Serie TV", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=serietv&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(FilmCategoryActivity.this, "xmtv://"+tmp);
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

        //rlCercaSerieTV.setOnClickListener(view -> startActivity(new Intent(FilmCategoryActivity.this, SerieTVCercaActivity.class)));

        rlCercaSerieTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(FilmCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Serie TV", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=serietv&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(FilmCategoryActivity.this, "xmtv://"+tmp);
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

















        /*rlSearchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(FilmCategoryActivity.this, Constant.Search_Video_Url);
            }
        });*/

        rlDocumentariWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(FilmCategoryActivity.this, Constant.EVILKINGDOCUMENTARIURL);
            }
        });

        rlVideoYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(FilmCategoryActivity.this, SeriesActivityServer4.class));
                startActivity(new Intent(FilmCategoryActivity.this, VideoTecaEmbedActivity.class));
            }
        });

        rlRaiMediaset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRaiMediaset();
            }
        });


        Constant.setFocusEvent(this, cinemaembed, rlListeIPTV, rlShare, rlCinetecaWebCaster, rlDocumentariWebCaster, rlVideoYT, rlCercaFilm, rlCercaSerieTV);
        CheckXml.checkXml(this);
    }

    private void showCinemaEmbed() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cinemaembed);
        dialog.setCancelable(true);

        Button btn_1 = dialog.findViewById(R.id.btn_film1);
        Button btn_2 = dialog.findViewById(R.id.btn_film2);
        Button btn_3 = dialog.findViewById(R.id.btn_serie1);
        Button btn_4 = dialog.findViewById(R.id.btn_serie2);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilmCategoryActivity.this, SeriesActivityServer4.class));
                dialog.dismiss();
            }
        });

        //btn_1
        btn_1.setFocusable(true);
        final boolean[] flag_btn_1 = {true};
        btn_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (flag_btn_1[0]) {
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_1[0] = false;
                } else {
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    btn_1.invalidate();
                    flag_btn_1[0] = true;
                }
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilmCategoryActivity.this, Film2Activity.class));
            dialog.dismiss();
            }
        });
        //btn_2
        btn_2.setFocusable(true);
        final boolean[] flag_btn_2 = {true};
        btn_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_2[0]) {
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_2[0] = false;
                } else {
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    btn_2.invalidate();
                    flag_btn_2[0] = true;
                }
            }
        });


        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilmCategoryActivity.this, SeriesActivityServer4.class));
                dialog.dismiss();
            }
        });
        //btn_2
        btn_3.setFocusable(true);
        final boolean[] flag_btn_3 = {true};
        btn_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_3[0]) {
                    btn_3.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_3[0] = false;
                } else {
                    btn_3.setBackgroundResource(R.drawable.section_selection_background);
                    btn_3.invalidate();
                    flag_btn_3[0] = true;
                }
            }
        });


        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilmCategoryActivity.this, Serie2Activity.class));
                dialog.dismiss();
            }
        });
        //btn_2
        btn_4.setFocusable(true);
        final boolean[] flag_btn_4 = {true};
        btn_4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_4[0]) {
                    btn_4.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_4[0] = false;
                } else {
                    btn_4.setBackgroundResource(R.drawable.section_selection_background);
                    btn_4.invalidate();
                    flag_btn_4[0] = true;
                }
            }
        });
        dialog.show();
    }
    private void showRaiMediaset() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_raimediasetondemand);
        dialog.setCancelable(true);

        Button btn_1 = dialog.findViewById(R.id.btn_ace);
        Button btn_2 = dialog.findViewById(R.id.btn_xmtv);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(FilmCategoryActivity.this, Constant.DEMAND_RAI_M3U_URL);
                dialog.dismiss();
            }
        });

        //btn_1
        btn_1.setFocusable(true);
        final boolean[] flag_btn_1 = {true};
        btn_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (flag_btn_1[0]) {
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_1[0] = false;
                } else {
                    btn_1.setBackgroundResource(R.drawable.section_selection_background);
                    btn_1.invalidate();
                    flag_btn_1[0] = true;
                }
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(FilmCategoryActivity.this, Constant.DEMAND_MEDIASET_M3U_URL);
                dialog.dismiss();
            }
        });
        //btn_2
        btn_2.setFocusable(true);
        final boolean[] flag_btn_2 = {true};
        btn_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"), Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"), Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_2[0]) {
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_2[0] = false;
                } else {
                    btn_2.setBackgroundResource(R.drawable.section_selection_background);
                    btn_2.invalidate();
                    flag_btn_2[0] = true;
                }
            }
        });


        dialog.show();
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
        CheckXml.checkXml(this);
    }
}
