package com.evilkingmediabeta;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dtx12.android_animations_actions.actions.Interpolations;

import java.util.Random;

import static com.dtx12.android_animations_actions.actions.Actions.color;
import static com.dtx12.android_animations_actions.actions.Actions.delay;
import static com.dtx12.android_animations_actions.actions.Actions.fadeIn;
import static com.dtx12.android_animations_actions.actions.Actions.fadeOut;
import static com.dtx12.android_animations_actions.actions.Actions.moveBy;
import static com.dtx12.android_animations_actions.actions.Actions.parallel;
import static com.dtx12.android_animations_actions.actions.Actions.play;
import static com.dtx12.android_animations_actions.actions.Actions.scaleTo;
import static com.dtx12.android_animations_actions.actions.Actions.sequence;

public class SplashScreenActivity extends AppCompatActivity {

        private View mTarget, mTargetVersion;
        private Random random = new Random();

        boolean send_flag = true;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else{
                // do something for phones running an SDK before lollipop
            }


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                setContentView(R.layout.activity_splash);
            } else {
                setContentView(R.layout.activity_splash_portrait);
            }

            mTarget = findViewById(R.id.splash_logo_img);//

            float targetY = 20;
            float delay = random.nextFloat();
            play(sequence(fadeOut(), scaleTo(1.2f, 1.2f), fadeIn(2), delay(delay),
                    parallel(moveBy(0, targetY, 2, Interpolations.ElasticEaseOut), sequence(color(-1, Color.GREEN, 1), color(Color.GREEN, Color.WHITE, 1)),
                            scaleTo(1, 1, 2, Interpolations.ElasticEaseOut))), mTarget);

            mTargetVersion = findViewById(R.id.splash_version);//

            play(sequence(fadeOut(), delay(4 + delay), fadeIn(1)), mTargetVersion);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        Intent i = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                        startActivity(i);
                        finish();

                }
            }, 6 * 1000);
        }

}


