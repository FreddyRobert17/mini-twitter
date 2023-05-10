package com.app.minitwitter.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.viewmodel.TweetViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {
    private TweetViewModel tweetViewModel;
    private String deleteTweetId;

    public static BottomModalTweetFragment newInstance(String tweetId) {
        BottomModalTweetFragment fragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TWEET_ID, tweetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            deleteTweetId = getArguments().getString(Constants.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bottom_modal_tweet, container, false);

        BottomNavigationView navigationView = view.findViewById(R.id.bottom_navigation_view);

        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.action_delete_tweet){
                tweetViewModel.deleteTweet(deleteTweetId);
                getDialog().dismiss();
                return true;
            }
            return false;
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.Factory.from(TweetViewModel.initializer))
                .get(TweetViewModel.class);
    }
}