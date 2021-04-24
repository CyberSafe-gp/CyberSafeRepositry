package com.example.cybersafe;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClient {
    private Retrofit retrofit;
    public void generateClient(){
        //create Http client
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(6000, TimeUnit.SECONDS)
                .connectTimeout(6000, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cyberdetectorml.herokuapp.com") //our hosting model
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }


    //return the Api
    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
