package com.evilkingmediabeta.utility;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class ApiClient {
    private static ApiXmlInterface apiXmlService;

    public static ApiXmlInterface getApiXmlClient() {
        if (apiXmlService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl("https://pastebin.com/")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(client)
                    .build();
            apiXmlService = restAdapter.create(ApiXmlInterface.class);
        }

        return apiXmlService;
    }

    public interface ApiXmlInterface {
        @GET("raw/Q5yVjCeF")
        Call<GetXmlInfo> getXml();
    }
}
