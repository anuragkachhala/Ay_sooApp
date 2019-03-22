package com.ay_sooapp.RestApi;

import com.ay_sooapp.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://94.16.118.118:8080/rest/";

    public static final String HEADER_CONTENT_TYPE = "application/json";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
            builder.addInterceptor(interceptor);
            okhttp3.OkHttpClient okHttpClient = builder.build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Map<String, String> getHeader() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", HEADER_CONTENT_TYPE);

        return map;


    }

    public static Map<String, String> getHeaders() {
        SessionManager sessionManager = SessionManager.getInstance();
        Map<String, String> map = new HashMap<String, String>();

        map.put("Content-Type", HEADER_CONTENT_TYPE);

        //we get token from shared pref
        map.put("Authorization", "basic " + sessionManager.getToken());
        return map;
    }

    public static class MyOkHttpInterceptor implements Interceptor {
        public static final String HEADER_CONTENT_TYPE = "application/x-www-form-urlencoded";


        @Override
        public Response intercept(Chain chain) throws IOException {
            SessionManager sessionManager = SessionManager.getInstance();
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .header("Content-Type", HEADER_CONTENT_TYPE)
                    .header("Authorization", "basic " + sessionManager.getToken())
                    .build();
            return chain.proceed(newRequest);
        }
    }
}
