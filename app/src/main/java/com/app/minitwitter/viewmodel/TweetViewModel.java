package com.app.minitwitter.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.app.minitwitter.common.MyApplication;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TwitterRepository twitterRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);

        twitterRepository = new TwitterRepository();
        tweets = twitterRepository.getAllTweets();
    }

    public void createTweet(String userId, String message){
        twitterRepository.createTweet(userId, message);
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets = twitterRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getFavTweets(){
        favTweets = twitterRepository.getFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets(){
        return getFavTweets();
    }

    public void likeTweet(String userId, String idTweet){
        twitterRepository.likeTweet(userId, idTweet);
    }

    public static final ViewModelInitializer<TweetViewModel> initializer = new ViewModelInitializer<>(
            TweetViewModel.class,
            creationExtras -> new TweetViewModel(MyApplication.getInstance())
    );
}
