package com.evilkingmediabeta.cerca;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.services.network.HttpRequest;

public class NewCercaDialog extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Dialog password_dialog = new Dialog(NewCercaDialog.this);
            password_dialog.setContentView(R.layout.harempassword_dialog);
            TextView negativeBtn, positiveBtn;
            EditText etPassword;
            negativeBtn = password_dialog.findViewById(R.id.negativeBtn);
            positiveBtn = password_dialog.findViewById(R.id.positiveBtn);
            etPassword = password_dialog.findViewById(R.id.etPassword);

            etPassword.setFocusable(true);
            CustomKeyboardHandler.showKeyboard(NewCercaDialog.this);

            etPassword.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus) {
                    CustomKeyboardHandler.showKeyboard(NewCercaDialog.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(NewCercaDialog.this, password_dialog.getWindow().getDecorView().getWindowToken());
                }
            });

            negativeBtn.setOnClickListener(v12 -> password_dialog.dismiss());

            positiveBtn.setOnClickListener(v13 -> {
                String manual_pass = etPassword.getText().toString();
                String baseurl = "https://supermyspace.xyz/cerca/cerca.php?s="+manual_pass+"&add=Cerca&cat=film";
                String tmp = HttpRequest.Base64.encodeBytes(baseurl.getBytes());
                Constant.openExternalBrowser(NewCercaDialog.this, "xmtv://"+tmp);

            }); }, 6 * 1000);
    }
}
