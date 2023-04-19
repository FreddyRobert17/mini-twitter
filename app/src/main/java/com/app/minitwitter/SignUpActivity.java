package com.app.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSignUp;
    TextView tvGoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp = findViewById(R.id.button_signup);
        tvGoLogin = findViewById(R.id.text_view_go_login);

        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.button_signup:
                break;
            case R.id.text_view_go_login:
                goToLogin();
                break;
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}