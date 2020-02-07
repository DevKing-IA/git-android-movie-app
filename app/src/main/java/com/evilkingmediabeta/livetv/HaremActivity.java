package com.evilkingmediabeta.livetv;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CheckXml;

public class HaremActivity extends AppCompatActivity {

    LinearLayout listeiptv, webcaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_harem);
        } else {
            setContentView(R.layout.activity_harem_portrait);
        }

        listeiptv = findViewById(R.id.listeiptvLayout);
        webcaster = findViewById(R.id.webcasterLayout);

        listeiptv.setOnClickListener(v -> Constant.openExternalBrowser(HaremActivity.this, Constant.LIVETV_HAREM_LISTEIPTV_URL));

        webcaster.setOnClickListener(v -> startActivity(new Intent(HaremActivity.this, HaremWebCasterActivity.class)));

        Constant.setFocusEvent(this, listeiptv, webcaster);
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        CheckXml.checkXml(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(HaremActivity.this, LiveTvCategoryActivity.class));
    }
    @Override
    protected void onPause() {
        super.onPause();
        CheckXml.checkXml(this);
    }
}
