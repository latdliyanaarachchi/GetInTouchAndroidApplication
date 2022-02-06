package com.example.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        Button btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                EditText email = findViewById(R.id.inputEmail);
                EditText password = findViewById(R.id.inputPassword);

                if (email.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "Please enter values for all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                postData(email.getText().toString(),password.getText().toString());

            }
        });


    }


    private void postData(String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://getintouchapp-env.eba-xiuydug9.us-east-1.elasticbeanstalk.com/api/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModalLogin modalLogin = new DataModalLogin(email,password);

        Call<LoginResponse> call = retrofitAPI.createPostLogin(modalLogin);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse responseFromAPI = response.body();

                if(responseFromAPI.getUserAvailable() && responseFromAPI.getId()!=null){

                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                }else{
                    Toast.makeText(LoginActivity.this, "Please create an account", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}