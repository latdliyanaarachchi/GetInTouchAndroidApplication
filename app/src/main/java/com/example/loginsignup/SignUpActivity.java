package com.example.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


                if (userName.getText().toString().isEmpty() && password.getText().toString().isEmpty() && email.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(password.getText().toString()!=cPassword.getText().toString()){
//                    Toast.makeText(SignUpActivity.this, "Password and Confirm Password Does not matching", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // calling a method to post the data and passing our name and job.
                postData(userName.getText().toString(), password.getText().toString(),email.getText().toString());
            }

        });
    }

    private void postData(String name, String email , String password) {


       // loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal modal = new DataModal(name, email , password);

        Call<DataModal> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                Toast.makeText(SignUpActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

//                jobEdt.setText("");
//                nameEdt.setText("");

                DataModal responseFromAPI = response.body();

                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getEmail();

                Toast.makeText(SignUpActivity.this, responseString, Toast.LENGTH_SHORT).show();
               // responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {

                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                // setting text to our text view when
                // we get error response from API.
               // responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}
