package com.evilkingmediabeta.utilitymenu;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.mywebcaster.MyWebCasterActivity;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.webview.EPGView;

import io.fabric.sdk.android.Fabric;

public class UtilityMenuActivity extends AppCompatActivity {

    LinearLayout rlepg, rlguide, rlspeedtest, rlwebcaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_epg_and_guide);
        } else {
            setContentView(R.layout.activity_epg_and_guide_portrait);
        }

        rlepg = findViewById(R.id.rlepg);
        rlguide = findViewById(R.id.rlguide);
        rlspeedtest = findViewById(R.id.rlspeedtest);
        rlwebcaster = findViewById(R.id.rlwebcaster);

        rlepg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UtilityMenuActivity.this, EPGView.class));
            }
        });

        rlguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(UtilityMenuActivity.this, Constant.Video_Guide_URL);
            }
        });

        rlwebcaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UtilityMenuActivity.this, MyWebCasterActivity.class));
            }
        });


        rlspeedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UtilityMenuActivity.this, SpeedTestActivity.class));
            }
        });

        Constant.setFocusEvent(this, rlepg, rlguide, rlspeedtest);
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
