package com.app.minitwitter.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.data.network.TwitterService;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.ui.fragments.TweetListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitterRepository {
    TwitterService twitterService;
    MutableLiveData<List<Tweet>> allTweets;
    public TwitterRepository(){
        twitterService = new TwitterService();
        allTweets = getAllTweets();
    }

    public TwitterRepository(TwitterService twitterService){
        this.twitterService = twitterService;
    }

    public Call<ResponseAuth> doLogin(RequestLogin requestLogin, String token){
        return twitterService.doLogin(requestLogin, token);
    }

    public Call<ResponseAuth> doSignUp(RequestSignUp requestSignUp){
        return twitterService.doSignUp(requestSignUp);
    }

    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = twitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                Log.i("TAG", response.body().toString());

                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                } else {
                    Log.i("TAG", "failurem: "  + response);
                }
            }
            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Log.i("TAG", "failurex: "  + t);
            }
        });
        return allTweets;
    }

    public void createTweet(String message){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(message);

        Call<Tweet> call = twitterService.createTweet(requestCreateTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> clonedList = new ArrayList<>();
                    clonedList.add(response.body());

                    for(int i = 0; i < allTweets.getValue().size(); i++){
                        clonedList.add(allTweets.getValue().get(i));
                    }

                    allTweets.postValue(clonedList);
                } else {
                    Log.i("TAG", "failure: "  + response);
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Log.i("TAG", "failure: "  + t);
            }
        });
    }
}
