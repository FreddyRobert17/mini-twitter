package com.app.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.retrofit.request.RequestLogin;
import com.app.minitwitter.retrofit.response.ResponseAuth;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.ui.customViews.ProgressButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGoSignUp;
    EditText editTextEmail, editTextPassword;
    View progressButtonView;
    ProgressButton progressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLoggedUser();

        setContentView(R.layout.activity_login);

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
        tvGoSignUp = findViewById(R.id.text_view_go_signup);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        progressButtonView = findViewById(R.id.custom_progress_button);
        progressButton = new ProgressButton(LoginActivity.this, progressButtonView, "Login");
    }

    private void addEvents(){
        tvGoSignUp.setOnClickListener(this);
        progressButtonView.setOnClickListener(view -> goToLogin());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.custom_progress_button:
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
            progressButton.showLoading();
            RequestLogin requestLogin = new RequestLogin(email, password);
            TwitterRepository twitterRepository = new TwitterRepository();
            String token = SharedPreferencesManager.getStringValue(Constants.PREF_TOKEN);

            Call<ResponseAuth> call =  twitterRepository.doLogin(requestLogin, "Bearer " + token);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, getString(R.string.generic_success_message), Toast.LENGTH_SHORT).show();
                        goToDashboard();
                    } else {
                        progressButton.stopLoading();
                        Toast.makeText(LoginActivity.this,  getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    progressButton.stopLoading();
                    Toast.makeText(LoginActivity.this,  getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToDashboard(){
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}