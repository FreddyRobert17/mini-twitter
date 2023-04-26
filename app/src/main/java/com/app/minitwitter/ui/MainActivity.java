package com.app.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.data.network.TwitterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvGoSignUp;

    EditText editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLoggedUser();

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViews();

        addEvents();
    }

    private void checkLoggedUser(){
        String token = SharedPreferencesManager.getStringValue(Constants.PREF_TOKEN);

        if(!token.isEmpty()){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    private void findViews(){
        btnLogin = findViewById(R.id.button_login);
        tvGoSignUp = findViewById(R.id.text_view_go_signup);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
    }

    private void addEvents(){
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.button_login:
                goToLogin();
                break;
            case R.id.text_view_go_signup:
                goToSignUp();
                break;
        }
    }

    private void goToLogin(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.isEmpty()){
            editTextEmail.setError(getResources().getString(R.string.editText_email_error_message));
        } else if (password.isEmpty()) {
            editTextPassword.setError(getResources().getString(R.string.editText_password_error_message));
        } else {
            RequestLogin requestLogin = new RequestLogin(email, password);
            TwitterService twitterService = new TwitterService();
            TwitterRepository twitterRepository = new TwitterRepository(twitterService);
            String token = SharedPreferencesManager.getStringValue(Constants.PREF_TOKEN);

            Call<ResponseAuth> call =  twitterRepository.doLogin(requestLogin, "Bearer " + token);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "tosdo bien", Toast.LENGTH_LONG).show();
                        goToDashboard();
                    } else {
                        Toast.makeText(MainActivity.this, "not successful response "  + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "failure response" + t, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void goToDashboard(){
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}