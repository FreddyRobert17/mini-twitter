package com.app.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.data.TwitterRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    TextView tvGoLogin;
    ScrollView scrollView;

    EditText editTextName, editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

       findViews();

        addEvents();
    }

    private void findViews(){
        scrollView = findViewById(R.id.scroll_view);
        btnSignUp = findViewById(R.id.button_signup);
        tvGoLogin = findViewById(R.id.text_view_go_login);
        editTextName = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
    }

    private void addEvents(){
        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.button_signup:
                goToSignUp();
                break;
            case R.id.text_view_go_login:
                goToLogin();
                break;
        }
    }

    private void goToSignUp(){
        String username = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(username.isEmpty()){
            editTextEmail.setError(getResources().getString(R.string.editText_username_error_message));
        } else if (email.isEmpty()){
            editTextEmail.setError(getResources().getString(R.string.editText_email_error_message));
        } else if (password.isEmpty()) {
            editTextPassword.setError(getResources().getString(R.string.editText_password_error_message));
        } else {
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password);
            TwitterRepository twitterRepository = new TwitterRepository();
            Call<ResponseAuth> call =  twitterRepository.doSignUp(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        SharedPreferencesManager.setStringValue(Constants.PREF_TOKEN, response.body().getAccessToken());
                        SharedPreferencesManager.setStringValue(Constants.PREF_USER_ID, response.body().getId());
                        SharedPreferencesManager.setStringValue(Constants.PREF_USER_NAME, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constants.PREF_USER_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constants.PREF_USER_PASSWORD, response.body().getPassword());
                        Toast.makeText(SignUpActivity.this, getString(R.string.generic_success_message), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this,  getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this,  getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}