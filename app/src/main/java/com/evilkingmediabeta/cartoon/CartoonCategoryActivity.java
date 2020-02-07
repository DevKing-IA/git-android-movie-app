package com.evilkingmediabeta.cartoon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;

import io.fabric.sdk.android.services.network.HttpRequest;

public class CartoonCategoryActivity extends AppCompatActivity {
    LinearLayout rlViewCartoons, rlCartoonWebCaster, rlEkmCartoons, rlAnimeUnity, rlCartoonYoutube, cercacartoni, rlCartoonEmbed;
    String m_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_cartoon_category);
        } else {
            setContentView(R.layout.activity_cartoon_category_portrait);
        }
        cercacartoni = findViewById(R.id.cercacartoni);
        rlViewCartoons = findViewById(R.id.rlViewCartoons);
        rlCartoonWebCaster = findViewById(R.id.rlCartoonWebCaster);
        rlAnimeUnity = findViewById(R.id.rlAnimeUnity);
        rlEkmCartoons = findViewById(R.id.rlEkmCartoons);
        rlCartoonEmbed = findViewById(R.id.rlCartoonEmbed);
        rlCartoonYoutube = findViewById(R.id.rlCartoonYoutube);

        rlViewCartoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(CartoonCategoryActivity.this, ViewCartoonsActivity.class);
               startActivity(i);
            }
        });
        rlCartoonWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartoonCategoryActivity.this, CartoonWebCasterActivity.class));
            }
        });
        rlEkmCartoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(CartoonCategoryActivity.this, Constant.EKM_CARTOONS_URL);
            }
        });
        rlAnimeUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartoonCategoryActivity.this, AnimeUnityActivity.class));
            }
        });

        rlCartoonYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartoonCategoryActivity.this, YoutubeCartoonActivity.class));
            }
        });

        rlCartoonEmbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartoonCategoryActivity.this, Film2Activity.class));
            }
        });


        cercacartoni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(CartoonCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(CartoonCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Cartoni", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=cartoni&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(CartoonCategoryActivity.this, "xmtv://"+tmp);
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

        Constant.setFocusEvent(this, rlCartoonWebCaster, rlEkmCartoons, rlAnimeUnity, rlCartoonYoutube, cercacartoni, rlCartoonEmbed);
        CheckXml.checkXml(this);
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
