package com.example.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 1;


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

        //google authentication

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=(SignInButton)findViewById(R.id.btnGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}