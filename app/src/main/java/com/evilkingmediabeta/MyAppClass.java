package com.evilkingmediabeta;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.evilkingmediabeta.sports.ekmsportsembed.network.RetrofitClient;
import com.evilkingmediabeta.sports.ekmsportsembed.network.apis.AppConfigApi;
import com.evilkingmediabeta.sports.ekmsportsembed.network.model.AppConfig;
import com.evilkingmediabeta.sports.ekmsportsembed.util.Config;
import com.evilkingmediabeta.sports.ekmsportsembed.util.NotificationClickHandler;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAppClass extends MultiDexApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext=this;

        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationClickHandler(mContext))
                //.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        SharedPreferences preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("status",true)){
            OneSignal.setSubscription(true);
        }else {
            OneSignal.setSubscription(false);
        }

        getAppConfigInfo();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext(){
        return mContext;
    }

    public void getAppConfigInfo() {

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AppConfigApi appConfigApi = retrofit.create(AppConfigApi.class);
        Call<AppConfig> call = appConfigApi.getAppConfig(Config.API_KEY);
        call.enqueue(new Callback<AppConfig>() {
            @Override
            public void onResponse(Call<AppConfig> call, Response<AppConfig> response) {

                if (response.code() == 200) {

                    AppConfig appConfig = response.body();

                    // save app config info to shared preference
                    saveAppConfigInfo(appConfig);

                }

            }

            @Override
            public void onFailure(Call<AppConfig> call, Throwable t) {
                t.printStackTrace();

                SharedPreferences preferences = getSharedPreferences("appConfig", MODE_PRIVATE);
                Constant.NAV_MENU_STYLE = preferences.getString("navMenuStyle", "grid");
                Constant.IS_ENABLE_PROGRAM_GUIDE = preferences.getBoolean("enableProgramGuide", false);
                Constant.IS_LOGIN_MANDATORY = preferences.getBoolean("loginMandatory", false);
                Constant.IS_GENRE_SHOW = preferences.getBoolean("genreShow", true);
                Constant.IS_COUNTRY_SHOW = preferences.getBoolean("countryShow", true);
            }
        });

    }

    public void saveAppConfigInfo(AppConfig appConfig) {

        Constant.NAV_MENU_STYLE = appConfig.getMenu();
        Constant.IS_ENABLE_PROGRAM_GUIDE = appConfig.isProgramEnable();
        Constant.IS_LOGIN_MANDATORY = appConfig.isLoginMandatory();
        Constant.IS_GENRE_SHOW = appConfig.isGenreVisible();
        Constant.IS_COUNTRY_SHOW = appConfig.isCountryVisible();

        SharedPreferences.Editor editor = getSharedPreferences("appConfig", MODE_PRIVATE).edit();
        editor.putString("navMenuStyle", appConfig.getMenu());
        editor.putBoolean("enableProgramGuide", appConfig.isProgramEnable());
        editor.putBoolean("loginMandatory", appConfig.isLoginMandatory());
        editor.putBoolean("genreShow", appConfig.isGenreVisible());
        editor.putBoolean("countryShow", appConfig.isCountryVisible());
        editor.apply();

    }

}
