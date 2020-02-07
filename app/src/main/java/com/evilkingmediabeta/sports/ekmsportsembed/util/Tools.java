package com.evilkingmediabeta.sports.ekmsportsembed.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;

import com.evilkingmediabeta.BuildConfig;
import com.evilkingmediabeta.R;

public class Tools {

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void share(Context context, String body) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =body +"\n\n"+ context.getString(R.string.share_body) + BuildConfig.APPLICATION_ID;
        String shareSub = "Share app";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

}
