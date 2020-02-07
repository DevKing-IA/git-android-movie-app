package com.evilkingmediabeta.livetv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dtx12.android_animations_actions.actions.Interpolations;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.ApiClient;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;
import com.evilkingmediabeta.utility.GetXmlInfo;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dtx12.android_animations_actions.actions.Actions.color;
import static com.dtx12.android_animations_actions.actions.Actions.delay;
import static com.dtx12.android_animations_actions.actions.Actions.fadeIn;
import static com.dtx12.android_animations_actions.actions.Actions.fadeOut;
import static com.dtx12.android_animations_actions.actions.Actions.moveBy;
import static com.dtx12.android_animations_actions.actions.Actions.parallel;
import static com.dtx12.android_animations_actions.actions.Actions.play;
import static com.dtx12.android_animations_actions.actions.Actions.scaleTo;
import static com.dtx12.android_animations_actions.actions.Actions.sequence;

public class HaremSplash extends AppCompatActivity {

    private View mTarget, mTargetVersion;
    private Random random = new Random();

    boolean send_flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_splash_harem);
        } else {
            setContentView(R.layout.activity_splash_harem_portrait);
        }

        mTarget = findViewById(R.id.splash_logo_img);//

        float targetY = 20;
        float delay = random.nextFloat();
        play(sequence(fadeOut(), scaleTo(1, 1), fadeIn(1), delay(delay),
                parallel(moveBy(0, targetY, 2, Interpolations.ElasticEaseOut), sequence(color(-1, Color.RED, 1), color(Color.WHITE, Color.RED, 1)),
                        scaleTo(1, 1, 2, Interpolations.ElasticEaseOut))), mTarget);

        mTargetVersion = findViewById(R.id.splash2);//

        play(sequence(fadeOut(), delay(4 + delay), fadeIn(1)), mTargetVersion);

        new Handler().postDelayed(() -> {
            Dialog password_dialog = new Dialog(HaremSplash.this);
            password_dialog.setContentView(R.layout.harempassword_dialog);
            TextView negativeBtn, positiveBtn;
            EditText etPassword;
            negativeBtn = password_dialog.findViewById(R.id.negativeBtn);
            positiveBtn = password_dialog.findViewById(R.id.positiveBtn);
            etPassword = password_dialog.findViewById(R.id.etPassword);

            etPassword.setFocusable(true);
            CustomKeyboardHandler.showKeyboard(HaremSplash.this);

            etPassword.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(HaremSplash.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(HaremSplash.this, password_dialog.getWindow().getDecorView().getWindowToken());
                }
            });

            negativeBtn.setOnClickListener(v12 -> password_dialog.dismiss());

            positiveBtn.setOnClickListener(v13 -> {
                String manual_pass = etPassword.getText().toString();
                if (manual_pass.isEmpty()){
                    Toast.makeText(HaremSplash.this, "Please enter a valid password!", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(HaremSplash.this);
                    progressDialog.setMessage("Checking your password from server...");
                    progressDialog.show();
                    Call<GetXmlInfo> apiCall = ApiClient.getApiXmlClient().getXml();
                    apiCall.enqueue(new Callback<GetXmlInfo>() {
                        @Override
                        public void onResponse(Call<GetXmlInfo> call, Response<GetXmlInfo> response) {
                            assert response.body() != null;
                            if (manual_pass.equals(response.body().getHaremPass())){
                                CustomKeyboardHandler.hiddenKeyboard(HaremSplash.this, etPassword.getWindowToken());
                                startActivity(new Intent(HaremSplash.this, HaremActivity.class));
                                password_dialog.dismiss();
                                progressDialog.dismiss();
                            } else {
                                Dialog alertDialog = new Dialog(HaremSplash.this);
                                alertDialog.setContentView(R.layout.notification_dialog);
                                LinearLayout passwordAlertLy = alertDialog.findViewById(R.id.passwordAlertLy);
                                TextView passalert_description = alertDialog.findViewById(R.id.passalert_description);
                                TextView negativeBtn1 = alertDialog.findViewById(R.id.negativeBtn);
                                TextView positiveBtn1 = alertDialog.findViewById(R.id.positiveBtn);
                                passwordAlertLy.setVisibility(View.VISIBLE);
                                negativeBtn1.setVisibility(View.GONE);
                                positiveBtn1.setText("OK");
                                String alertStr = "Your password is not correct! Please ask to admin!";
                                passalert_description.setText(alertStr);
                                positiveBtn1.setOnClickListener(v131 -> alertDialog.dismiss());
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
                                alertDialog.show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetXmlInfo> call, Throwable t) {
                            Log.d("EKM", t.getMessage());
                        }
                    });
                }
            });

            password_dialog.show();
            }, 6 * 1000);

    }
}
