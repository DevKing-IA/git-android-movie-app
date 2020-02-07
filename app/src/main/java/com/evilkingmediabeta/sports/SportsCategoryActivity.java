package com.evilkingmediabeta.sports;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.player.WebViewActivity;
import com.evilkingmediabeta.sports.ekmsportsembed.EkmSportsEmbedActivity;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.webview.LiveChannelsAce1Activity;
import com.evilkingmediabeta.webview.LiveEventsAce2Activity;
import com.evilkingmediabeta.webview.SearchAceFixActivity;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.HttpRequest;

public class SportsCategoryActivity extends AppCompatActivity {

    LinearLayout rlSchedule, rlSportsEPG, rlCricfree, rlEKSport, rlHighlights, rlListeIPTV, rlSportsWebCaster, rlWebView, rlMyP2P, rlSoccer, rlFootballOnDemand, rlSportsEmbed, cercasport;
    String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_sports_category);
        } else {
            setContentView(R.layout.activity_sports_category_portrait);
        }
        cercasport = findViewById(R.id.cercasport);

        rlEKSport = findViewById(R.id.rlEKSport);
        rlCricfree = findViewById(R.id.rlCricfree);
        rlHighlights = findViewById(R.id.rlHighlights);
        rlSportsEPG = findViewById(R.id.rlSportsEPG);
        rlSchedule = findViewById(R.id.rlSchedulesResults);
        rlListeIPTV = findViewById(R.id.rlListeIPTV);
        rlSportsWebCaster = findViewById(R.id.rlSportsWebCaster);
        rlWebView = findViewById(R.id.rlWebView);
        rlMyP2P = findViewById(R.id.rlMyP2P);
        rlSoccer = findViewById(R.id.rlSoccer);
        rlFootballOnDemand = findViewById(R.id.rlFootballOnDemand);
//        rlUsaGoalDNS = findViewById(R.id.rlUsaGoalDNS);
        rlSportsEmbed = findViewById(R.id.rlSportsEmbed);



        cercasport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(SportsCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(SportsCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Sports", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+ HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=sport&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(SportsCategoryActivity.this, "xmtv://"+tmp);
                        Log.d("[Cerca Globale]", "Cerca Avviato!");
                        //Log.d("[Cerca Globale]", "URL: " + tmp);
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


        rlEKSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsEKSportActivity.class));
            }
        });

        rlCricfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsCategoryActivity.this, SportsCricfreeActivity.class);
                startActivity(i);
            }
        });

        rlSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsCategoryActivity.this, WebViewActivity.class);
                i.putExtra("videoUrl",Constant.SPORTSEPGURL);
                startActivity(i);
            }
        });

        rlListeIPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.openExternalBrowser(SportsCategoryActivity.this,Constant.SPORTSBYDOCURL);
            }
        });

        rlSportsWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsWebCasterActivity.class));
            }
        });
        rlHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SportsCategoryActivity.this,WebViewActivity.class);
//                i.putExtra("videoUrl",Constant.SPORTSURL6);
//                startActivity(i);
                Constant.openWVCapp(SportsCategoryActivity.this, Constant.SPORTSURL6);
            }
        });

        rlSportsEPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SportsCategoryActivity.this,WebViewActivity.class);
                i.putExtra("videoUrl",Constant.SPORTSURL7);
                startActivity(i);
            }
        });

        rlWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWebViewDialog();
            }
        });

        rlMyP2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsMyp2pActivity.class));
            }
        });

        rlSoccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsSoccerActivity.class));
            }
        });

        rlFootballOnDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, FootballOnDemandActivity.class));
            }
        });

//        rlUsaGoalDNS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Constant.openWVCapp(SportsCategoryActivity.this, Constant.SPORTS_USA_GOALS_DNS_URL);
//            }
//        });

        rlSportsEmbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSportsEmbedDialog();
            }
        });

        Constant.setFocusEvent(this, cercasport, rlSchedule, rlSportsEPG, rlCricfree, rlEKSport, rlHighlights, rlListeIPTV, rlSportsWebCaster, rlWebView, rlMyP2P, rlSoccer, rlFootballOnDemand, rlSportsEmbed);
        CheckXml.checkXml(this);

    }

    private void showSportsEmbedDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sport_embed);
        dialog.setCancelable(true);

        Button btn_1 = dialog.findViewById(R.id.btn_ace);
        Button btn_2 = dialog.findViewById(R.id.btn_xmtv);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsEmbedScrapeActivity.class));
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                    startActivity(new Intent(SportsCategoryActivity.this, EkmSportsEmbedActivity.class));
                } else{
                    Toast.makeText(SportsCategoryActivity.this, "[ATTENZIONE]\nFunzione non disponibile per versioni minori di Android 5.0",
                            Toast.LENGTH_LONG).show();                }
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

    private void showWebViewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sport_acestream);
        dialog.setCancelable(true);

        Button btn_ace = dialog.findViewById(R.id.btn_sito1);
        Button btn_xmtv = dialog.findViewById(R.id.btn_sito2);
        Button btn_search = dialog.findViewById(R.id.btn_searchace);

        btn_ace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportsCategoryActivity.this, LiveChannelsAce1Activity.class);
                intent.putExtra("web_url", Constant.SPORTS_WEBACE_URL1);
                intent.putExtra("flag", "ace");
                startActivity(intent);
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
                Intent intent = new Intent(SportsCategoryActivity.this, LiveEventsAce2Activity.class);
                intent.putExtra("web_url", Constant.SPORTS_WEBACE_URL2);
                intent.putExtra("flag", "xmtv");
                startActivity(intent);
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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportsCategoryActivity.this, SearchAceFixActivity.class);
                intent.putExtra("web_url", Constant.SPORTS_WEBACE_URLSEARCH);
                intent.putExtra("flag", "ace");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //btn_ace
        btn_search.setFocusable(true);
        final boolean[] flag_btn_search = {true};
        btn_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int[] colors = {Color.parseColor("#A20000"),Color.parseColor("#550101")};
            int[] colors2 = {Color.parseColor("#550101"),Color.parseColor("#A20000")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_btn_search[0]){
                    btn_search.setBackgroundResource(R.drawable.section_selection_background);
                    flag_btn_search[0] = false;
                } else {
                    btn_search.setBackgroundResource(R.drawable.section_selection_background);
                    btn_search.invalidate();
                    flag_btn_search[0] = true;
                }
            }
        });

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
}
