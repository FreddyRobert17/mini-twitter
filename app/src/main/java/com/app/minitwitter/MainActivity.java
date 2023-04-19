package com.app.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvGoSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.button_login);
        tvGoSignUp = findViewById(R.id.text_view_go_signup);

        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.button_login:
                break;
            case R.id.text_view_go_signup:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}