package com.app.minitwitter.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.viewmodel.TweetViewModel;

import java.util.ArrayList;
import java.util.List;

public class TweetListFragment extends Fragment {
    private int tweetListType = Constants.TWEET_LIST_ALL;
    RecyclerView recyclerView;
    List<Tweet> tweetList = new ArrayList<>();
    TweetListRecyclerViewAdapter tweetListRecyclerViewAdapter;
    TweetViewModel tweetViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView ivNoDataImage;
    TextView tvNoDataMessage;

    public TweetListFragment() {
    }

    public static TweetListFragment newInstance(int tweetListType){
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.Factory.from(TweetViewModel.initializer))
                .get(TweetViewModel.class);

        if(getArguments() != null){
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tweet_list_container, container, false);

            Context context = view.getContext();

            recyclerView = view.findViewById(R.id.list);
            ivNoDataImage = view.findViewById(R.id.iv_not_found_image);
            tvNoDataMessage = view.findViewById(R.id.tv_not_found_text);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);

                    if(tweetListType == Constants.TWEET_LIST_ALL){
                        refreshData();
                    } else if(tweetListType == Constants.TWEET_LIST_FAVS) {
                        refreshFavData();
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            tweetListRecyclerViewAdapter = new TweetListRecyclerViewAdapter(getActivity(), tweetList);
            recyclerView.setAdapter(tweetListRecyclerViewAdapter);
            tweetListRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if(tweetList.isEmpty()){
                        ivNoDataImage.setVisibility(View.VISIBLE);
                        tvNoDataMessage.setVisibility(View.VISIBLE);
                    } else {
                        ivNoDataImage.setVisibility(View.GONE);
                        tvNoDataMessage.setVisibility(View.GONE);
                    }
                }
            });

            if(tweetListType == Constants.TWEET_LIST_ALL){
                loadAllTweetData();
            } else if(tweetListType == Constants.TWEET_LIST_FAVS) {
                loadFavData();
            }

            return view;
    }

    private void loadAllTweetData(){
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                tweetListRecyclerViewAdapter.setData(tweets);
            }
        });
    }

    private void loadFavData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                tweetListRecyclerViewAdapter.setData(tweetList);
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

    private void refreshFavData() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                swipeRefreshLayout.setRefreshing(false);
                tweetList = tweets;
                tweetListRecyclerViewAdapter.setData(tweetList);
                tweetViewModel.getNewFavTweets().removeObserver(this);
            }
        });
    }
}