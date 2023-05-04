package com.app.minitwitter.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.ui.fragments.AddTweetDialogFragment;
import com.app.minitwitter.ui.fragments.FavoriteTweetsFragment;
import com.app.minitwitter.ui.fragments.ProfileFragment;
import com.app.minitwitter.ui.fragments.TweetListFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CircleImageView imageProfile;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = findViewById(R.id.fab);
        imageProfile = findViewById(R.id.profile_image);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        replaceFragment(TweetListFragment.newInstance(Constants.TWEET_LIST_ALL));
                        break;
                    case R.id.menu_favorites:
                        replaceFragment(TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS));
                        break;
                    case R.id.menu_profile:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return true;
            }
        });

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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment ,fragment);
        fragmentTransaction.commit();
    }
}