package com.example.cybersafe;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//For ourModel
public interface Api {
    @Multipart
    @Headers("Accept: text/html")
    @POST("/predict")
    Call<String> process(
            @Part("message") RequestBody message
    );
}
