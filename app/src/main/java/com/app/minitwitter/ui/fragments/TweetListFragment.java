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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    SwipeRefreshLayout swipeRefreshLayout;

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

            Context context = view.getContext();

            recyclerView = view.findViewById(R.id.list);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_blue));

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    refreshData();
                }
            });

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            tweetListRecyclerViewAdapter = new TweetListRecyclerViewAdapter(getActivity(), tweetList);
            recyclerView.setAdapter(tweetListRecyclerViewAdapter);

            loadTweetData();

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

    private void refreshData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                swipeRefreshLayout.setRefreshing(false);
                tweetList = tweets;
                tweetListRecyclerViewAdapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }
}