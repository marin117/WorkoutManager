package com.workoutmanager.HttpClient;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit  retrofit = null;
    //private String STRING_BASE_URL = "http://10.0.2.2:3000/";
    private String STRING_BASE_URL = "http://192.168.0.12:3000/";
    //private String STRING_BASE_URL = "http://35.234.81.24:3000/";

    public RetrofitClient(){
        retrofit = null;

    }

    private static Retrofit getRetrofit(String url) {
        if (retrofit == null){
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(url);
            builder.addConverterFactory(ScalarsConverterFactory.create());
            builder.addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.client(httpClient.build()).build();
        }
        return retrofit;
    }

    public RestInterface createClient() {
        Retrofit retrofit = RetrofitClient.getRetrofit(STRING_BASE_URL);

        return retrofit.create(RestInterface.class);
    }

}
