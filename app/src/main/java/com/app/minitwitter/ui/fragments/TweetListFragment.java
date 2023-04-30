package com.app.minitwitter.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.minitwitter.R;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.data.network.TwitterService;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.viewmodel.TweetViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetListFragment extends Fragment {
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    List<Tweet> tweetList = new ArrayList<>();
    TweetListRecyclerViewAdapter tweetListRecyclerViewAdapter;
    TweetViewModel tweetViewModel;

    public TweetListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.Factory.from(TweetViewModel.initializer))
                .get(TweetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list_container, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            tweetListRecyclerViewAdapter = new TweetListRecyclerViewAdapter(getActivity(), tweetList);
            recyclerView.setAdapter(tweetListRecyclerViewAdapter);

            loadTweetData();
        }
        return view;
    }

    private void loadTweetData(){
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                tweetListRecyclerViewAdapter.setData(tweets);
            }
        });
    }
}