package com.app.minitwitter.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.data.network.TwitterService;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitterRepository {
    TwitterService twitterService;
    MutableLiveData<List<Tweet>> allTweets;

    MutableLiveData<List<Tweet>> favTweets;

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

    public MutableLiveData<List<Tweet>> getFavTweets(){
        if(favTweets == null){
            favTweets = new MutableLiveData<>();
        }

        List<Tweet> currentList = allTweets.getValue();
        List<Tweet> favList = new ArrayList<>();
        String userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);

        for(Tweet tweet: currentList){
            if(tweet.getLikes().contains(userId)){
                favList.add(tweet);
            }
        }

        favTweets.postValue(favList);

        return favTweets;
    }

    public void createTweet(String userId, String message){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(userId, message);

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

    public void likeTweet(String userId, String idTweet){
        Call<Tweet> call = twitterService.likeTweet(userId, idTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> clonedList = new ArrayList<>();

                    for(int i = 0; i < allTweets.getValue().size(); i++){
                        if(Objects.equals(allTweets.getValue().get(i).getId(), response.body().getId())){
                            clonedList.add(response.body());
                        } else {
                          clonedList.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }

                    allTweets.setValue(clonedList);

                    getFavTweets();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {

            }
        });
    }
}
