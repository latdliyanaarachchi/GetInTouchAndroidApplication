package com.example.loginsignup;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {


    @POST("users")
    Call<DataModal> createPost(@Body DataModal dataModal);
}
