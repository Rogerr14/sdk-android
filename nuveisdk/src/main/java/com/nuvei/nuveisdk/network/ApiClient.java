package com.nuvei.nuveisdk.network;

import com.nuvei.nuveisdk.Nuvei;
import com.nuvei.nuveisdk.helpers.GlobalHelper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL_PROD = "ttps://ccapi.paymentez.com";
    private static final String BASE_URL_TEST = "https://ccapi-stg.paymentez.com";

    public static Retrofit getClient(String code, String key){

            String baseUrl = Nuvei.isTestMode() ? BASE_URL_TEST : BASE_URL_PROD;
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient.Builder client = new OkHttpClient.Builder();
            if (Nuvei.isTestMode()) {
                client.addInterceptor(logging);
            }
            String token = GlobalHelper.getAuthToken(key, code);
            client.addInterceptor(chain -> {
                Request request =  chain.request().newBuilder()
                        .addHeader("Content-type", "application/json")
                        .addHeader("Auth-Token", token )
                        .build();
                        return chain.proceed(request);
            });
            OkHttpClient newClient = client.build();

            return  new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(newClient)
                    .build();

    }

}
