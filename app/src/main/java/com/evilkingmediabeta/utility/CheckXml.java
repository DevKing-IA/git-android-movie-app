package com.evilkingmediabeta.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evilkingmediabeta.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckXml {
    private static Float currentVersionNumber;
    private static Dialog dialog;
    private static LinearLayout versionLy, messageLy;
    private static TextView version_description, message_description;
    private static TextView negativeBtn, positiveBtn;

    public static void checkXml(Context context){

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notification_dialog);

        versionLy = dialog.findViewById(R.id.versionLy);
        messageLy = dialog.findViewById(R.id.messageLy);

        version_description = dialog.findViewById(R.id.version_description);
        message_description = dialog.findViewById(R.id.message_description);

        negativeBtn = dialog.findViewById(R.id.negativeBtn);
        positiveBtn = dialog.findViewById(R.id.positiveBtn);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(context.getResources().getDimensionPixelSize(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);

        Call<GetXmlInfo> apiCall = ApiClient.getApiXmlClient().getXml();
        apiCall.enqueue(new Callback<GetXmlInfo>() {
            @Override
            public void onResponse(Call<GetXmlInfo> call, Response<GetXmlInfo> response) {
                getVersionAndMessage(context, response.body().getVersion(), response.body().getMessage());
            }

            @Override
            public void onFailure(Call<GetXmlInfo> call, Throwable t) {
                Log.d("EKM", t.getMessage());
            }
        });
    }

    private static void getVersionAndMessage(Context context, String version, String message){

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionNumber = Float.parseFloat(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        Float newVersionNumber = Float.parseFloat(version);
        SharedPreferences preferences = context.getSharedPreferences("ekm_pref", Context.MODE_PRIVATE);
        String oldMessage = preferences.getString("message", "");
        SharedPreferences.Editor editor = context.getSharedPreferences("ekm_pref", Context.MODE_PRIVATE).edit();
        assert oldMessage != null;
        if (currentVersionNumber < newVersionNumber && !oldMessage.equals(message)){
            versionLy.setVisibility(View.VISIBLE);
            messageLy.setVisibility(View.VISIBLE);
            String updateStr = "New version " + newVersionNumber + " is released. Click DOWNLOAD NOW to download please.";
            version_description.setText(updateStr);
            message_description.setText(message);
            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.evilkingmedia.com/download/"));
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show();
            editor.putString("message", message);
            editor.apply();
        } else if (currentVersionNumber < newVersionNumber){
            versionLy.setVisibility(View.VISIBLE);
            String updateStr = "New version " + newVersionNumber + " is released. Click DOWNLOAD NOW to download please.";
            version_description.setText(updateStr);
            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.evilkingmedia.com/download/"));
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else if (!oldMessage.equals(message)){
            messageLy.setVisibility(View.VISIBLE);
            message_description.setText(message);
            negativeBtn.setVisibility(View.GONE);
            positiveBtn.setText("OK");
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            editor.putString("message", message);
            editor.apply();
        }
    }
}
