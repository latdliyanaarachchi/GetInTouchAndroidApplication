package com.example.loginsignup;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {


    @POST("user")
    Call<DataModal> createPostSignUp(@Body DataModal dataModal);

    @POST("login")
    Call<LoginResponse> createPostLogin(@Body DataModalLogin dataModalLogin);
}
