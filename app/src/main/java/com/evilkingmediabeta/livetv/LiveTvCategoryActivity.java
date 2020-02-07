package com.evilkingmediabeta.livetv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.share.ShareActivity;
import com.evilkingmediabeta.utility.ApiClient;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;
import com.evilkingmediabeta.utility.GetXmlInfo;

import io.fabric.sdk.android.services.network.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveTvCategoryActivity extends AppCompatActivity {
    LinearLayout listeiptv, tvshare, tfb, tvwebcaster, harem, cercacanaleiptv;
    String m_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_live_category);
        } else {
            setContentView(R.layout.activity_live_category_portrait);
        }
        cercacanaleiptv = findViewById(R.id.cercacanaleiptv);
        listeiptv = findViewById(R.id.rlListeIPTV);
        tvshare = findViewById(R.id.tvshareLayout);
        tfb = findViewById(R.id.tfbLayout);
        tvwebcaster = findViewById(R.id.tvwebcasterLayout);
        harem = findViewById(R.id.haremLayout);

        listeiptv.setOnClickListener(view -> Constant.openExternalBrowser(LiveTvCategoryActivity.this, Constant.LIVETV_LISTEIPTV_URL));
        cercacanaleiptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CategoryActivity.this, CercaGlobaleActivity.class));
                //String manual_pass = etPassword.getText().toString();
                long unixTime = System.currentTimeMillis() / 1000L*139;
                AlertDialog.Builder builder = new AlertDialog.Builder(LiveTvCategoryActivity.this);

                builder.setTitle("Cosa vuoi cercare?");
                // Set up the input
                final EditText input = new EditText(LiveTvCategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Cerca su Live TV", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String baseurl = "https://supermyspace.xyz/cerca/getm3u.php?s="+ HttpRequest.Base64.encodeBytes(m_Text.getBytes())+"&add=Cerca&cat=iptv&tokn="+unixTime+"&drptkey=EK:40-200000BUILD&url=1.m3u";
                        String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                        Constant.openExternalBrowser(LiveTvCategoryActivity.this, "xmtv://"+tmp);
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

        tvshare.setOnClickListener(view -> {
            Intent intent = new Intent(LiveTvCategoryActivity.this, ShareActivity.class);
            intent.putExtra("type", "live");
            startActivity(intent);
        });


        tfb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TFBUrl));
            startActivity(intent);
        });

        tvwebcaster.setOnClickListener(v -> startActivity(new Intent(LiveTvCategoryActivity.this, TvWebCasterActivity.class)));

        // Old harem splash
        //harem.setOnClickListener(v -> startActivity(new Intent(LiveTvCategoryActivity.this, HaremSplash.class)));


        harem.setOnClickListener(v -> {
            Dialog password_dialog = new Dialog(LiveTvCategoryActivity.this);
            password_dialog.setContentView(R.layout.harempassword_dialog);
            TextView negativeBtn, positiveBtn;
            EditText etPassword;
            negativeBtn = password_dialog.findViewById(R.id.negativeBtn);
            positiveBtn = password_dialog.findViewById(R.id.positiveBtn);
            etPassword = password_dialog.findViewById(R.id.etPassword);

            etPassword.setFocusable(true);
            CustomKeyboardHandler.showKeyboard(LiveTvCategoryActivity.this);

            etPassword.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(LiveTvCategoryActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(LiveTvCategoryActivity.this, password_dialog.getWindow().getDecorView().getWindowToken());
                }
            });

            negativeBtn.setOnClickListener(v12 -> password_dialog.dismiss());

            positiveBtn.setOnClickListener(v13 -> {
                String manual_pass = etPassword.getText().toString();
                if (manual_pass.isEmpty()){
                    Toast.makeText(LiveTvCategoryActivity.this, "Please enter a valid password!", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(LiveTvCategoryActivity.this);
                    progressDialog.setMessage("Checking your password from server...");
                    progressDialog.show();
                    Call<GetXmlInfo> apiCall = ApiClient.getApiXmlClient().getXml();
                    apiCall.enqueue(new Callback<GetXmlInfo>() {
                        @Override
                        public void onResponse(Call<GetXmlInfo> call, Response<GetXmlInfo> response) {
                            assert response.body() != null;
                            if (manual_pass.equals(response.body().getHaremPass())){
                                CustomKeyboardHandler.hiddenKeyboard(LiveTvCategoryActivity.this, etPassword.getWindowToken());
                                startActivity(new Intent(LiveTvCategoryActivity.this, HaremActivity.class));
                                password_dialog.dismiss();
                                progressDialog.dismiss();
                            } else {
                                Dialog alertDialog = new Dialog(LiveTvCategoryActivity.this);
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
        });

        Constant.setFocusEvent(this, listeiptv, tvshare, tfb, tvwebcaster, harem, cercacanaleiptv);
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

