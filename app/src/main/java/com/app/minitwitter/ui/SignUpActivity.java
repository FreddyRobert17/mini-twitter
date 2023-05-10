package com.app.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.retrofit.request.RequestSignUp;
import com.app.minitwitter.retrofit.response.ResponseAuth;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.ui.customElements.ProgressButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGoLogin;
    ScrollView scrollView;
    View progressButtonView;
    ProgressButton progressButton;

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
        tvGoLogin = findViewById(R.id.text_view_go_login);
        editTextName = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        progressButtonView = findViewById(R.id.custom_progress_button);
        progressButton = new ProgressButton(SignUpActivity.this, progressButtonView, "Sign up");
    }

    private void addEvents(){
        progressButtonView.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.custom_progress_button:
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
            progressButton.showLoading();
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password);
            TwitterRepository twitterRepository = new TwitterRepository();
            Call<ResponseAuth> call =  twitterRepository.doSignUp(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        progressButton.stopLoading();

                        saveUserData(response);

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
                    progressButton.stopLoading();
                    Toast.makeText(SignUpActivity.this,  getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserData(Response<ResponseAuth> response) {
        SharedPreferencesManager.setStringValue(Constants.PREF_TOKEN, response.body().getAccessToken());
        SharedPreferencesManager.setStringValue(Constants.PREF_USER_ID, response.body().getId());
        SharedPreferencesManager.setStringValue(Constants.PREF_USER_NAME, response.body().getUsername());
        SharedPreferencesManager.setStringValue(Constants.PREF_USER_EMAIL, response.body().getEmail());
        SharedPreferencesManager.setStringValue(Constants.PREF_USER_PASSWORD, response.body().getPassword());
    }

    private void goToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}