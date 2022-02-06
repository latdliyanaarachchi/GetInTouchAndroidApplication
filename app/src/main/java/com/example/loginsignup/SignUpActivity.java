package com.example.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView btn = findViewById(R.id.Alreadyamember);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        View signUpBtn = findViewById(R.id.btnSignUp);

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText userName = findViewById(R.id.inputUsername);
                EditText email = findViewById(R.id.inputEmail);
                EditText password = findViewById(R.id.inputPassword);
                EditText cPassword = findViewById(R.id.inputConfirmPassword);
                String emailContent = email.getText().toString().trim();
                //regular expression for email validation
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                //validate for empty string
                if (email.length() <= 0 || password.length() <= 0 || userName.length() <= 0) {
                    Toast.makeText(SignUpActivity.this, "Please enter values for all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                //email validate
                if (!emailContent.matches(emailPattern)) {
                    Toast.makeText(SignUpActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //empty string validation
                if (userName.getText().toString().isEmpty() && password.getText().toString().isEmpty() && email.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }

                //password and confirm password validation
                if (password.getText().toString() != cPassword.getText().toString()) {
                    Toast.makeText(SignUpActivity.this, "Password and Confirm Password Does not matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                // create POST api call to sign up
                postData(userName.getText().toString(), password.getText().toString(), email.getText().toString());
            }

        });
    }

    private void postData(String name, String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://getintouchapp-env.eba-xiuydug9.us-east-1.elasticbeanstalk.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal modal = new DataModal(name, email, password);

        Call<DataModal> call = retrofitAPI.createPostSignUp(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                DataModal responseFromAPI = response.body();
                Toast.makeText(SignUpActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getEmail();
                Toast.makeText(SignUpActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
