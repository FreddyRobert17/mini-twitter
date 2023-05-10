package com.app.minitwitter.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.ui.fragments.AddTweetDialogFragment;
import com.app.minitwitter.ui.fragments.ProfileFragment;
import com.app.minitwitter.ui.fragments.TweetListFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CircleImageView imageProfile;
    BottomNavigationView bottomNavigationView;
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = findViewById(R.id.fab);
        imageProfile = findViewById(R.id.profile_image);
        tvUsername = findViewById(R.id.username);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    replaceFragment(TweetListFragment.newInstance(Constants.TWEET_LIST_ALL));
                    fab.show();
                    break;
                case R.id.menu_favorites:
                    replaceFragment(TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS));
                    fab.hide();
                    break;
                case R.id.menu_profile:
                    replaceFragment(new ProfileFragment());
                    fab.hide();
                    break;
            }
            return true;
        });

        fab.setOnClickListener(view -> {
            AddTweetDialogFragment dialog = new AddTweetDialogFragment();
            dialog.show(getSupportFragmentManager(), "NewTweetDialogFragment");
        });

        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTOURL);
        String userName = SharedPreferencesManager.getStringValue(Constants.PREF_USER_NAME);

        tvUsername.setText(userName);

        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.unknown_person)
                .into(imageProfile);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment ,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> finishAffinity())
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {}).show();
    }
}