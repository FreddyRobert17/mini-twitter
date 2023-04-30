package com.app.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.ui.fragments.AddTweetDialogFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CircleImageView imageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = findViewById(R.id.fab);
        imageProfile = findViewById(R.id.profile_image);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTweetDialogFragment dialog = new AddTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewTweetDialogFragment");
            }
        });

        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTOURL);

        Glide.with(this)
                .load("https://cdn.pixabay.com/photo/2014/10/06/17/30/child-476507_1280.jpg")
                .into(imageProfile);
    }
}