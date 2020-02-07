package com.evilkingmediabeta.utilitymenu;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.R;

public class InfoDispositivoActivity extends AppCompatActivity {

    TextView androidVersion,getAndroidVersion, androidBuild,getAndroidBuild;
    boolean flag = false;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        TextView textView = (TextView) findViewById(R.id.getAndroidBuild);

        androidBuild = findViewById(R.id.androidBuild);
        androidVersion = findViewById(R.id.androidVersion);
        getAndroidBuild = findViewById(R.id.getAndroidBuild);
        getAndroidVersion = findViewById(R.id.getAndroidVersion);

        setContentView(R.layout.activity_info_disp);

        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        //to add to textview
        getAndroidVersion.setText(deviceName);

    }
    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }
}
