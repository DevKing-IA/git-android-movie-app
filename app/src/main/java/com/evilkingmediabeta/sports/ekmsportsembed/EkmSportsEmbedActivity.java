package com.evilkingmediabeta.sports.ekmsportsembed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.ekmsportsembed.fragment.MainHomeFragment;

import io.fabric.sdk.android.Fabric;

public class EkmSportsEmbedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_ekm_sports_embed);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new MainHomeFragment())
                .commit();

    }
}
