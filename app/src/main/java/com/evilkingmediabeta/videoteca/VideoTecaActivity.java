package com.evilkingmediabeta.videoteca;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;

public class VideoTecaActivity extends AppCompatActivity {

    LinearLayout rlDocumentariWebCaster, rlVideoVari, rlVideoEmbed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_video_teca);
        } else {
            setContentView(R.layout.activity_video_teca_portrait);
        }

        rlDocumentariWebCaster = findViewById(R.id.rlDocumentariWebCaster);
//        rlVideoVari = findViewById(R.id.rlVideoVari);
        rlVideoEmbed = findViewById(R.id.rlVideoEmbed);

        rlDocumentariWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(VideoTecaActivity.this, Constant.EVILKINGDOCUMENTARIURL);
            }
        });

//        rlVideoVari.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Constant.openExternalBrowser(VideoTecaActivity.this, Constant.Video_Vari_M3u_Url);
//            }
//        });

        rlVideoEmbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoTecaActivity.this, VideoTecaEmbedActivity.class));
            }
        });

//        Constant.setFocusEvent(this, rlDocumentariWebCaster, rlVideoVari, rlVideoEmbed);
        Constant.setFocusEvent(this, rlDocumentariWebCaster, rlVideoEmbed);
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
