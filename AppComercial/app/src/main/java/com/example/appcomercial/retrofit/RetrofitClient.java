package com.example.appcomercial.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String URL_API_SERVICE = "http://10.10.42.182:81";
    public static String API_TOKEN;

    private static class AuthInterceptor implements Interceptor{

        @Override
        public Response intercept(final Chain chain) throws IOException {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder().header("Content-Type", "application/json");
            //Verificar si ya tenemos un token disponible para agregarlo a la cabecera
            final String token = API_TOKEN;
            if (token != null && !token.isEmpty()){
                requestBuilder.header("Authorization", "Bearer " + token);
                Log.e("API TOKEN", token);
            }
            final Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor());

    public static Retrofit urlComercial = new Retrofit.Builder()
            .baseUrl(URL_API_SERVICE)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService createService(){
        return urlComercial.create(ApiService.class);
    }
}
